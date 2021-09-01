package com.unhash.splitup.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
    private Integer id;
    private String userName;
    private String fullName;
    private String phone;
    private String email;
    private String password;
}
