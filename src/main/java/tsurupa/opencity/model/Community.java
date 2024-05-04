package tsurupa.opencity.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import tsurupa.opencity.model.utils.Status;
import tsurupa.opencity.model.utils.Tag;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table
@Data
public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private long id;

    private String title;

    private String description;

    private String contact_info;

    private Tag tag;

    private Date update_datetime;

    private Status status;

    @ManyToOne
    @JoinColumn(name="owner_user_id", nullable=false)
    private User user;

    @JsonProperty("user")
    public Long getUserId() {
        return user != null ? user.getId() : null;
    }

}

