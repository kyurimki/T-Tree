package com.ttree.ttree.service;

import com.ttree.ttree.domain.entity.User;
import com.ttree.ttree.domain.repository.UserRepository;
import com.ttree.ttree.dto.AuthImageDto;
import com.ttree.ttree.dto.UserDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
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
        return userRepository.save(userDto.toEntity()).getUser_id();
    }

    @Transactional
    public UserDto getUser(Long id){
        User user = userRepository.findById(id).get();

        UserDto userDto = UserDto.builder()
                .user_id(user.getUser_id())
                .name(user.getName())
                .studentIdNum(user.getStudentIdNum())
                .password(user.getPassword())
                .email(user.getEmail())
                .role(user.getRole())
                .major1(user.getMajor1())
                .major2(user.getMajor2())
                .status(user.isStatus())
                .phoneNum(user.getPhoneNum())
                .teamIdNum(user.getTeamIdNum())
                .build();
        return userDto;

    }

    @Transactional
    public UserDto getUserByStudentId(String studentId) {
        List<User> userList = userRepository.findAll();
        for(User user : userList) {
            if(studentId.equals(user.getStudentIdNum())) {
                Long id = user.getUser_id();
                UserDto userDto = getUser(id);
                return userDto;
            }
        }
        return null;
    }

    @Transactional
    public boolean idCheck(String id){
        if(userRepository.findBystudentIdNum(id) != null){ // DB에 존재함
            return true;
        }
        return false;
    }

    @Transactional
    public List<UserDto> getNotApprovedUser() {
        List<User> userList = userRepository.findUsersByStatusIsFalse();
        int size = userList.size();
        List<UserDto> userNotApprovedList = new ArrayList<>(size);
        for(int i = 0; i < size; i++) {
            Long id = userList.get(i).getUser_id();
            UserDto userDto = getUser(id);
            userNotApprovedList.add(userDto);
        }
        return userNotApprovedList;
    }
}
