package com.service.clique.Model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Collaboration {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private boolean approved;
    @ManyToOne
    private Post post;
    @OneToOne
    private Profile profile;
    @OneToOne
    private Group group;
    @ManyToMany
    @JoinTable(
            name = "CollaborationProfiles",
            joinColumns = @JoinColumn(name = "collaborationId"),
            inverseJoinColumns = @JoinColumn(name = "profileId"))
    private Collection<Profile> consent = new ArrayList<>();
}
