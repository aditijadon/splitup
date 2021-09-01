package com.unhash.splitup.repository;

import com.unhash.splitup.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {

    //QUERY TO GET GROUPS BELONGING TO A USER

    @Query("SELECT g FROM Group g JOIN g.member m WHERE m.id = :userId")
    Set<Group> getGroupsByUserId(@Param("userId") Integer userId);
}
