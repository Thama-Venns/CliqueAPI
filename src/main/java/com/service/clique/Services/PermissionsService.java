package com.service.clique.Services;

import com.service.clique.Model.*;
import com.service.clique.Repositories.PermissionsRepository;
import com.service.clique.Repositories.PostRepository;
import com.service.clique.Repositories.RightsRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class PermissionsService {
    @Autowired PostRepository postRepository;
    @Autowired PermissionsRepository permissionsRepository;
    @Autowired RightsRepository rightsRepository;

    public Iterable<Permission> getPermissions() {
        Iterable<Permission> permissions = null;
        try {
            permissions = permissionsRepository.findAll();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return permissions;
    }

    public Permission getUserPermissions(long profileId) {
        Permission permission = null;
        try {
            permission = permissionsRepository.findByProfileId(profileId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return permission;
    }

    public Permission defaultPermission(Profile profile) {
        Permission newPermission = null;
        byte b = 1;
        byte b2 = 0;

        Rights rights = new Rights(0, b, b, b, b, b2, b2, b2, null);
        Permission permission = new Permission(0, 2, profile, rights);

        try {
            newPermission = permissionsRepository.save(permission);

//            if(newPermission.getId() > 0)
//                rights = new Rights(0, b, b, b, b, null);
//                rightsRepository.save(rights);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return newPermission;
    }

    public Permission updatePermission(Permission permission) {
        Permission newPermission = null;
        try {
            Permission permission1 = permissionsRepository.findById(permission.getId()).get();
            if (permission1 != null)
                newPermission = permissionsRepository.save(permission);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return newPermission;
    }

    public Iterable<Permission>  getGroupPermissions(long groupId) {
        Iterable<Permission> permissions = null;
        try {
            permissions = permissionsRepository.findPermissionsOnGroups(groupId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return permissions;
    }

    // Get Default allow settings
    public Collection<Profile> getDefaultAllow(@NotNull Collection<Permission> permissions) {
        Collection<Profile> consents = new ArrayList<>();
        long now = new Date().getTime();
        try {
            consents = permissions.stream()
                       .filter(x -> x.getAllow() == 1 && x.getRights().getShare() == 1 || (x.getProfile().getLastSeen().getTime() - now) > 259200)
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
                .filter(x -> x.getAllow() == 2 && x.getRights().getShare() == 0)
                .map(a -> a.getProfile())
                .collect(Collectors.toList());
        return  await;
    }

    // denied requests
    public Collection<Profile> denyApproval(@NotNull Collection<Permission> permissions) {
        Collection<Profile> deny = permissions.stream()
                .filter(x -> x.getAllow() == 0 && x.getRights().getShare() == 0)
                .map(x -> x.getProfile())
                .collect(Collectors.toList());
        return deny;
    }
}
