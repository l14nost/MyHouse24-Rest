package lab.space.my_house_24_rest.model.message;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record MessageResponseForCard(
        String title,
        String descriptionStyle,
        LocalDate date
) {
}
