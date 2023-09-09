package lab.space.my_house_24_rest.model.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lab.space.my_house_24_rest.model.apartment.ApartmentResponseForProfile;
import lombok.Builder;

import java.util.List;

@Builder
public record UserResponseForProfile(
        @Schema(example = "Testov Test Testovich")
        String fullName,
        @Schema(example = "1")
        Long id,
        @Schema(example = "test@gmail.com")
        String email,
        @Schema
        List<ApartmentResponseForProfile> apartments
) {
}
