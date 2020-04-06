package com.service.clique.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Data
@Entity
public class Share {
    @Id
    @GeneratedValue
    private long id;
    private boolean accepted;
    @OneToOne
    private Profile profile;
    @ManyToOne
    private Post post;
    @OneToOne
    private Group group;
    @ManyToMany
    @JoinTable(
            name = "ShareProfiles",
            joinColumns = @JoinColumn(name = "shareId"),
            inverseJoinColumns = @JoinColumn(name = "profileId"))
    private  Collection<Profile> consent = new ArrayList<>();
}
