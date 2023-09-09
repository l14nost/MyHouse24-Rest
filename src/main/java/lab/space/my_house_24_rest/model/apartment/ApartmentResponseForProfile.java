package lab.space.my_house_24_rest.model.apartment;

import io.swagger.v3.oas.annotations.media.Schema;
import lab.space.my_house_24_rest.entity.BankBook;
import lab.space.my_house_24_rest.model.bankBook.BankBookResponseForProfile;
import lombok.Builder;

@Builder
public record ApartmentResponseForProfile(
        @Schema(example = "1")
        Integer number,
        @Schema(example = "House")
        String house,
        @Schema(example = "Address")
        String address,
        @Schema(example = "Section")
        String section,
        @Schema(example = "Floor")
        String floor,
        @Schema
        BankBookResponseForProfile bankBook

) {
}
