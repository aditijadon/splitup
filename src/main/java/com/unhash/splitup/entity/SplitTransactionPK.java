package com.unhash.splitup.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Enumerated;
import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class SplitTransactionPK implements Serializable {

    //COMPOSITE KEY DEFINITION FOR SPLIT-TRANSACTION

    @Column(name = "transaction_id")
    private Integer transactionId;

    @Column(name = "user_id")
    private Integer userId;

    //EXTRA COLUMN ADDED IN MANY-TO-MANY MAPPING TABLE

    @Enumerated
    @Column(name = "transaction_type")
    private TransactionType transactionType;
}
