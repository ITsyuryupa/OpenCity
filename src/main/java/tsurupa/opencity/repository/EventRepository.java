package tsurupa.opencity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tsurupa.opencity.model.Event;
import tsurupa.opencity.model.User;
import tsurupa.opencity.model.utils.Status;

import java.util.List;
import java.util.Optional;

public interface EventRepository  extends JpaRepository<Event, Long> {
    List<Event> findAllByStatus(Status status);
}
