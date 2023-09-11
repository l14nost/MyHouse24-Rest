package lab.space.my_house_24_rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lab.space.my_house_24_rest.model.user.ProfileEditEmailRequest;
import lab.space.my_house_24_rest.service.MessageService;
import lab.space.my_house_24_rest.service.UserService;
import lab.space.my_house_24_rest.utill.ErrorMapper;
import lab.space.my_house_24_rest.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Message")
public class MessageController {
    private final MessageService messageService;


    @Operation(summary = "Messages")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "OK"),
            @ApiResponse(responseCode = "400",description = "Bad Request"),
            @ApiResponse(responseCode = "401",description = "Unauthorized")
    })
    @PostMapping("/get-all-messages")
    public ResponseEntity profilePage(@RequestParam Integer page){
        return ResponseEntity.ok(messageService.findAllForMain(page));
    }
    @Operation(summary = "Message card")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "OK"),
            @ApiResponse(responseCode = "400",description = "Bad Request"),
            @ApiResponse(responseCode = "401",description = "Unauthorized")
    })
    @GetMapping("/get-message-for-card/{id}")
    public ResponseEntity getMessageForCard(@PathVariable Long id){
       if (id<=0){
           return ResponseEntity.badRequest().body("Id must be positive");
       }
       try {
           return ResponseEntity.ok(messageService.findByIdForCard(id));
       }
       catch (EntityNotFoundException e){
           return ResponseEntity.badRequest().body("Message not found");
       }

    }
}
