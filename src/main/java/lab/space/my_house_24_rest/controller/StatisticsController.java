package lab.space.my_house_24_rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lab.space.my_house_24_rest.service.MessageService;
import lab.space.my_house_24_rest.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Statistic")
public class StatisticsController {
    private final StatisticService statisticService;

    @Operation(summary = "Statistics")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "OK"),
            @ApiResponse(responseCode = "400",description = "Bad Request"),
            @ApiResponse(responseCode = "401",description = "Unauthorized")
    })
    @GetMapping("/get-statistics")
    public ResponseEntity getMessageForCard(){
       try {
           return ResponseEntity.ok(statisticService.balanceForStatistic());
       }
       catch (EntityNotFoundException e){
           return ResponseEntity.badRequest().body("User not found");
       }

    }
}
