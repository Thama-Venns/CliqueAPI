package com.service.clique.Repositories;

import com.service.clique.Model.Rights;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface RightsRepository extends PagingAndSortingRepository<Rights, Long> {
}
