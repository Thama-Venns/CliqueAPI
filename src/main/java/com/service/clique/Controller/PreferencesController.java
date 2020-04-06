package com.service.clique.Controller;

import com.service.clique.Model.Permission;
import com.service.clique.Services.PermissionsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("preferences")
@ApiOperation("Provides operations to manage preferences.")
public class PreferencesController {

    @Autowired
    PermissionsService permissionsService;

    @GetMapping
    @ApiOperation("Gets all permissions")
    public ResponseEntity<?> getAllPermissions() {
        Iterable<Permission> permissions = permissionsService.getPermissions();
        return ResponseEntity.ok().body(permissions);
    }

    @GetMapping("/{profileId}")
    @ApiOperation("Gets user permissions")
    public ResponseEntity<?> getUserPermissions(@PathVariable long profileId) {
        Permission permissions = null;
        try {
            permissions = permissionsService.getUserPermissions(profileId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong!");
        }

        return ResponseEntity.ok().body(permissions);
    }

    @PatchMapping
    public ResponseEntity<?> UpdatePreferences(@RequestBody Permission permission) {
        if(permission == null)
            return ResponseEntity.badRequest().body("Bad");

        try {
            Permission newPermission = permissionsService.updatePermission(permission);

            if (newPermission == null)
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Failure");

        } catch (Exception e) {
            System.err.println(e.getMessage());
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok("Ok");
    }

    @GetMapping("/groups")
    @ApiOperation("Retrieves a group members' permissions. ")
    public Iterable<Permission> GetGroupPermissions(long groupId) {
        Iterable<Permission> permissions = null;
        try {
            permissions = permissionsService.getGroupPermissions(groupId);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return  permissions;
    }

//    @PostMapping
//    public ResponseEntity<?> SetPreferences(@RequestParam long profileId) {
//
//        try {
//
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//        return ResponseEntity.ok("Ok");
//    }

}
