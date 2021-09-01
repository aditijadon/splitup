package com.unhash.splitup.repository;

import com.unhash.splitup.entity.Group;
import com.unhash.splitup.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    //QUERY TO GET GROUP BY TRANSACTION-ID

    @Query("SELECT t.group FROM Transaction t JOIN t.group WHERE t.id = :transactionId")
    Group getGroupsByTransactionId(@Param("transactionId") Integer transactionId);
}
