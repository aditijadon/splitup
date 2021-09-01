package com.unhash.splitup.request;


import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class GroupRequest {
    private Integer id;
    private String groupName;
    private String description;
    private Set<UserRequest> member;
}
