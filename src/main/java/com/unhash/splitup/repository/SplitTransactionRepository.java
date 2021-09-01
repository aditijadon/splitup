package com.unhash.splitup.repository;

import com.unhash.splitup.entity.SplitTransaction;
import com.unhash.splitup.entity.SplitTransactionPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface SplitTransactionRepository extends JpaRepository<SplitTransaction, SplitTransactionPK> {

    //QUERY TO FIND SPLIT-TRANSACTION BY TRANSACTION-ID

    @Query("SELECT st FROM SplitTransaction st WHERE st.transaction IN (SELECT t FROM Transaction t WHERE t.id = :transactionId)")
    Set<SplitTransaction> getSplitTransactionByTransactionId(@Param("transactionId") Integer transactionId);

    //QUERY TO GET PAID-BY USERS BY TRANSACTION-ID

    @Query("SELECT st FROM SplitTransaction st WHERE st.transaction IN (SELECT t FROM Transaction t WHERE t.id = :transactionId) AND st.id.transactionType = 1")
    Set<SplitTransaction> getPaidByTransactionId(@Param("transactionId") Integer transactionId);

    //QUERY TO GET SHARED-BY USERS BY TRANSACTION-ID

    @Query("SELECT st FROM SplitTransaction st WHERE st.transaction IN (SELECT t FROM Transaction t WHERE t.id = :transactionId) AND st.id.transactionType = 0")
    Set<SplitTransaction> getSharedByTransactionId(@Param("transactionId") Integer transactionId);

    //QUERY TO GET GROUPWISE CONTRIBUTION OF USERS

    @Query("SELECT st FROM SplitTransaction st WHERE st.id.userId = :userId")
    Set<SplitTransaction> getGroupwiseContributionByUser(@Param("userId") Integer userId);

}