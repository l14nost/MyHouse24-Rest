package lab.space.my_house_24_rest.validator;

import lab.space.my_house_24_rest.entity.User;
import lab.space.my_house_24_rest.repository.UserRepository;
import lab.space.my_house_24_rest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final UserRepository userRepository;
    public void uniqueEmail(String email, Long id, BindingResult result, String object){
        Locale locale = LocaleContextHolder.getLocale();
        String emailResponse;
        if (locale.toLanguageTag().equals("uk")) emailResponse = "Користувач із цією поштою вже існує";
        else emailResponse = "User with this email already exist";
        Optional<User> user = userRepository.findUserByEmail(email);
        if (id!=0){
            if (user.isPresent()) {
                if (!user.get().getId().equals(id)) {
                    result.addError(new FieldError(object, "email", emailResponse));
                }
            }
        }
        else{
            if (user.isPresent()){
                result.addError(new FieldError(object, "email", emailResponse));
            }
        }
    }

}
