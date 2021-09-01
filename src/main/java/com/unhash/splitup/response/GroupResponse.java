package com.unhash.splitup.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class GroupResponse {
    private Integer id;
    private String groupName;
    private String description;
    private Set<TransactionResponse> transactions;
    private Set<UserResponse> member;
}
