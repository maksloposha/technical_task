package technikal.task.fishmarket.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import technikal.task.fishmarket.users.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
