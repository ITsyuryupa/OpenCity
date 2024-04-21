package tsurupa.opencity.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import tsurupa.opencity.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    //List<User> findByNickname(String nickname);

    //boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    List<User> findAllByEmail(String email);
}
