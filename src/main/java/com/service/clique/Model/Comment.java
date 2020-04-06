package com.service.clique.Model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Comment {
    @Id
    @GeneratedValue
    private long id;
    private String comment;
    private long likes;
    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Date created = new Date();
    @OneToOne
    private Profile profile;
    @ManyToOne
    private Post post;
}
