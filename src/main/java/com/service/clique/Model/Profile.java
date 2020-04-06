package com.service.clique.Model;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String Gender;
    @JsonFormat(pattern="yyyy-mm-dd")
    @Column(nullable = false)
    private Date dateOfBirth;
    @Column(nullable = false, columnDefinition = "BOOLEAN default true")
    private boolean hide;
    @Column(nullable = true)
    private String avatar;
    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Date joined;
    private Date lastSeen;
    //@JsonIgnore
    @Column(nullable = false)
    private String password;

    @JsonIgnore
    @OneToOne
    private Permission permission;
    @JsonIgnore
    @ManyToMany(mappedBy = "members")
    private Collection<Group> groups;
    @JsonIgnore
    @ManyToMany(mappedBy = "consent")
    private Collection<Share> shares;
    @JsonIgnore
    @ManyToMany(mappedBy = "consent")
    private Collection<Collaboration> collaborations;
}
