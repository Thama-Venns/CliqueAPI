package com.service.clique.Repositories;

import com.service.clique.Model.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Collection;

@RepositoryRestResource
public interface PostRepository extends PagingAndSortingRepository<Post, Long> {
//    @Query("SELECT p FROM Post p WHERE p.profile.id = ?1")
//    Collection<Post> findAllByUserId(Long id);
//
    @Query("SELECT DISTINCT p FROM Post p " +
            "INNER JOIN Group g ON p.group.id = g.id " +
            "INNER JOIN g.members gm " +
            "LEFT OUTER JOIN Collaboration c ON p.id = c.post.id " +
            "LEFT OUTER JOIN p.collaborations cl " +
            "LEFT OUTER JOIN cl.group cg ON cl.group.id = cg.id " +
            "LEFT OUTER JOIN cg.members cm " +
            "LEFT OUTER JOIN Share s ON p.id = s.post.id " +
            "LEFT OUTER JOIN p.shares sh " +
            "LEFT OUTER JOIN sh.group sg ON sh.group.id = sg.id " +
            "LEFT OUTER JOIN sg.members sm " +
            "WHERE (gm.id = ?1 OR c.profile.id = ?1) OR (cm.id = ?1 AND c.approved = true) OR (sm.id = ?1 AND s.accepted = true) " + // AND cm.id != gm.id) " +
                    "AND g.isPrivate = false " +
            "ORDER BY p.creation DESC")
    Collection<Post> findAllByGroupMemberId(Long id);

    @Query("SELECT p FROM Post p " +
            //"INNER JOIN Group g " +
            "WHERE p.group.id = ?1")
    Collection<Post> findByGroupId(Long gid);
    Collection<Post> findByProfileUsername(String username);


//    @Query("SELECT DISTINCT p FROM Post p " +
//            "INNER JOIN Group g ON p.group.id = g.id " +
//            "INNER JOIN g.members gm " +
//            "LEFT OUTER JOIN Collaboration c ON p.id = c.post.id " +
//            "LEFT OUTER JOIN p.collaborations cl " +
//            "LEFT OUTER JOIN cl.group cg ON cl.group.id = cg.id " +
//            "LEFT OUTER JOIN cg.members cm " +
//            "LEFT OUTER JOIN Share s ON p.id = s.post.id " +
//            "LEFT OUTER JOIN p.shares sh " +
//            "LEFT OUTER JOIN sh.group sg ON sh.group.id = sg.id " +
//            "LEFT OUTER JOIN sg.members sm " +
//            "WHERE (gm.id = ?1 OR c.profile.id = ?1 ) OR (cm.id = ?1 AND c.approved = true) OR (sm.id = ?1 AND s.accepted = true) " + // AND cm.id != gm.id) " +
//            "AND  p.postText Like ?2")

    @Query("SELECT DISTINCT p FROM Post p " +
           "WHERE p.postText Like ?1")
    Collection<Post> findMatch(String text);

    @Query("SELECT DISTINCT p FROM Post p " +
            "JOIN Profile pr " +
            "WHERE (p.postText Like '%pr.firstName%' AND p.postText Like '%pr.lastName%') OR p" +
            ".postText Like '%pr.email%'")
    Collection<Post> findDetailsMatch();
}
