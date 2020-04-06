package com.service.clique.Repositories;

import com.service.clique.Model.Collaboration;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CollaboarationRepository extends PagingAndSortingRepository<Collaboration, Long> {
    @Query("Select c From Collaboration c Where c.profile.id =  ?1 AND c.post.id = ?2")
    Collaboration findByProfileIdAndProfileId(Long profileId, Long postId);
}
