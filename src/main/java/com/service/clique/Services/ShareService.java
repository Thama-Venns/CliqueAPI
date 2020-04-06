package com.service.clique.Services;

import com.service.clique.Model.*;
import com.service.clique.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class ShareService {
    @Autowired ShareRepository shareRepository;
    @Autowired PostRepository postRepository;
    @Autowired ProfileRepository profileRepository;
    @Autowired PermissionsRepository permissionsRepository;
    @Autowired GroupRepository groupRepository;
    @Autowired PermissionsService permissionsService;

    public Iterable<Share> getAllShares() {
        Iterable<Share> shares = null;
        try {
            shares = shareRepository.findAll();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return shares;
    }

    public Iterable<Share> GetUserShares(long profileId) {
        Iterable<Share> shares = null;
        try {
            shares = shareRepository.findByPostProfileId(profileId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return shares;
    }

    public Share findShare(long postId, long profileId) {
        Share share = null;
        try {
            share = shareRepository.findByPostIdAndProfileId(postId, profileId);
        } catch (Exception e) {
            System.out.println("You've already shared this post");
        }
        return share;
    }

    // Share post
    public String sharePost(long profileId, long postId, long groupId) {
        String result = "";
        Share share = new Share();
        try {
            Post post = postRepository.findById(postId).get();
            Profile profile = profileRepository.findById(profileId).get();
            Group group = groupRepository.findById(groupId).get();
            long postGroupId = post.getGroup().getId();


            Collection<Permission> permissions = (Collection<Permission>) permissionsRepository.findPermissionsOnGroups(postGroupId);

            if(post.getProfile().getId() == profile.getId()) {
                return "You cannot share your own post.";
            }

            Collection<Profile> deny = permissionsService.denyApproval(permissions);

            if (!deny.isEmpty()) {
                return "This post cannot be shared due to user preference.";
            }

            Collection<Profile> await = permissionsService.awaitingApproval(permissions);

            if (!await.isEmpty()) {
                result = "Post awaiting permission from owners.";
            }

            Collection<Profile> consents = permissionsService.getDefaultAllow(permissions);

            if(!consents.contains(profile) && post.getGroup().getMembers().contains(profile)) {
                consents.add(profile);
            }

//            Collection<Profile> taged = post.getCollaborations();//.stream().filter(x -> x.getProfile().getId() == profileId);
//            // Send requests here

//            if(!consents.contains(profile) && post.getCollaborations().contains(profile))
//                consents.add(profile);
//
            if(!consents.isEmpty())
                share.setConsent(consents);

            if(post != null)
                share.setPost(post);
            share.setProfile(profile);

            share.setGroup(group);

            shareRepository.save(share);
            if(share.getId() > 0 && await.isEmpty())
                result = "Post shared";

        } catch (Exception e) {
            System.err.println(e.getMessage());
            return  "Internal Server error!";
        }
        return result;
    }

    // Accept a share request
    public Share acceptShare(long profileId, long id) {
        Share share = null;
        try {
            share = shareRepository.findById(id).get();

            Collection<Profile> members = share.getPost().getGroup().getMembers();
            Profile profile = members.stream().filter(x -> x.getId() == profileId).findFirst().get();//.filter(x -> x.getId() == profileId);

            if(share != null && profile != null) {
                if (members.contains(profile)) {
                    if(!share.getConsent().contains(profile))
                        share.getConsent().add(profile);
                    else
                        System.out.println("already consented");
                } else {
                    System.out.println("Not a group member");
                }

                if (members.size() == share.getConsent().size())
                    share.setAccepted(true);
            }

            shareRepository.save(share);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return share;
    }
}
