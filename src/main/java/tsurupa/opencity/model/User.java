package tsurupa.opencity.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;
import java.util.Set;
import tsurupa.opencity.model.utils.Role;

@Entity
@Table(name = "_user")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private long id;

    private String email;

    private String password;

    @Column(name = "registation_date")
    private Date registationDate;

    private Role role;

    @OneToMany
    @JoinColumn(name = "owner_user_id")
    private Set<Event> eventSet;

    @OneToMany
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    private Set<ReportEvent> reportEventSet;

//    @OneToMany(mappedBy="user")
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    private Set<FavoriteEvent> favoriteEventSet;


}

