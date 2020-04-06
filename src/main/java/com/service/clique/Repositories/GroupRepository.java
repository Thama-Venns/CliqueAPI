package com.service.clique.Repositories;

import com.service.clique.Model.Group;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@RepositoryRestResource
public interface GroupRepository extends PagingAndSortingRepository<Group, Long> {

    Group findByGroupName(String name);
//    @Query("Select Distinct g From Group g " +
//            //"Inner Join GroupMember gm On g.id = gm.group.id " +
//            "Where g.profile.id = ?1")
//    Collection<Group> findGroupsByProfileId(Long id);
    //@Query("Select c From Connection c Where user_1_id = ?1 OR user_2_id = ?1 AND accepted = true")
    @Query("Select Distinct g From Group g " +
            "Join g.members m " +
            "Where m.id = ?1")
    Set<Group> findByGroupNameAndProfileId(Long profileId);

    @Query("SELECT DISTINCT g FROM Group g WHERE g.isPrivate = false AND g.groupName NOT IN('General', 'Friends')")
    Collection<Group> findPublicGroups();

    //@Query("SELECT g FROM Group g WHERE g.groupName = 1? AND g.profile.id = 2?")
    Group findByGroupNameAndProfileId(String GroupName, Long profileId);
}
