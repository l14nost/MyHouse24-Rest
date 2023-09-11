package lab.space.my_house_24_rest.service;

import lab.space.my_house_24_rest.model.statistic.StatisticResponse;

import java.util.List;

public interface StatisticService {
    List<StatisticResponse> balanceForStatistic();
}
