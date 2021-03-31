package ttree.it.ttreeGradle.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ttree.it.ttreeGradle.domain.entity.CustomUserDetails;
import ttree.it.ttreeGradle.domain.entity.User;
import ttree.it.ttreeGradle.domain.repository.UserRepository;

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
