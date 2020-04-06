package com.service.clique.Repositories;

import com.service.clique.Model.Connection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Collection;

@RepositoryRestResource
public interface ConnectionsRepository extends PagingAndSortingRepository<Connection, Long> {
    @Query("Select c From Connection c Where requester_id = ?1 OR receiver = ?1")
    Collection<Connection> findByUserId(Long id);

    @Query("Select c From Connection c Where (requester_id = ?1 OR receiver = ?1) AND accepted = false")
    Collection<Connection> findPendingByUserId(Long id);

    //For connections suggestions
    @Query("SELECT n FROM Connection n WHERE (requester <> ?1 AND receiver <> ?2) OR (receiver <> ?1 AND requester_id <> ?2)")
    Collection findNonConnections(Long uid, Long cid);
}
