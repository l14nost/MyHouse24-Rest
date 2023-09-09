package lab.space.my_house_24_rest.model.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ProfileEditEmailRequest(
        @Schema(example = "newEmail@gmail.com")
        @NotBlank(message = "Must be specified")
        @Size(max = 100, message = "Size must be less than 100")
        @Pattern(message = "{pattern.email.message}", regexp = "^((([0-9A-Za-z]{1}[-0-9A-z\\.]{0,30}[0-9A-Za-z]?))@([-A-Za-z]{1,}\\.){1,}[-A-Za-z]{2,})$")
        String email
) {
}
