package lab.space.my_house_24_rest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lab.space.my_house_24_rest.config.JWTAuthenticationFilter;
import lab.space.my_house_24_rest.model.user.ProfileEditEmailRequest;
import lab.space.my_house_24_rest.model.user.UserResponseForProfile;
import lab.space.my_house_24_rest.service.UserService;
import lab.space.my_house_24_rest.validator.UserValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(ProfileController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class ProfileControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private UserValidator userValidator;
    @MockBean
    private JWTAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void profilePage() throws Exception {
        when(userService.findUserForProfile()).thenReturn(UserResponseForProfile.builder().id(1L).build());
        ResultActions response = mockMvc.perform(get("/profile")
                .content(objectMapper.writeValueAsString(UserResponseForProfile.builder().id(1L).build()))
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(UserResponseForProfile.builder().id(1L).build())));
    }
    @Test
    void profilePage_NotFound() throws Exception {
        when(userService.findUserForProfile()).thenThrow(new EntityNotFoundException());
        ResultActions response = mockMvc.perform(get("/profile")
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(content().string("User not found"));
    }

    @Test
    void editEmail() throws Exception {
        ProfileEditEmailRequest profileEditEmailRequest = new ProfileEditEmailRequest("mail@gmail.com");
        when(userService.getCurrentUser()).thenReturn(1L);
        ResultActions response = mockMvc.perform(post("/edit-email")
                .content(objectMapper.writeValueAsString(profileEditEmailRequest))
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(MockMvcResultMatchers.status().isOk());

    }
    @Test
    void editEmail_Valid() throws Exception {
        ProfileEditEmailRequest profileEditEmailRequest = new ProfileEditEmailRequest("mail");
        when(userService.getCurrentUser()).thenReturn(1L);
        ResultActions response = mockMvc.perform(post("/edit-email")
                .content(objectMapper.writeValueAsString(profileEditEmailRequest))
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(MockMvcResultMatchers.status().isBadRequest());

    }
}