package tsurupa.opencity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tsurupa.opencity.model.Community;
import tsurupa.opencity.model.User;
import tsurupa.opencity.model.utils.Status;

import java.util.List;
import java.util.Optional;

public interface CommunityRepository  extends JpaRepository<Community, Long> {
    List<Community> findAllByStatus(Status status);

    List<Community> findAllCommunityByUserAndStatus(User user, Status status);

    List<Community> findAllCommunityByStatus(Status status);
}
