package woowa.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import woowa.demo.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
