package lab.space.my_house_24_rest.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lab.space.my_house_24_rest.entity.User;
import lab.space.my_house_24_rest.repository.UserRepository;
import lab.space.my_house_24_rest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(username).orElseThrow(()->new EntityNotFoundException("User by email:"+username+" not found"));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.getAuthorities());

    }
}
