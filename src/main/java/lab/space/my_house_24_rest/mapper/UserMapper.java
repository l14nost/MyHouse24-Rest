package lab.space.my_house_24_rest.mapper;

import lab.space.my_house_24_rest.entity.User;
import lab.space.my_house_24_rest.model.user.UserResponseForProfile;

public class UserMapper {

    public static UserResponseForProfile entityToDtoForProfile(User user){
        return UserResponseForProfile.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getLastname()+" "+user.getFirstname()+" "+user.getSurname())
                .apartments(user.getApartmentList().stream().map(ApartmentMapper::entityToDtoForProfile).toList())
                .build();
    }
}
