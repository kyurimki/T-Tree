package com.ttree.ttree.service;

import com.ttree.ttree.domain.entity.User;
import com.ttree.ttree.domain.repository.UserRepository;
import com.ttree.ttree.dto.UserDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Transactional
    public Long saveUser(UserDto userDto){
        idCheck(userDto.getStudentIdNum());
        return userRepository.save(userDto.toEntity()).getId();
    }

    @Transactional
    public UserDto getUser(Long id){
        User user = userRepository.findById(id).get();

        UserDto userDto = UserDto.builder()
                .id(id)
                .name(user.getName())
                .studentIdNum(user.getStudentIdNum())
                .email(user.getEmail())
                .role(user.getRole())
                .major1(user.getMajor1())
                .major2(user.getMajor2())
                .build();
        return userDto;

    }

    @Transactional
    public UserDto getUserByStudentId(String studentId) {
        List<User> userList = userRepository.findAll();
        for(User user : userList) {
            if(studentId.equals(user.getStudentIdNum())) {
                Long id = user.getId();
                UserDto userDto = getUser(id);
                return userDto;
            }
        }
        return null;
    }

    public void idCheck(String id){
        if(userRepository.findBystudentIdNum(id) != null){
            throw new IllegalStateException("이미 존재하는 회원입니다");
        }
    }

}
