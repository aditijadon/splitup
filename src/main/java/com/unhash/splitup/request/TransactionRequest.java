package com.unhash.splitup.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
public class TransactionRequest {
    private Integer id;
    private Integer groupId;
    private String billFor;
    private Double amount;
    private Date paidOn;
    private Map<String,Double> paidBy;
    private Set<UserRequest> sharedBy;
}
