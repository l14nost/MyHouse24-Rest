package lab.space.my_house_24_rest.enums;

import lombok.RequiredArgsConstructor;

import java.util.Locale;

@RequiredArgsConstructor
public enum BankBookStatus {
    ACTIVE("Active", "Активний"),
    INACTIVE("Inactive", "Неактивний");
    private final String nameEn;
    private final String nameUk;

    public String getBankBookStatus(Locale locale) {
        if (locale.getLanguage().equalsIgnoreCase("uk")) {
            return nameUk;
        }
        return nameEn;
    }
}
