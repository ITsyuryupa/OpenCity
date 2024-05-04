package tsurupa.opencity.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tsurupa.opencity.model.Photo;
import tsurupa.opencity.model.Report;
import tsurupa.opencity.model.utils.EntityType;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, String> {


    @Query("SELECT p.id FROM Photo p WHERE p.entityId = :entityId AND p.type = :type")
    List<String> findAllByEntityIdAndType(@Param("entityId") Long entityId, @Param("type") EntityType type);



}
