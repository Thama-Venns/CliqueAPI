package com.service.clique.Repositories;

import com.service.clique.Model.Permission;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface PermissionsRepository extends PagingAndSortingRepository<Permission, Long> {
    Permission findByProfileId(Long profileId);
    @Query("SELECT DISTINCT p FROM Permission p " +
            "JOIN Profile pr ON p.profile.id = pr.id " +
            "JOIN pr.groups pg " +
            "WHERE pg.id = :groupId")
    Iterable<Permission> findPermissionsOnGroups(@Param("groupId") Long groupId);
}