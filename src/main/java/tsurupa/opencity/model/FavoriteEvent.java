//package tsurupa.opencity.model;
//
//import jakarta.persistence.*;
//import lombok.Data;
//import org.hibernate.annotations.OnDelete;
//import org.hibernate.annotations.OnDeleteAction;
//
//import java.util.Date;
//
//@Entity
//@Table(name = "favorite_event")
//@Data
//public class FavoriteEvent {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(columnDefinition = "serial")
//    private long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="user_id", nullable=false)
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    private User user;
//
//    @ManyToOne
//    @JoinColumn(name="event_id", nullable=false)
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    private Event event;
//
//    private Date created_at;
//}
