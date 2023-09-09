package lab.space.my_house_24_rest.mapper;

import lab.space.my_house_24_rest.entity.Apartment;
import lab.space.my_house_24_rest.model.apartment.ApartmentResponseForProfile;
import lab.space.my_house_24_rest.model.bankBook.BankBookResponseForProfile;

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
}
