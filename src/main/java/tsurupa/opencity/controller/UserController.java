package tsurupa.opencity.controller;



import org.springframework.web.bind.annotation.*;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import tsurupa.opencity.model.utils.Role;
import tsurupa.opencity.model.User;
import tsurupa.opencity.repository.UserRepository;
import tsurupa.opencity.service.CheckPermission;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            if (!userRepository.existsByEmail(user.getEmail())) {
                if (user.getPassword().equals("") || user.getEmail().equals("")) {
                    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                }
                User newUser = new User();
                newUser.setEmail(user.getEmail());
                newUser.setPassword(user.getPassword());
                newUser.setRole(Role.user);
                newUser.setRegistationDate(new Date());

                User _user = userRepository.save(newUser);
                return new ResponseEntity<>(CheckPermission.generateNewToken(_user), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Почта занята",HttpStatus.CONFLICT);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signinUser(@RequestParam(name = "userEmail") String userEmail, @RequestParam(name = "userPassword") String userPassword) {
        try {
            Optional<User> users = userRepository.findByEmail(userEmail);
            if (!users.isPresent()) {
                return new ResponseEntity<>("пользователь не найден",HttpStatus.NOT_FOUND);
            } else if (users.get().getPassword().equals(userPassword)) {
                return new ResponseEntity<>(CheckPermission.generateNewToken(users.get()) , HttpStatus.OK);
            } else {
                return new ResponseEntity<>("пароль неверный", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping("/update/email")
    public ResponseEntity<?> updateUserEmail(@RequestParam(name = "oldEmail") String oldEmail, @RequestParam(name = "newEmail") String newEmail, @RequestHeader("token") String token) {
        if(userRepository.existsByEmail(newEmail)){
            return new ResponseEntity<>("Почта занята", HttpStatus.BAD_REQUEST);
        }
        String[] tokenArr = CheckPermission.tokenDecryption(token);
        String email = tokenArr[0];
        String password = tokenArr[1];

        Optional<User> user = userRepository.findByEmail(oldEmail);
        if (CheckPermission.himself(userRepository, user, token)) {

            User _user = user.get();
            _user.setEmail(newEmail);
            userRepository.save(_user);

            return new ResponseEntity<>(CheckPermission.generateNewToken(_user), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Отказано в доступе",HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/password")
    public ResponseEntity<?> updateUserPassword(@RequestParam(name = "userEmail") String userEmail, @RequestParam(name = "newPassword") String newPassword, @RequestHeader("token") String token) {

        String[] tokenArr = CheckPermission.tokenDecryption(token);
        String email = tokenArr[0];
        String password = tokenArr[1];

        Optional<User> user = userRepository.findByEmail(userEmail);

        if (CheckPermission.himself(userRepository, user, token)) {

            User _user = user.get();
            _user.setPassword(newPassword);
            userRepository.save(_user);

            return new ResponseEntity<>(CheckPermission.generateNewToken(_user), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Отказано в доступе",HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/role")
    public ResponseEntity<?> updateUserRole(@RequestParam(name = "userEmail") String userEmail, @RequestParam(name = "newRole") Integer newRole, @RequestHeader("token") String token) {

        String[] tokenArr = CheckPermission.tokenDecryption(token);
        String email = tokenArr[0];
        String password = tokenArr[1];

        Optional<User> user = userRepository.findByEmail(userEmail);

        if (CheckPermission.admin(userRepository, token)) {

            User _user = user.get();
            _user.setRole(Role.values()[newRole]);
            userRepository.save(_user);

            return new ResponseEntity<>("Роль изменина",HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Отказано в доступе",HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<Optional<User>> getUserById(@PathVariable("user_id") long id, @RequestHeader("token") String token) {
        try {

            Optional<User> user = userRepository.findById(id);

            /* check permission, user himmself or moderator and higher */
            if(!CheckPermission.himself_moderator(userRepository, user, token)){
                return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
            }
            return new ResponseEntity<>(user, HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUser(@RequestHeader("token") String token) {
        try {
            if(!CheckPermission.admin(userRepository, token)){
                return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
            }
            List<User> user = userRepository.findAll();
            return new ResponseEntity<>(user, HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") long id) {
//        try {
//            userRepository.deleteById(id);
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

}
