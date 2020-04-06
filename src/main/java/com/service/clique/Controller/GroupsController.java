package com.service.clique.Controller;

import com.service.clique.Model.Group;
import com.service.clique.Services.GroupsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping("/groups")
@Api("Handles User groups")
public class GroupsController {

    @Autowired GroupsService groupsService;

    @GetMapping
    @ApiOperation("Gets a list of Public Groups")
    public ResponseEntity<?> getPublicGroups() {
        Collection<Group> groups = null;
        try {
            groups = groupsService.getAllPublicGroups();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Groups could not be retrieved");
        }
        return ResponseEntity.ok(groups);
    }

    @GetMapping(value = "/profile/{profileId}")
    @ApiOperation("Gets a list of Profile Groups")
    public ResponseEntity getUserGroups(@PathVariable long profileId) {
        Iterable<Group> groups = null;
        try {
            groups = groupsService.getUserGroups(profileId);

            if(groups == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No groups where found");

        } catch (Exception e) {
            System.out.println(e.getMessage());
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong!");
        }

        return ResponseEntity.ok(groups);
    }

    @GetMapping("/byName")
    @ApiOperation("Gets a group by its name")
    public ResponseEntity<?> getGroupByName(@RequestParam String groupName) {
        Group group = groupsService.getGroupByName(groupName);

        if(group == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group Not found");

        return  ResponseEntity.ok(group);
    }
//
//    @GetMapping(value = "/profile/{profileId}")
//    @ApiOperation("Gets a list of Profile Groups")
//    public ResponseEntity<?> getUserGroups(@PathVariable long profileId) {
//        Iterable<Group> groups = null;
//        try {
//            groups = groupsService.getUserGroups(profileId);
//            if(groups == null)
//                ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Groups where found for this user");
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//        return ResponseEntity.ok(groups);
//    }
//
    @GetMapping(value = "/{groupId}")
    @ApiOperation("Gets group by id")
    public ResponseEntity<?> getGroupById(@PathVariable long groupId) {
        Group group = null;
        try {
            group = groupsService.getGroupById(groupId);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
        return ResponseEntity.ok(group);
    }

    @PostMapping
    @ApiOperation("Creates a new group Group")
    public ResponseEntity<?> createGroup(@RequestBody Group group) {
        String result = groupsService.addGroup(group);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/members/{profileId}/{groupId}")
    @ApiOperation("Adds new members to a group")
    public ResponseEntity<?> addGroupMember(@PathVariable long profileId, @PathVariable long groupId) {
        Group group = null;
        try {
            group = groupsService.addMembers(profileId, groupId);

            if (group == null)
                return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("member could not be created");

        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
        return ResponseEntity.created(URI.create("")).body(group);
    }

//
//    @PostMapping("/default/{profileId}")
//    @ApiOperation("Creates default groups")
//    public ResponseEntity<?> createDefaultGroups(@RequestBody Profile profile) {
//        try {
//            groupsService.AddDefaultGroups(profile);
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
//        }
//
//        return ResponseEntity.created(URI.create("groupLocation")).body("Groups created");
//    }
//
//    @PostMapping("addMembers/{groupId}")
//    @ApiOperation("Adds new members to a group")
//    public ResponseEntity<?> addGroupMembers(@PathVariable long groupId, @RequestBody List<GroupMember> groupMember) {
//        try {
//
//            for (GroupMember mbr :groupMember) {
//                Group group = new Group();
//                group.setId(groupId);
//                mbr.setGroup(group);
//            }
//            Iterable<GroupMember> newMembers = groupsService.addMembers(groupId, groupMember);
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
//        }
//        return ResponseEntity.created(URI.create("")).body(groupMember.size() + "Member(s) to");
//    }
//
//    @DeleteMapping("{groupId}")
//    @ApiOperation("Deletes an Existing group Groups")
//    public ResponseEntity<?> removeGroup(@PathVariable long groupId) {
//        if(groupsService.removeGroup(groupId)) {
//            return ResponseEntity.ok().body("Group Removed");
//        } else {
//            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Group could not be deleted");
//        }
//    }
//
}
