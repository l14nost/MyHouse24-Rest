package lab.space.my_house_24_rest.model.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {
    @Schema(example = "admin@gmail.com")
    private String login;
    @Schema(example = "pass")
    private String password;
}
