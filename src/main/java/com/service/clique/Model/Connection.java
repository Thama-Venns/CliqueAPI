package com.service.clique.Model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Data
@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Connection {
    @Id
    @GeneratedValue
    private long id;
    private boolean accepted;
    @ManyToOne
    private Profile requester;
    @ManyToOne
    private Profile receiver;
}
