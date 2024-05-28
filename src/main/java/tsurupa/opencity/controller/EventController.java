package tsurupa.opencity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tsurupa.opencity.model.Event;
import tsurupa.opencity.model.Photo;
import tsurupa.opencity.model.Report;
import tsurupa.opencity.model.User;
import tsurupa.opencity.model.utils.EntityType;
import tsurupa.opencity.model.utils.Status;
import tsurupa.opencity.model.utils.Tag;
import tsurupa.opencity.repository.EventRepository;
import tsurupa.opencity.repository.PhotoRepository;
import tsurupa.opencity.repository.ReportRepository;
import tsurupa.opencity.repository.UserRepository;
import tsurupa.opencity.service.CheckPermission;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/event")
public class EventController {
    @Autowired
    EventRepository eventRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createEvent(@RequestBody Event event, @RequestHeader String token) {
        try {
            String[] parts = CheckPermission.tokenDecryption(token);

            // Получение email и пароля
            String email = parts[0];
            String password = parts[1];
            Optional<User> userAuthData = userRepository.findByEmail(email);

            if (CheckPermission.auth(userRepository, token)) {

                if (event == null) {
                    return new ResponseEntity<>("Event cannot be null", HttpStatus.BAD_REQUEST);
                }
                if (event.getTitle() == null || event.getTitle().isEmpty()) {
                    return new ResponseEntity<>( "Title cannot be null or empty", HttpStatus.BAD_REQUEST);
                }
                if (event.getDescription() == null || event.getDescription().isEmpty()) {
                    return new ResponseEntity<>( "Description cannot be null or empty", HttpStatus.BAD_REQUEST);
                }
                if (event.getAddress() == null || event.getAddress().isEmpty()) {
                    return new ResponseEntity<>( "Address cannot be null or empty", HttpStatus.BAD_REQUEST);
                }
                if (event.getDatetime_start() == null) {
                    return new ResponseEntity<>( "Datetime_start cannot be null", HttpStatus.BAD_REQUEST);
                }
                if (event.getDatetime_end() == null) {
                    return new ResponseEntity<>( "Datetime_end cannot be null", HttpStatus.BAD_REQUEST);
                }


                Event newEvent = new Event();
                newEvent.setTitle(event.getTitle());
                newEvent.setDescription(event.getDescription());
                newEvent.setAddress(event.getAddress());
                newEvent.setDatetime_start(event.getDatetime_start());
                newEvent.setDatetime_end(event.getDatetime_end());
                newEvent.setPrice_min(event.getPrice_min());
                newEvent.setPrice_max(event.getPrice_max());
                newEvent.setUpdate_datetime(new Date());
                newEvent.setUser(userAuthData.get());
                newEvent.setStatus(Status.verification);
                if(event.getTag() == null){
                    newEvent.setTag(Tag.другое);
                }else   {
                    newEvent.setTag(event.getTag());
                }

                eventRepository.save(newEvent);
                return new ResponseEntity<>(newEvent.getId(), HttpStatus.OK);

            } else {
                return new ResponseEntity<>("Пользователь не авторизован", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/all/status/{status}")
    public ResponseEntity<?> getAllEventByStatusValue(@PathVariable("status") Integer status,
                                                      @RequestHeader("token") String token) {
        try {
            if(!CheckPermission.moderator(userRepository, token)){
                return new ResponseEntity<>("Отказано в доступе", HttpStatus.FORBIDDEN);
            }

            List<Event> events = eventRepository.findAllEventByStatus(Status.getStatusByValue(status));
            return new ResponseEntity<>(events, HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all/active")
    public ResponseEntity<?> getAllActivEvent() {
        try {
            List<Event> events = eventRepository.findAllByStatus(Status.activ);
            return new ResponseEntity<>(events, HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all/my/status/{status}")
    public ResponseEntity<?> getMyEventByStatusValue(@PathVariable("status") Integer status, @RequestHeader("token") String token) {
        try {
            if(!CheckPermission.auth(userRepository, token)){
                return new ResponseEntity<>("Пользователь не аторизирован", HttpStatus.FORBIDDEN);
            }
            String[] parts = CheckPermission.tokenDecryption(token);
            Optional<User> user = userRepository.findByEmail(parts[0]);
            List<Event> events = eventRepository.findAllEventByUserAndStatus(user.get(), Status.getStatusByValue(status));
            return new ResponseEntity<>(events, HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getEventById(@PathVariable("id") long id, @RequestHeader String token) {
        try {
            Optional<Event> event = eventRepository.findById(id);


            return new ResponseEntity<>(event.get(), HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable("id") long id, @RequestBody Event updatedEvent,
                                         @RequestHeader String token) {
        Optional<Event> eventData = eventRepository.findById(id);

        if (eventData.isPresent() && CheckPermission.himself_moderator(userRepository, eventData.get().getUser(), token)) {
            Event existingEvent = eventData.get();

            // Проверка на пустые значения или нулевые поля
            if (updatedEvent.getTitle() == null || updatedEvent.getTitle().isEmpty() ||
                    updatedEvent.getDescription() == null || updatedEvent.getDescription().isEmpty() ||
                    updatedEvent.getAddress() == null || updatedEvent.getAddress().isEmpty() ||
                    updatedEvent.getDatetime_start() == null ||
                    updatedEvent.getDatetime_end() == null ||
                    updatedEvent.getPrice_min() == null || updatedEvent.getPrice_max() == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            // Обновление данных о событии
            existingEvent.setTitle(updatedEvent.getTitle());
            existingEvent.setDescription(updatedEvent.getDescription());
            existingEvent.setAddress(updatedEvent.getAddress());
            existingEvent.setDatetime_start(updatedEvent.getDatetime_start());
            existingEvent.setDatetime_end(updatedEvent.getDatetime_end());
            existingEvent.setPrice_min(updatedEvent.getPrice_min());
            existingEvent.setPrice_max(updatedEvent.getPrice_max());

            existingEvent.setUpdate_datetime(new Date());

            if(updatedEvent.getTag() != null){
                existingEvent.setTag(updatedEvent.getTag());
            }
            if(userRepository.findByEmail(CheckPermission.tokenDecryption(token)[0]).get().getRole().getValue() > 0){
                existingEvent.setStatus(updatedEvent.getStatus());
            }
            // Сохранение обновленного события в базе данных
            Event updatedEventData = eventRepository.save(existingEvent);
            return new ResponseEntity<>("Событие создано", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable("id") long id, @RequestHeader String token) {
        try {
            Optional<Event> event = eventRepository.findById(id);
            if(CheckPermission.himself_moderator(userRepository, event.get().getUser(), token)){
                eventRepository.deleteById(id);

                List<Report> reports = reportRepository.findAllByType(EntityType.event);
                for (int i = reports.size() - 1; i >= 0; i--) {
                    if (reports.get(i).getEntityId() == id) {
                        reportRepository.delete(reports.get(i));
                    }
                }

                List<String> photos = photoRepository.findAllByEntityIdAndType(id, EntityType.event);
                for (String photoId:  photos) {
                    photoRepository.deleteById(photoId);
                }

                return new ResponseEntity<>("Событие удалено",HttpStatus.OK);
            }

            return new ResponseEntity<>("Отказано в доступе",HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
