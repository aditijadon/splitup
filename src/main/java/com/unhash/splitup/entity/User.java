package com.unhash.splitup.entity;

import com.unhash.splitup.util.PasswordEncryptor;
import lombok.*;
import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String userName;

    private String fullName;

    private String phone;

    private String email;

    //PASSWORD ENCRYPTOR
    @Convert(converter = PasswordEncryptor.class)
    private String password;

    @Enumerated(EnumType.STRING)
    private CredentialProvider credentialProvider;

    //MANY-TO-MANY MAPPING BETWEEN USERS AND RELATED GROUPS
    @ManyToMany(fetch = FetchType.EAGER, cascade= CascadeType.ALL, mappedBy="member")
    private Set<Group> relatedGroup;

    //ONE-TO-MANY MAPPING BETWEEN USER AND SPLIT-TRANSACTIONS
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "transaction")
    private Set<SplitTransaction> splitTransaction;
}
