package tsurupa.opencity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tsurupa.opencity.model.Event;
import tsurupa.opencity.model.Report;
import tsurupa.opencity.model.utils.EntityType;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findAllByType(EntityType entityType);
}
