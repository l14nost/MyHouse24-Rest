package lab.space.my_house_24_rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lab.space.my_house_24_rest.model.auth.AuthenticationRequest;
import lab.space.my_house_24_rest.model.auth.AuthenticationResponse;
import lab.space.my_house_24_rest.service.impl.AuthenticationService;
import lab.space.my_house_24_rest.service.impl.LogoutService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
@Tag(name = "Authenticate")
@Slf4j
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @Operation(summary = "Authenticate")
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
    @Operation(summary = "Logout")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "OK"),
            @ApiResponse(responseCode = "400",description = "Bad Request"),
            @ApiResponse(responseCode = "401",description = "Unauthorized")
    })
    @PostMapping("/logout")
    public ResponseEntity logout(){
        return ResponseEntity.ok("Success logout");
    }

    @Operation(summary = "Refresh Token")
    @PostMapping("/refresh-token")
    public ResponseEntity refresh(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        return authenticationService.refreshToken(request, response);
    }
}
