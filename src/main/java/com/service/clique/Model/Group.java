package com.service.clique.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import javax.persistence.*;
import java.util.*;

@Data
@Entity
@Table(name = "`group`")
public class Group {
    @Id
    @GeneratedValue
    private long id;
    private String groupName;
    private boolean isPrivate;
    @OneToOne
    Permission permission;
    @OneToOne
    private Profile profile;
    @JsonIgnore
    @OneToMany(mappedBy = "group")
    private Collection<Post> posts;
    @ManyToMany
    @JoinTable(
            name = "GroupProfiles",
            joinColumns = @JoinColumn(name = "groupId"),
            inverseJoinColumns = @JoinColumn(name = "profileId"))
    private Collection<Profile> members = new ArrayList<>();
}
