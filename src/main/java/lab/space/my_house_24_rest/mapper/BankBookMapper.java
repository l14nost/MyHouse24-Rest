package lab.space.my_house_24_rest.mapper;

import lab.space.my_house_24_rest.entity.BankBook;
import lab.space.my_house_24_rest.model.bankBook.BankBookResponseForProfile;

public class BankBookMapper {
    public static BankBookResponseForProfile entityToDtoForProfile(BankBook bankBook) {
        return BankBookResponseForProfile.builder()
                .id(bankBook.getId())
                .number(bankBook.getNumber())
                .build();
    }
}
