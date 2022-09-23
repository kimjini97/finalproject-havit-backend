package com.havit.finalbe.service;

import com.havit.finalbe.entity.Groups;
import com.havit.finalbe.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GroupService {

    private final GroupRepository groupRepository;


    @Transactional(readOnly = true)
    public Groups isPresentGroup(Long groupId) {
        Optional<Groups> groupOptional = groupRepository.findById(groupId);
        return groupOptional.orElse(null);
    }
}
