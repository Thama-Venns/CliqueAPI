package com.service.clique.Services;

import com.service.clique.Model.*;
import com.service.clique.Repositories.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class CollaborationsService {

    @Autowired CollaboarationRepository collaboarationRepository;
    @Autowired GroupRepository groupRepository;
    @Autowired PostRepository postRepository;
    @Autowired ProfileRepository profileRepository;
    @Autowired PermissionsRepository permissionsRepository;

    public Iterable<Collaboration> getAllCollaborations() {
        return collaboarationRepository.findAll();
    }
    public Collaboration addCollaboration(Collaboration collaboration) {
        Collaboration coll = null;
        try {
            long profileId = collaboration.getProfile().getId();
            long postId = collaboration.getPost().getId();
            coll = collaboarationRepository.findByProfileIdAndProfileId(profileId, postId);

            if(collaboration != null)
                coll = collaboarationRepository.save(collaboration);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return coll;
    }

    // Add collaboration on created post
    public String addCollaboration(long postId, long profileId) {
        String result = "Collaboration created";
        Collaboration collaboration = null;
        try {
            Post post = postRepository.findById(postId).get();
            Profile profile = profileRepository.findById(profileId).get();
            long postGroupId = post.getGroup().getId();


            Collection<Permission> permissions = (Collection<Permission>) permissionsRepository.findPermissionsOnGroups(postGroupId);

            if(post.getProfile().getId() == profile.getId()) {
                return "You cannot share your own post.";
            }

            Collection<Profile> deny = denyApproval(permissions);

            if (!deny.isEmpty()) {
                return "This post cannot be shared due to user preference.";
            }

            Collection<Profile> await = awaitingApproval(permissions);

            if (!await.isEmpty()) {
                result = "Post awaiting permission from owners.";
            }

            Collection<Profile> consents = getDefaultAllow(permissions);

            if(!consents.contains(profile) && post.getGroup().getMembers().contains(profile)) {
                consents.add(profile);
            }

            if(post != null && profile != null) {
                collaboration.setProfile(profile);
                collaboration.setPost(post);
                collaboration.setConsent(consents);
                collaboarationRepository.save(collaboration);
            }

        } catch (Exception e) {

        }
        return "";
    }

    // Create collaborations on post.
    public Collection<Collaboration> createCollaborations(Post post) {

        Collection<Collaboration> collaborations = post.getCollaborations();
        Collection<Profile> consents = new ArrayList<Profile>();

        consents.add(post.getProfile());


        for (Collaboration collaboration : collaborations) {

            Group collabsFriendsGroup = groupRepository.findByGroupNameAndProfileId("Friends", collaboration.getProfile().getId());

            if(collabsFriendsGroup != null) {
                collaboration.setGroup(collabsFriendsGroup);
                collaboration.setProfile(collaboration.getProfile());
                collaboration.setPost(post);

                collaboration.setConsent(consents);

            } else {
                System.out.println("cannot create collaboration since group is not found");
                return null;
            }
        }

        collaboarationRepository.saveAll(collaborations);

        return null;
    }

    // Accept a share request
    public String acceptCollaboration(long profileId, long id) {
        String result = "";
        Collaboration collaboration = null;
        try {
            collaboration = collaboarationRepository.findById(id).get();
            long groupId = collaboration.getPost().getGroup().getId();

            Collection<Profile> members = collaboration.getPost().getGroup().getMembers();
            Profile profile = members.stream().filter(x -> x.getId() == profileId).findFirst().get();//.filter(x -> x.getId() == profileId);

            Collection<Permission> permissions = (Collection<Permission>) permissionsRepository.findPermissionsOnGroups(groupId);
//            for (Permission permission:permissions) {
//                if(permission.getAllow() == 2 && permission.getRights().getCollaborate() == 0) {
//
//                }
//            }

            if(collaboration != null && profile != null) {
                if (members.contains(profile)) {
                    if(!collaboration.getConsent().contains(profile))
                        collaboration.getConsent().add(profile);
                    else
                        return "already consented";
                } else {
                    return "Not a group member";
                }

                if (members.size() == collaboration.getConsent().size())
                    collaboration.setApproved(true);
            }

            collaboarationRepository.save(collaboration);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }


    // Get Default allow settings
    public Collection<Profile> getDefaultAllow(@NotNull Collection<Permission> permissions) {
        Collection<Profile> consents = new ArrayList<>();
        long now = new Date().getTime();
        try {
            consents = permissions.stream()
                    .filter(x -> x.getAllow() == 1 && x.getRights().getCollaborate() == 1 || (x.getProfile().getLastSeen().getTime() - now) > 259200)
                    .map(a -> a.getProfile())
                    .collect(Collectors.toList());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return consents;
    }

    // awaiting approval.
    public Collection<Profile> awaitingApproval(@NotNull Collection<Permission> permissions) {
        Collection<Profile> await = permissions.stream()
                .filter(x -> x.getAllow() == 2 && x.getRights().getCollaborate() == 0)
                .map(a -> a.getProfile())
                .collect(Collectors.toList());
        return  await;
    }

    // denied requests
    public Collection<Profile> denyApproval(@NotNull Collection<Permission> permissions) {
        Collection<Profile> deny = permissions.stream()
                .filter(x -> x.getAllow() == 0 && x.getRights().getCollaborate() == 0)
                .map(x -> x.getProfile())
                .collect(Collectors.toList());
        return deny;
    }

}


//    Collection<Profile> deny = collaboration.getPost().getGroup().getMembers().stream()
//            .filter(x -> x.getPermission().getAllow() == 0).collect(Collectors.toList());
//
//            if(!deny.isEmpty()) {
//                    return "Collaboration not allowed for this post as it conflicts with user preferences.";
//                    }
//
//                    Collection<Profile> allow = collaboration.getPost().getGroup().getMembers().stream()
//        .filter(x -> x.getPermission().getAllow() == 1).collect(Collectors.toList());
//
//        if(!allow.isEmpty()) {
//        collaboration.setConsent(allow);
//        }
//
//        Collection<Profile> await = collaboration.getPost().getGroup().getMembers().stream()
//        .filter(x -> x.getPermission().getAllow() == 2 && x.getPermission().getRights().getCollaborate() == 0).collect(Collectors.toList());
//
//        if(!await.isEmpty()) {
//        collaboration.setConsent(allow);
//        result = "Awaiting approval.";
//        }
