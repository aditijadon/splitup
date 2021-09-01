package com.unhash.splitup.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String billFor;

    private Double amount;

    private Date paidOn;

    //MANY-TO-ONE MAPPING BETWEEN TRANSACTIONS AND GROUP

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")
    private Group group;

    //ONE-TO-MANY MAPPING BETWEEN TRANSACTION AND SPLIT-TRANSACTIONS

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "user")
    private Set<SplitTransaction> splitTransaction;
}
