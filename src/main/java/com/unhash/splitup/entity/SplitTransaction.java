package com.unhash.splitup.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "split_transactions")
@ToString
public class SplitTransaction {
    @EmbeddedId
    private SplitTransactionPK id;

    //MANY-TO-ONE MAPPING BETWEEN TRANSACTION AND SPLIT-TRANSACTION

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @MapsId("transactionId")                    //This is the name of attribute in SplitTransactionPK class
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    //MANY-TO-ONE MAPPING BETWEEN USER AND SPLIT-TRANSACTION

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @MapsId("userId")                           //This is the name of attribute in SplitTransactionPK class
    @JoinColumn(name = "user_id")
    private User user;

    private Double money;
}

