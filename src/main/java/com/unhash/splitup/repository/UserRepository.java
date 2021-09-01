package com.unhash.splitup.repository;

import com.unhash.splitup.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    //QUERY TO GET USER BY EMAIL-ID

    @Query("SELECT u FROM User u WHERE u.email = :email")
    public Optional<User> getUserByEmail(@Param("email") String email);

    //QUERY TO GET USER BY USERNAME AND PASSWORD

    @Query("SELECT u FROM User u WHERE u.userName = :userName AND u.password = :password")
    public Optional<User> getUserByUsernamePassword(@Param("userName") String userName, @Param("password") String password);

    //QUERY TO FIND USER BY GROUP-ID

    @Query("SELECT u FROM User u JOIN u.relatedGroup g WHERE g.id = :groupId")
    public Set<User> findUsersByGroupId(@Param("groupId") Integer groupId);
}
