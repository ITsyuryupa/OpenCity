package tsurupa.opencity.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import tsurupa.opencity.model.utils.EntityType;

import tsurupa.opencity.model.utils.Role;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table
@Data
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private long id;

    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonProperty("user")
    public Long getUserId() {
        return user != null ? user.getId() : null;
    }

    private Long entityId;

    private EntityType type;

}
