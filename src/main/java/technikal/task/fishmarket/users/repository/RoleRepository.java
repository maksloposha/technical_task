package technikal.task.fishmarket.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import technikal.task.fishmarket.users.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String user);
}
