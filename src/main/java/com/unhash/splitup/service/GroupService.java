package com.unhash.splitup.service;

import com.unhash.splitup.entity.Group;
import com.unhash.splitup.entity.User;
import com.unhash.splitup.exception.InternalException;
import com.unhash.splitup.repository.GroupRepository;
import com.unhash.splitup.repository.UserRepository;
import com.unhash.splitup.request.GroupRequest;
import com.unhash.splitup.response.GroupResponse;
import com.unhash.splitup.util.ConvertorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Slf4j
@Service
public class GroupService {
    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;


    //SERVICE METHOD FOR CREATING A NEW GROUP
    public GroupResponse createGroup(GroupRequest groupRequest){
        log.info("create Group is entry!");
        try{
            log.info("create Group is starting!");
            Group group = ConvertorUtil.groupRequestToEntity(groupRequest);
            log.info("group details " + group);

            Set<User> users = new HashSet<>();
            group.getMember().forEach(member -> {
                log.info("inside for each" + member);
                Optional<User> userOptional = userRepository.findById(member.getId());
                if (!userOptional.isPresent()) {
                    throw new RuntimeException("User with id " + member.getId() + " is not present in system.");
                } else {
                    users.add(userOptional.get());
                }
            });

            group.setMember(users);
            group = groupRepository.save(group);
            log.info("create-group service is up!");
            return ConvertorUtil.groupEntityToResponse(group);

        } catch (Exception e){
            throw new InternalException(e.getMessage());
        }
    }


    //SERVICE METHOD FOR GETTING GROUPS BY USER-ID
    public Set<GroupResponse> getGroupsByUserId(int userId){
        try {
            Set<GroupResponse> groupsResponse = new HashSet<GroupResponse>();
            for (Group group : groupRepository.getGroupsByUserId(userId)) {
                groupsResponse.add(ConvertorUtil.groupEntityToResponse(group));
            }
            return groupsResponse;

        } catch (Exception e){
            throw new InternalException(e.getMessage());
        }
    }

    //SERVICE METHOD FOR GETTING GROUP BY GROUP-ID
    public GroupResponse getGroupByGroupId(int id){
        try {
            log.info("inside get-group-by-id!");
            Optional<Group> groupOptional = groupRepository.findById(id);
            if (groupOptional.isPresent()) {
                return ConvertorUtil.groupEntityToResponse(groupOptional.get());
            } else {
                throw new InternalException("Group with id " + id + " is not present in system.");
            }
        } catch (Exception e) {
            throw new InternalException(e.getMessage());
        }
    }

}
