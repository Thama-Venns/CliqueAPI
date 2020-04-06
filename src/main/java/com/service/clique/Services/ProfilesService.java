package com.service.clique.Services;

import com.service.clique.Model.Group;
import com.service.clique.Model.Profile;
import com.service.clique.Repositories.GroupRepository;
import com.service.clique.Repositories.ProfileRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;

@Service
public class ProfilesService {

    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired PermissionsService permissionsService;

    //Gets all user profiles
    public Iterable<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    //Gets a single selected user by id
    public Profile getProfileById(long uid) throws ResourceAccessException {
        return profileRepository.findById(uid)
                .orElseThrow(() -> new ResourceAccessException("Profile not found on :: " + uid));
    }

    //Get profile by username
    public Profile getByUsername(String username) {
        Profile profile = null;
        try {
            profile = profileRepository.findByUsername(username);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return profile;
    }

    //Gets a single selected user by email
    public Profile getProfileByEmail(String email) throws ResourceAccessException {
        return profileRepository.findByEmail(email);
                //.orElseThrow(() -> new ResourceAccessException("Profile not found on :: " + email));
    }

    //Creates a new user profile
    public Profile createProfile(Profile profile) {
        Profile userProfile = null;
        try {
            userProfile = profileRepository.save(profile);

            Group friends = new Group();
            friends.setGroupName("Friends");
            friends.setProfile(userProfile);
            friends.getMembers().add(userProfile);

            groupRepository.save(friends);

            permissionsService.defaultPermission(userProfile);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return userProfile;
    }

    //Updates User details
    public Profile updateUser(long uid, Profile profile) {
        Profile user = null;

        try {
            user = profileRepository.findById(uid)
                    .orElseThrow(() -> new ResourceAccessException("User not found"));

            profileRepository.save(profile);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return user;
    }

    //Authenticates a user's identity
    public Profile login(String email, String password) {

        Profile user = null;
        try {
            user = profileRepository.findByEmailAndPassword(email, password);

            // update last seen.
            user.setLastSeen(new Date());
            profileRepository.save(user);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return user;
    }
}
