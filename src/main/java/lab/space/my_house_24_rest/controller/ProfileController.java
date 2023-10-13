package lab.space.my_house_24_rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lab.space.my_house_24_rest.model.user.ProfileEditEmailRequest;
import lab.space.my_house_24_rest.service.UserService;
import lab.space.my_house_24_rest.utill.ErrorMapper;
import lab.space.my_house_24_rest.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Profile")
public class ProfileController {
    private final UserService userService;
    private final UserValidator userValidator;


    @Operation(summary = "Profile", security = {
            @SecurityRequirement(name = "bearerAuth")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "OK",content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(
                                    name = "Example 1",
                                    value = "{\n" +
                                            "  \"fullName\": \"Test Test Test\",\n" +
                                            "  \"id\": 1,\n" +
                                            "  \"email\": \"test@gmail.com\",\n" +
                                            "  \"apartments\": [\n" +
                                            "    {\n" +
                                            "      \"number\": 1,\n" +
                                            "      \"house\": \"house\",\n" +
                                            "      \"address\": \"address\",\n" +
                                            "      \"section\": \"1\",\n" +
                                            "      \"floor\": \"1\",\n" +
                                            "      \"bankBook\": {\n" +
                                            "        \"id\": 1,\n" +
                                            "        \"number\": \"000000001\"\n" +
                                            "      }\n" +
                                            "    }\n" +
                                            "  ]\n" +
                                            "}"
                            )
                    }
            )),
            @ApiResponse(responseCode = "400",description = "Bad Request"),
            @ApiResponse(responseCode = "401",description = "Unauthorized")
    })
    @GetMapping("/profile")
    public ResponseEntity profilePage(){
        try {
            return ResponseEntity.ok(userService.findUserForProfile());
        }
        catch (EntityNotFoundException e){
            return ResponseEntity.badRequest().body("User not found");
        }

    }
    @Operation(summary = "Edit email", security = {
            @SecurityRequirement(name = "bearerAuth")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "OK"),
            @ApiResponse(responseCode = "400",description = "Bad Request"),
            @ApiResponse(responseCode = "401",description = "Unauthorized")
    })
    @PostMapping("/edit-email")
    public ResponseEntity editEmail(@RequestBody @Valid ProfileEditEmailRequest profileEditEmailRequest, BindingResult result){
        if (profileEditEmailRequest.email()!=null){
            userValidator.uniqueEmail(profileEditEmailRequest.email(),userService.getCurrentUser(),result,"ProfileEditEmailRequest");
        }
        if (result.hasErrors()){
            return ResponseEntity.badRequest().body(ErrorMapper.mapErrors(result));
        }
        return userService.updateEmail(profileEditEmailRequest.email());
    }
}
