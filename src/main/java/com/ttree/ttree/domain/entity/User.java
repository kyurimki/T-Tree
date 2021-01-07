package com.ttree.ttree.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
public class User {

    @Id
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(nullable = false, unique = true, length = 45)
    private String studentIdNum; //학번

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false)
    private String identity;

    @Column(nullable = false)
    private String major1;

    @Column
    private String major2;

    //인증사진 추가

    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }

    @Builder
    public User(Long id, String name, String studentIdNum, String password, String identity, String major1, String major2){
        this.id = id;
        this.name = name;
        this.studentIdNum = studentIdNum;
        this.password = password;
        this.identity = identity;
        this.major1 = major1;
        this.major2 = major2;
    }
}