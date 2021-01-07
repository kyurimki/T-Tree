package com.ttree.ttree.domain.repository;

import com.ttree.ttree.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.studentIdNum = ?1")
    public User findBystudentIdNum(String studentIdNum);
}
