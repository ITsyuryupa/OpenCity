package tsurupa.opencity.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import tsurupa.opencity.model.utils.Status;
import tsurupa.opencity.model.utils.Tag;

import java.util.Date;

@Entity
@Table
@Data
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String address;

    private Date datetime_start;

    private Date datetime_end;

    private Double price_min;

    private Double price_max;

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
