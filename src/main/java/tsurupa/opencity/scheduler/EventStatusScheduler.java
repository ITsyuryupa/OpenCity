package tsurupa.opencity.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tsurupa.opencity.model.Event;
import tsurupa.opencity.model.utils.Status;
import tsurupa.opencity.repository.EventRepository;

import java.util.Date;
import java.util.List;

@Component
public class EventStatusScheduler {

    @Autowired
    private EventRepository eventRepository;

    @Scheduled(cron = "0 0 0 * * ?") // Каждый день в полночь
    public void updateEventStatuses() {
        Date now = new Date();
        List<Event> events = eventRepository.findAllByStatus(Status.activ);

        for (Event event : events) {
            if (event.getDatetime_end().before(now)) {
                event.setStatus(Status.archive);
                eventRepository.save(event);
            }
        }
    }

}

