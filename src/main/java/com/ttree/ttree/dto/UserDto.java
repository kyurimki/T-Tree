package com.ttree.ttree.dto;

import com.ttree.ttree.domain.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String studentIdNum;
    private String password;
    private String role;
    private String major1;
    private String major2;
    private boolean status;

    private Long fileId;
    private String filename;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public User toEntity(){
        User build = User.builder()
                .id(id)
                .name(name)
                .studentIdNum(studentIdNum)
                .password(password)
                .role(role)
                .major1(major1)
                .major2(major2)
                .status(status)
                .build();
        return build;
    }

    @Builder
    public UserDto(Long id, String name, String studentIdNum, String password, String role, String major1, String major2, Long fileId, boolean status){
        this.id = id;
        this.name = name;
        this.studentIdNum = studentIdNum;
        this.password = password;
        this.role = role;
        this.major1 = major1;
        this.major2 = major2;
        this.fileId = fileId;
        this.status = status;
    }
}
