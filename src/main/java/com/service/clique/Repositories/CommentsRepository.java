package com.service.clique.Repositories;

import com.service.clique.Model.Comment;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CommentsRepository extends PagingAndSortingRepository<Comment, Long> {
}
