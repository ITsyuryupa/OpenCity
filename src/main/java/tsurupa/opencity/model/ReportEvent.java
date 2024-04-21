package tsurupa.opencity.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "report_event")
@Data
public class ReportEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private long id;

    private String description;

//    @ManyToOne
//    @JoinColumn(name="user_id", nullable=false)
//    private User user;

//    @ManyToOne
//    @JoinColumn(name="event_id", nullable=false)
//    private Event event;
}
