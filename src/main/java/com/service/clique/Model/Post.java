package com.service.clique.Model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Data
@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(length = 1000)
    private String postText;
    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Date creation = new Date();
    private long likes;
    @OneToOne
    private Profile profile;
    @ManyToOne
    private Group group;
    @OneToMany(mappedBy = "post")
    private Collection<Image> images;
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private Collection<Collaboration> collaborations;
    @OneToMany(mappedBy = "post")
    private Collection<Comment> comments;
    @OneToMany(mappedBy = "post")
    private Collection<Share> shares;
}
