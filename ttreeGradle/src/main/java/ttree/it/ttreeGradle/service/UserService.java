package ttree.it.ttreeGradle.service;

import org.springframework.stereotype.Service;
import ttree.it.ttreeGradle.domain.entity.User;
import ttree.it.ttreeGradle.domain.repository.UserRepository;
import ttree.it.ttreeGradle.dto.UserDto;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Transactional
    public Long saveUser(UserDto userDto){ return userRepository.save(userDto.toEntity()).getId(); }

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
    
    @Transactional
    public boolean idCheck(String id){
        boolean id_status = false;
        if(userRepository.findBystudentIdNum(id) == null){
            id_status = true;
        }
        return id_status;
    }

}
