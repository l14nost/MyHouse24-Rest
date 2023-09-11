package lab.space.my_house_24_rest.service.impl;

import lab.space.my_house_24_rest.entity.User;
import lab.space.my_house_24_rest.mapper.ApartmentMapper;
import lab.space.my_house_24_rest.model.statistic.StatisticResponse;
import lab.space.my_house_24_rest.service.StatisticService;
import lab.space.my_house_24_rest.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticServiceImpl implements StatisticService {
    private final UserService userService;
    @Override
    public List<StatisticResponse> balanceForStatistic() {
        log.info("Try to get statistic");
        User user = userService.findById(userService.getCurrentUser());
        return user.getApartmentList().stream().map(ApartmentMapper::entityToDtoForStatistic).toList();
    }
}
