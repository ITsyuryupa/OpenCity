package tsurupa.opencity.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import tsurupa.opencity.model.utils.EntityType;

@Entity
@Table
@Data
public class Photo {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Lob
    private byte[] data;

    private long entityId;

    private EntityType type;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonProperty("user")
    public Long getUserId() {
        return user != null ? user.getId() : null;
    }




}

