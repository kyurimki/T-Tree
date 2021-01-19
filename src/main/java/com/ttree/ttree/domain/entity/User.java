package com.ttree.ttree.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(nullable = false, unique = true, length = 45)
    private String studentIdNum; //학번

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String major1;

    @Column
    private String major2;

    @Column(columnDefinition = "boolean default false")
    private boolean status;

    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }

    @Builder
    public User(Long user_id, String name, String studentIdNum, String email, String password, String role, String major1, String major2, boolean status){
        this.user_id = user_id;
        this.name = name;
        this.studentIdNum = studentIdNum;
        this.email = email;
        this.password = password;
        this.role = role;
        this.major1 = major1;
        this.major2 = major2;
        this.status = status;
    }
}