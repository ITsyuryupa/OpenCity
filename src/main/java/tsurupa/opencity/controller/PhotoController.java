package tsurupa.opencity.controller;

import tsurupa.opencity.model.Photo;
import tsurupa.opencity.model.User;
import tsurupa.opencity.repository.CommunityRepository;
import tsurupa.opencity.repository.EventRepository;
import tsurupa.opencity.repository.UserRepository;
import tsurupa.opencity.service.CheckPermission;
import tsurupa.opencity.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Controller
@CrossOrigin("http://localhost:3000")
@RequestMapping("/api/photo")
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private CommunityRepository communityRepository;

    @PostMapping("/event/{event_id}")
    public ResponseEntity<String> uploadPhotoEvent(@RequestParam("file") MultipartFile file, @PathVariable("event_id") long id,
                                                  @RequestHeader String token) {
        String message = "";
        try {
            if (!file.getContentType().contains("image")){
                message = "Invalid file type";
                System.out.println(file.getContentType());
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
            }
            if(!CheckPermission.auth(userRepository, token)){
                return new ResponseEntity<>("Пользователь не аторизирован", HttpStatus.FORBIDDEN);
            }
            if(!eventRepository.findById(id).isPresent()){
                return new ResponseEntity<>("Событие не найдено", HttpStatus.FORBIDDEN);
            }
            Optional<User> user = userRepository.findByEmail(CheckPermission.tokenDecryption(token)[0]);
            photoService.storePhotoEvent(file, id, user.get());
            message = "Uploaded the file successfully";
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (Exception e) {
            message = "Could not upload the file";
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }

    @PostMapping("/community/{id}")
    public ResponseEntity<String> uploadPhotoCommunity(@RequestParam("file") MultipartFile file, @PathVariable("id") long id,
                                                   @RequestHeader String token) {
        String message = "";
        try {
            if (!file.getContentType().contains("image")){
                message = "Invalid file type";
                System.out.println(file.getContentType());
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
            }
            if(!CheckPermission.auth(userRepository, token)){
                return new ResponseEntity<>("Пользователь не аторизирован", HttpStatus.FORBIDDEN);
            }
            if(!communityRepository.findById(id).isPresent()){
                return new ResponseEntity<>("Сообщество не найдено", HttpStatus.FORBIDDEN);
            }
            Optional<User> user = userRepository.findByEmail(CheckPermission.tokenDecryption(token)[0]);
            photoService.storePhotoCommunity(file, id, user.get());
            message = "Uploaded the file successfully";
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (Exception e) {
            message = "Could not upload the file";
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getPhoto(@PathVariable String id) {
        Photo photo = photoService.getPhoto(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment")
                .body(photo.getData());
    }

    @GetMapping("/all/by-id-and-type/{id}")
    public ResponseEntity<List<String>> getAllPhotoIdByIdAndType(@PathVariable("id") long id, @RequestParam("type") int type) {
        try {
            List<String> filesId = photoService.getAllPhotoByEntityIdAndType(id, type);
            return new ResponseEntity<>(filesId, HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletePhoto(@PathVariable("id") String id) {
        try {
            photoService.deletePhoto(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
