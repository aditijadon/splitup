package com.unhash.splitup.response;

import com.unhash.splitup.entity.TransactionType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserContributionResponse {
    private Integer groupId;
    private Integer userId;
    private TransactionType transactionType;
    private Double amount;
}
