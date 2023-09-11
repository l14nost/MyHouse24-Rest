package lab.space.my_house_24_rest.mapper;

import lab.space.my_house_24_rest.entity.Apartment;
import lab.space.my_house_24_rest.model.apartment.ApartmentResponseForProfile;
import lab.space.my_house_24_rest.model.bankBook.BankBookResponseForProfile;
import lab.space.my_house_24_rest.model.statistic.StatisticResponse;

import java.math.BigDecimal;

public class ApartmentMapper {
    public static ApartmentResponseForProfile entityToDtoForProfile(Apartment apartment) {
        BankBookResponseForProfile bankBookResponseForProfile = null;
        if (apartment.getBankBook()!=null) {
            bankBookResponseForProfile = BankBookMapper.entityToDtoForProfile(apartment.getBankBook());
        }

        return ApartmentResponseForProfile.builder()
                .address(apartment.getHouse().getAddress())
                .section(apartment.getSection().getName())
                .floor(apartment.getFloor().getName())
                .house(apartment.getHouse().getName())
                .number(apartment.getNumber())
                .bankBook(bankBookResponseForProfile)
                .build();
    }

    public static StatisticResponse entityToDtoForStatistic(Apartment apartment){
        BigDecimal totalPrice = BigDecimal.ZERO;
        if(apartment.getBankBook()!=null){
            totalPrice = apartment.getBankBook().getTotalPrice();
        }
        return StatisticResponse.builder()
                .apartmentNumber(apartment.getNumber())
                .balance(totalPrice)
                .build();
    }
}
