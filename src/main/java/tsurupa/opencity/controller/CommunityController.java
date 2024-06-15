package tsurupa.opencity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tsurupa.opencity.model.*;
import tsurupa.opencity.model.utils.EntityType;
import tsurupa.opencity.model.utils.Status;
import tsurupa.opencity.model.utils.Tag;
import tsurupa.opencity.repository.*;
import tsurupa.opencity.service.CheckPermission;

import java.io.Console;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/community")
public class CommunityController {
    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private ReportRepository reportRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createCommunity(@RequestBody Community community, @RequestHeader String token) {
        try {
            String[] parts = CheckPermission.tokenDecryption(token);

            // Получение email и пароля
            String email = parts[0];
            String password = parts[1];
            Optional<User> userAuthData = userRepository.findByEmail(email);

            if (CheckPermission.auth(userRepository, token)) {

                if (community == null) {
                    return new ResponseEntity<>("Event cannot be null", HttpStatus.BAD_REQUEST);
                }
                if (community.getTitle() == null || community.getTitle().isEmpty()) {
                    return new ResponseEntity<>( "Title cannot be null or empty", HttpStatus.BAD_REQUEST);
                }
                if (community.getDescription() == null || community.getDescription().isEmpty()) {
                    return new ResponseEntity<>( "Description cannot be null or empty", HttpStatus.BAD_REQUEST);
                }
                if (community.getContact_info() == null || community.getContact_info().isEmpty()) {
                    return new ResponseEntity<>( "Contact_info cannot be null or empty", HttpStatus.BAD_REQUEST);
                }

                Community newCommunity = new Community();
                newCommunity.setTitle(community.getTitle());
                newCommunity.setDescription(community.getDescription());
                newCommunity.setContact_info(community.getContact_info());
                newCommunity.setUpdate_datetime(LocalDateTime.now());
                newCommunity.setUser(userAuthData.get());
                newCommunity.setStatus(Status.verification);
                if(community.getTag() == null || !Tag.exist(community.getTag().getValue())){
                    System.out.println(Tag.другое);
                    newCommunity.setTag(Tag.другое);
                }else{
                    newCommunity.setTag(community.getTag());
                }

                communityRepository.save(newCommunity);
                return new ResponseEntity<>(newCommunity.getId(), HttpStatus.OK);

            } else {
                return new ResponseEntity<>("Пользователь не авторизован", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/all/status/{status}")
    public ResponseEntity<?> getAllСommunityByStatusValue(@PathVariable("status") Integer status, @RequestHeader("token") String token) {
        try {
            if(!CheckPermission.moderator(userRepository, token)){
                return new ResponseEntity<>("Отказано в доступе", HttpStatus.FORBIDDEN);
            }

            List<Community> сommunities = communityRepository.findAllCommunityByStatus(Status.getStatusByValue(status));
            return new ResponseEntity<>(сommunities, HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all/active")
    public ResponseEntity<?> getAllActivCommunity() {
        try {
            List<Community> communities = communityRepository.findAllByStatus(Status.activ);
            return new ResponseEntity<>(communities, HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all/my/status/{status}")
    public ResponseEntity<?> getMyCommunityByStatusValue(@PathVariable("status") Integer status, @RequestHeader("token") String token) {
        try {
            if(!CheckPermission.auth(userRepository, token)){
                return new ResponseEntity<>("Пользователь не аторизирован", HttpStatus.FORBIDDEN);
            }
            String[] parts = CheckPermission.tokenDecryption(token);
            Optional<User> user = userRepository.findByEmail(parts[0]);
            List<Community> communities = communityRepository.findAllCommunityByUserAndStatus(user.get(), Status.getStatusByValue(status));
            return new ResponseEntity<>(communities, HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getCommunityById(@PathVariable("id") long id) {
        try {
            Optional<Community> community = communityRepository.findById(id);


            return new ResponseEntity<>(community.get(), HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCommunity(@PathVariable("id") long id, @RequestBody Community updatedCommunity, @RequestHeader String token) {
        Optional<Community> communityData = communityRepository.findById(id);

        if (communityData.isPresent() && CheckPermission.himself_moderator(userRepository, communityData.get().getUser(), token)) {
            Community existingCommunity = communityData.get();

            // Проверка на пустые значения или нулевые поля
            if (updatedCommunity.getTitle() == null || updatedCommunity.getTitle().isEmpty() ||
                    updatedCommunity.getDescription() == null || updatedCommunity.getDescription().isEmpty() ||
                    updatedCommunity.getContact_info() == null || updatedCommunity.getContact_info().isEmpty()
                    ) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            // Обновление данных
            existingCommunity.setTitle(updatedCommunity.getTitle());
            existingCommunity.setDescription(updatedCommunity.getDescription());
            existingCommunity.setContact_info(updatedCommunity.getContact_info());
            existingCommunity.setUpdate_datetime(LocalDateTime.now());
            if(updatedCommunity.getTag() != null){
                existingCommunity.setTag(updatedCommunity.getTag());
            }

            if(userRepository.findByEmail(CheckPermission.tokenDecryption(token)[0]).get().getRole().getValue() > 0){

                existingCommunity.setStatus(updatedCommunity.getStatus());
            }

            communityRepository.save(existingCommunity);
            return new ResponseEntity<>("Сообщество обновлено", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCommunity(@PathVariable("id") long id, @RequestHeader String token) {
        try {
            Optional<Community> community = communityRepository.findById(id);
            if(CheckPermission.himself_moderator(userRepository, community.get().getUser(), token)){
                communityRepository.deleteById(id);

                List<Report> reports = reportRepository.findAllByType(EntityType.community);
                for (int i = reports.size() - 1; i >= 0; i--) {
                    if (reports.get(i).getEntityId() == id) {
                        reportRepository.delete(reports.get(i));
                    }
                }

                List<String> photos = photoRepository.findAllByEntityIdAndType(id, EntityType.community);
                for (String photoId:  photos) {
                    photoRepository.deleteById(photoId);
                }

                return new ResponseEntity<>("Сообщество удалено",HttpStatus.OK);
            }

            return new ResponseEntity<>("Отказано в доступе",HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

