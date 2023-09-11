package lab.space.my_house_24_rest.model.message;

import lombok.Builder;

import java.time.LocalDate;
@Builder
public record MessageResponse(
        Long id,
        Boolean check,
        String title,
        String description,
        LocalDate date
) {
}
