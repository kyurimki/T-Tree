package ttree.it.ttreeGradle.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ttree.it.ttreeGradle.domain.entity.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.studentIdNum = ?1")
    public User findBystudentIdNum(String studentIdNum);

    @Query("SELECT u FROM User u WHERE u.status = false")
    public List<User> findUsersByStatusIsFalse();
}
