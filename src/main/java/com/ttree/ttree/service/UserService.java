package com.ttree.ttree.service;

import com.ttree.ttree.domain.repository.UserRepository;
import com.ttree.ttree.dto.UserDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Transactional
    public Long saveUser(UserDto userDto){ return userRepository.save(userDto.toEntity()).getId(); }

}
