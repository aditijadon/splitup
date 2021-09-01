package com.unhash.splitup.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "groups")
@ToString
public class Group {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String groupName;

    private String description;

    //MANY-TO-MANY MAPPING BETWEEN USERS AND GROUPS

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name="groupedUsers", joinColumns={@JoinColumn(referencedColumnName="id")}
            , inverseJoinColumns={@JoinColumn(referencedColumnName="id")})
    private Set<User> member;

    //ONE-TO-MANY MAPPING BETWEEN TRANSACTION AND GROUP

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "group")
    private Set<Transaction> transaction;
}
