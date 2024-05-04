package tsurupa.opencity.service;

import tsurupa.opencity.model.User;
import tsurupa.opencity.repository.UserRepository;

import java.util.Optional;

public class CheckPermission {

    public static String generateNewToken(User user){
        return user.getEmail() + "$" + user.getPassword();
    }

    public static boolean auth(UserRepository userRepository, String token){
        String[] parts = tokenDecryption(token);

        // Получение email и пароля
        String email = parts[0];
        String password = parts[1];

        Optional<User> userAuthData = userRepository.findByEmail(email);
        return userAuthData.isPresent() && userAuthData.get().getPassword().equals(password);
    }

    public static String[] tokenDecryption(String token){
        String[] parts = token.split("\\$");

        // Получение email и пароля
        String email = parts[0];
        String password = parts[1];

        return parts;
    }
    public static boolean himself(UserRepository userRepository,Optional<User> user, String token){
        String[] parts = tokenDecryption(token);

        // Получение email и пароля
        String email = parts[0];
        String password = parts[1];

        Optional<User> userAuthData = userRepository.findByEmail(email);
        if(userAuthData.isPresent() && userAuthData.get().getPassword().equals(password)){
            if(!user.get().getEmail().equals(userAuthData.get().getEmail())){

                    return false;

            }
        }else{
            return false;
        }
        return true;
    }

    public static boolean himself_moderator(UserRepository userRepository,Optional<User> user, String token){
        String[] parts = tokenDecryption(token);

        // Получение email и пароля
        String email = parts[0];
        String password = parts[1];

        Optional<User> userAuthData = userRepository.findByEmail(email);
        if(userAuthData.isPresent() && userAuthData.get().getPassword().equals(password)){
            if(!user.get().getEmail().equals(userAuthData.get().getEmail())){
                if(userAuthData.get().getRole().getValue() < 1){
                    return false;
                }
            }
        }else{
            return false;
        }
        return true;
    }

    public static boolean himself_moderator(UserRepository userRepository,User user, String token){
        String[] parts = tokenDecryption(token);

        // Получение email и пароля
        String email = parts[0];
        String password = parts[1];

        Optional<User> userAuthData = userRepository.findByEmail(email);
        if(userAuthData.isPresent() && userAuthData.get().getPassword().equals(password)){
            if(!user.getEmail().equals(userAuthData.get().getEmail())){
                if(userAuthData.get().getRole().getValue() < 1){
                    return false;
                }
            }
        }else{
            return false;
        }
        return true;
    }

    public static boolean himself_admin(UserRepository userRepository, Optional<User> user, String token){
        String[] parts = tokenDecryption(token);

        // Получение email и пароля
        String email = parts[0];
        String password = parts[1];

        Optional<User> userAuthData = userRepository.findByEmail(email);
        if(userAuthData.isPresent() && userAuthData.get().getPassword().equals(password)){
            if(!user.get().getEmail().equals(userAuthData.get().getEmail())){
                if(userAuthData.get().getRole().getValue() < 2){
                    return false;
                }
            }
        }else{
            return false;
        }
        return true;
    }

    public static boolean moderator(UserRepository userRepository, String token){
        String[] parts = token.split("\\$");

        // Получение email и пароля
        String email = parts[0];
        String password = parts[1];

        Optional<User> userAuthData = userRepository.findByEmail(email);
        if(userAuthData.isPresent() && userAuthData.get().getPassword().equals(password)){
            if(userAuthData.get().getRole().getValue() < 1){
                return false;
            }

        }else{
            return false;
        }
        return true;
    }

    public static boolean admin(UserRepository userRepository, String token){
        String[] parts = token.split("\\$");

        // Получение email и пароля
        String email = parts[0];
        String password = parts[1];

        Optional<User> userAuthData = userRepository.findByEmail(email);
        if(userAuthData.isPresent() && userAuthData.get().getPassword().equals(password)){
            if(userAuthData.get().getRole().getValue() < 2){
                return false;
            }

        }else{
            return false;
        }
        return true;
    }

}
