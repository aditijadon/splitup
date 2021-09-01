package com.unhash.splitup.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private Integer id;
    private String userName;
    private String fullName;
    private String phone;
    private String email;
}
