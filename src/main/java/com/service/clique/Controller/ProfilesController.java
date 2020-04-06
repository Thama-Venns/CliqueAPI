package com.service.clique.Controller;

import com.service.clique.Model.Profile;
import com.service.clique.Repositories.ProfileRepository;
import com.service.clique.Services.ProfilesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/profiles")
@Api("Handles user profiles")
public class ProfilesController {

    @Autowired
    ProfilesService profilesService;
    //@Autowired
    //GroupsService groupsService;

    @GetMapping
    @ApiOperation("Gets all user profiles")
    public Iterable<Profile> getAllProfiles() {
        return profilesService.getAllProfiles();
    }

    @GetMapping(value = "{uid}")
    @ApiOperation("Gets a single selected user")
    public Profile getProfile(@PathVariable long uid) throws ResourceAccessException {
        return profilesService.getProfileById(uid);
    }

    @PostMapping("/register")
    @ApiOperation("Creates a new user profile")
    public ResponseEntity createProfile(@RequestBody Profile profile) {

        if (profile == null)
            return ResponseEntity.badRequest().body("Model State error");

        Profile userProfile = null;
        try {
            userProfile = profilesService.getProfileByEmail(profile.getEmail());
            if (userProfile != null)
                return ResponseEntity.badRequest().body("User already Exists");

            userProfile = profilesService.createProfile(profile);

            if (userProfile != null) {
                    //groupsService.AddDefaultGroups(profile);
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed");
        }
        return ResponseEntity.status(HttpStatus.OK).body(userProfile);
    }


    @GetMapping("user")
    @ApiOperation("Gets a user profile by username.")
    public ResponseEntity<?> getProfileByUsername(@RequestParam String username) {
        Profile profile = profilesService.getByUsername(username);

        if (profile == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profile not found");

        return ResponseEntity.ok(profile);
    }

    @PatchMapping("/{uid}")
    @ApiOperation("Updates User details")
    public ResponseEntity updateUser(@PathVariable long uid, @RequestBody Profile profile) {
        Profile user = null;
        if(profile == null)
            return ResponseEntity.badRequest().body("Invalid Request");

        if(profile.getEmail() != null || profile.getPassword() != null) {
            return ResponseEntity.badRequest().body("User E-mail and password cannot be updated here");
        }

        try {
            user = profilesService.getProfileById(uid);
            if(user != null)
                return ResponseEntity.badRequest().body("User already exist");

            if(user == null)
                profilesService.updateUser(uid, profile);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return ResponseEntity.ok().body("Updated");
    }

    @PostMapping("/login")
    @ApiOperation("Authenticates a user's identity")
    public ResponseEntity login(@RequestParam String email, @RequestParam String password) {

        Profile user = null;
        try {
            user = profilesService.login(email, password);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        if (user != null)
            return ResponseEntity.ok().body(user);
        else
            return ResponseEntity.notFound().build();
    }
}
