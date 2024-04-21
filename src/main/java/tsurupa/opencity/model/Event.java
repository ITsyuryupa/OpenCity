package tsurupa.opencity.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import tsurupa.opencity.model.utils.Status;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table
@Data
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private long id;

    private String title;

    private String description;

    private String address;

    private Date datetime_start;

    private Date datetime_end;

    private Double price_min;

    private Double price_max;

    private Date update_datetime;

    private Status status;

    @ManyToOne
    @JoinColumn(name="owner_user_id", nullable=false)
    private User user;

    @JsonProperty("user")
    public Long getUserId() {
        return user != null ? user.getId() : null;
    }

    @OneToMany
    @JoinColumn(name = "event_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Set<ReportEvent> reportEventSet;

    public void addReportEvent(ReportEvent reportEvent) {
        if (reportEventSet == null) {
            reportEventSet = new HashSet<>();
        }
        reportEventSet.add(reportEvent);
    }

    public void removeReportEvent(ReportEvent reportEvent) {
        if (reportEventSet != null) {
            reportEventSet.remove(reportEvent);
        }
    }

//    @OneToMany
//    @JoinColumn(name = "event_id")
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    private Set<FavoriteEvent> favoriteEventSet;

//    public void addFavoriteEvent(ReportEvent reportEvent) {
//        if (reportEventSet == null) {
//            reportEventSet = new HashSet<>();
//        }
//        reportEventSet.add(reportEvent);
//    }
//
//    public void removeFavoriteEvent(ReportEvent reportEvent) {
//        if (reportEventSet != null) {
//            reportEventSet.remove(reportEvent);
//        }
//    }
}
