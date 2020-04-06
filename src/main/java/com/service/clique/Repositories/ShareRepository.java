package com.service.clique.Repositories;

import com.service.clique.Model.Share;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ShareRepository extends PagingAndSortingRepository<Share, Long> {
    Iterable<Share> findByPostProfileId(long profileId);
    Share findByPostIdAndProfileId(long postId, long profileId);
}
