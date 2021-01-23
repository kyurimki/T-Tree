package com.ttree.ttree.dto;

import com.ttree.ttree.domain.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserDto {
    private Long user_id;
    private String name;
    private String studentIdNum;
    private String email;
    private String password;
    private String role;
    private String major1;
    private String major2;
    private boolean status;
    private String phoneNum;
    private Long teamIdNum;

    private Long fileId;

    public User toEntity(){
        User build = User.builder()
                .user_id(user_id)
                .name(name)
                .studentIdNum(studentIdNum)
                .email(email)
                .password(password)
                .role(role)
                .major1(major1)
                .major2(major2)
                .status(status)
                .phoneNum(phoneNum)
                .teamIdNum(teamIdNum)
                .build();
        return build;
    }

    @Builder
    public UserDto(Long user_id, String name, String studentIdNum, String email, String password, String role, String major1, String major2, Long fileId, boolean status, String phoneNum, Long teamIdNum){
        this.user_id = user_id;
        this.name = name;
        this.studentIdNum = studentIdNum;
        this.email = email;
        this.password = password;
        this.role = role;
        this.major1 = major1;
        this.major2 = major2;
        this.fileId = fileId;
        this.status = status;
        this.phoneNum = phoneNum;
        this.teamIdNum = teamIdNum;
    }
}
