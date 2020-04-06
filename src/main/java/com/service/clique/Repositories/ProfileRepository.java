package com.service.clique.Repositories;

import com.service.clique.Model.Profile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ProfileRepository extends PagingAndSortingRepository<Profile, Long> {
    @Query("select p from Profile p where p.email = ?1")
    Profile findByEmail(String email);
    @Query("select p from Profile p where p.email = ?1 and p.password = ?2")
    Profile findByEmailAndPassword(String email, String password);
    Profile findByUsername(String username);
}
