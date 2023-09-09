package lab.space.my_house_24_rest.service;

import lab.space.my_house_24_rest.entity.User;
import lab.space.my_house_24_rest.model.user.UserResponseForProfile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User findById(Long id);

    Long getCurrentUser();

    UserResponseForProfile findUserForProfile();

    ResponseEntity updateEmail(String email);
}
