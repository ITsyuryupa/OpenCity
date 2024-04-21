package tsurupa.opencity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tsurupa.opencity.model.Event;
import tsurupa.opencity.model.User;
import tsurupa.opencity.model.utils.Role;
import tsurupa.opencity.model.utils.Status;
import tsurupa.opencity.repository.EventRepository;
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

                eventRepository.save(newEvent);
                return new ResponseEntity<>("Событие создано", HttpStatus.OK);

            } else {
                return new ResponseEntity<>("Пользователь не авторизован", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/all/active")
    public ResponseEntity<?> getAllActivEvent(@RequestHeader("token") String token) {
        try {
            if(!CheckPermission.auth(userRepository, token)){
                return new ResponseEntity<>("Пользователь не аторизирован", HttpStatus.FORBIDDEN);
            }
            List<Event> events = eventRepository.findAllByStatus(Status.activ);
            return new ResponseEntity<>(events, HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @PutMapping("/update/{id}")
//    public ResponseEntity<?> updateHotel(@PathVariable("id") long id, @RequestBody Event event) {
//        Optional<Hotel> hotelData = hotelRepository.findById(id);
//
//        if (hotelData.isPresent()) {
//            if (hotel.getCity().equals("") || hotel.getName().equals("") || hotel.getEmail().equals("") || hotel.getCoordinates().equals("")
//                    || hotel.getDescription().equals("") || hotel.getStreet().equals("") || hotel.getHouseNumber().equals(null) || hotel.getCountry().equals("")) {
//                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//            }
//            Hotel _hotel = hotelData.get();
//            _hotel.setCity(hotel.getCity());
//            _hotel.setCountry(hotel.getCountry());
//            _hotel.setDescription(hotel.getDescription());
//            _hotel.setHouseNumber(hotel.getHouseNumber());
//            _hotel.setStreet(hotel.getStreet());
//            _hotel.setCoordinates(hotel.getCoordinates());
//            _hotel.setEmail(hotel.getEmail());
//            _hotel.setName(hotel.getName());
//            return new ResponseEntity<>(hotelRepository.save(_hotel), HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
}
