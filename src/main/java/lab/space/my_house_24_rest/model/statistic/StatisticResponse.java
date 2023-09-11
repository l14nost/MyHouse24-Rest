package lab.space.my_house_24_rest.model.statistic;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record StatisticResponse(
        Integer apartmentNumber,
        BigDecimal balance
) {
}
