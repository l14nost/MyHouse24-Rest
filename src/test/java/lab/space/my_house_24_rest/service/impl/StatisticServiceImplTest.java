package lab.space.my_house_24_rest.service.impl;

import lab.space.my_house_24_rest.entity.Apartment;
import lab.space.my_house_24_rest.entity.User;
import lab.space.my_house_24_rest.model.apartment.ApartmentResponseForProfile;
import lab.space.my_house_24_rest.model.statistic.StatisticResponse;
import lab.space.my_house_24_rest.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatisticServiceImplTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private StatisticServiceImpl statisticService;

    @Test
    void balanceForStatistic() {
        List<Apartment> apartments = List.of(
                Apartment.builder().build(),
                Apartment.builder().build(),
                Apartment.builder().build(),
                Apartment.builder().build(),
                Apartment.builder().build()
        );
        when(userService.getCurrentUser()).thenReturn(1L);
        when(userService.findById(1L)).thenReturn(User.builder().apartmentList(apartments).build());

        List<StatisticResponse> statisticResponseList = statisticService.balanceForStatistic();

        assertEquals(apartments.size(), statisticResponseList.size());

    }
}