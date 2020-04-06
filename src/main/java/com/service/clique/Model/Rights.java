package com.service.clique.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Rights {
    @Id
    @GeneratedValue
    private long id;
    private byte share;
    private byte tag;
    private byte collaborate;
    private byte hideProfile;
    private byte hideEmail;
    private byte hideLastSeen;
    private byte hideDOB;
    @JsonIgnore
    @OneToOne(mappedBy = "rights")
    Permission permission;
}
