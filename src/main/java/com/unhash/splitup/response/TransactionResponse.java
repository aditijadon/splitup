package com.unhash.splitup.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
public class TransactionResponse {
    private Integer id;
    private String billFor;
    private Double amount;
    private Date paidOn;
    private Integer groupId;
    private Map<String,Double> paidBy;
    private Set<UserResponse> sharedBy;
}
