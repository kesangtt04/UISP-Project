package UISP.repository;

import UISP.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepositoy extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    User save(User user);
    User findByEmail(String email);
}
