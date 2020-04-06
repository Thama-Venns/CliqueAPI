package com.service.clique.Services;

import com.service.clique.Model.Group;
import com.service.clique.Model.Profile;
import com.service.clique.Repositories.GroupRepository;
import com.service.clique.Repositories.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.*;

@Service
public class GroupsService {

    @Autowired GroupRepository groupRepository;
    @Autowired ProfileRepository profileRepository;

    public Collection<Group> getAllPublicGroups() {
        Collection<Group> groups = null;
        try {
            groups = (Collection<Group>) groupRepository.findPublicGroups();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return groups;
    }

    // Gets all groups where user is a member.
    public Iterable<Group> getUserGroups(long profileId) {
        Iterable<Group> groups = null;
        try {
            groups = groupRepository.findByGroupNameAndProfileId(profileId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return groups;
    }

    // Get group by id.
    public Group getGroupByName(String groupName) {
        Group group = null;
        try {
            group = groupRepository.findByGroupName(groupName);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return group;
    }

    // Get group by id.
    public Group getGroupById(long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceAccessException("Group not found"));
    }

//    public Iterable<Group> getUserGroups(long profileId) {
//        return groupRepository.findGroupsByProfileId(profileId);
//    }

    public String addGroup(Group group) {
        String result = "";
        long profileId = group.getProfile().getId();

        try {
            // Checks.
            Profile user = profileRepository.findById(profileId).get();
            Group exists = groupRepository.findByGroupName(group.getGroupName());

            if(exists != null) {
                return "Group already exits.";
            }

            group.getMembers().add(user);
            groupRepository.save(group);
            result = "Group added!";

        } catch (Exception e) {
            System.err.println(e.getMessage());
            return "Something went wrong!";
        }

        return result;
    }

    public Group addMembers(long profileId, long groupId) {
        Group member = null;
        try {
            Group group = groupRepository.findById(groupId).get();
            Profile profile = profileRepository.findById(profileId).get();

            if(group != null && profile != null)
                group.getMembers().add(profile);
                member = groupRepository.save(group);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return member;
    }
//
//
//    public boolean removeGroup(long groupId) {
//        boolean deleted = false;
//        try {
//            groupRepository.deleteById(groupId);
//
//            if(groupRepository.findById(groupId) == null)
//                deleted = true;
//
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//        }
//        return deleted;
//    }
}
