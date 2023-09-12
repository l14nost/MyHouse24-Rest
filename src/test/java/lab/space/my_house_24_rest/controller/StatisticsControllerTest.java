package lab.space.my_house_24_rest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lab.space.my_house_24_rest.config.JWTAuthenticationFilter;
import lab.space.my_house_24_rest.model.message.MessageResponseForCard;
import lab.space.my_house_24_rest.model.statistic.StatisticResponse;
import lab.space.my_house_24_rest.service.MessageService;
import lab.space.my_house_24_rest.service.StatisticService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(StatisticsController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class StatisticsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private StatisticService statisticService;
    @MockBean
    private JWTAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getStatistics() throws Exception {
        List<StatisticResponse> statisticResponseList = List.of(
                StatisticResponse.builder().build(),
                StatisticResponse.builder().build(),
                StatisticResponse.builder().build()
        );
        when(statisticService.balanceForStatistic()).thenReturn(statisticResponseList);
        ResultActions response = mockMvc.perform(get("/get-statistics")
                .content(objectMapper.writeValueAsString(statisticResponseList))
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(statisticResponseList)));

    }

    @Test
    void getStatistics_NotFound() throws Exception {

        when(statisticService.balanceForStatistic()).thenThrow(new EntityNotFoundException());
        ResultActions response = mockMvc.perform(get("/get-statistics")
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(content().string("User not found"));

    }
}