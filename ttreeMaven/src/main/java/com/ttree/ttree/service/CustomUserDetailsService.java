package com.ttree.ttree.service;

import com.ttree.ttree.domain.entity.CustomUserDetails;
import com.ttree.ttree.domain.entity.User;
import com.ttree.ttree.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String studentIdNum) throws UsernameNotFoundException {
        User user = userRepository.findBystudentIdNum(studentIdNum);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomUserDetails(user);
    }
}
