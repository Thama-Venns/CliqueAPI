package com.service.clique.Model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Permission {
    @Id
    @GeneratedValue
    private long id;
    private int allow;
    @OneToOne
    private Profile profile;
    @OneToOne(cascade = CascadeType.ALL)
    private Rights rights;
}

