package lab.space.my_house_24_rest.model.bankBook;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record BankBookResponseForProfile(
        @Schema(example = "1")
        Long id,
        @Schema(example = "000000010")
        String number
) {
}
