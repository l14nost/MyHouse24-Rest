package lab.space.my_house_24_rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lab.space.my_house_24_rest.config.JWTAuthenticationFilter;
import lab.space.my_house_24_rest.model.message.MessageResponse;
import lab.space.my_house_24_rest.model.message.MessageResponseForCard;
import lab.space.my_house_24_rest.service.MessageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(MessageController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class MessageControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MessageService messageService;
    @MockBean
    private JWTAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllMessages() throws Exception {
        Page<MessageResponse> messageResponsePage = new PageImpl<>(List.of(
                MessageResponse.builder().id(1L).build(),
                MessageResponse.builder().id(1L).build(),
                MessageResponse.builder().id(1L).build(),
                MessageResponse.builder().id(1L).build()
        ));
        when(messageService.findAllForMain(0)).thenReturn(messageResponsePage);
        ResultActions response = mockMvc.perform(post("/get-all-messages")
                        .param("page", "0")
                .content(objectMapper.writeValueAsString(messageResponsePage))
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getMessageForCard_IdNegative() throws Exception {
        ResultActions response = mockMvc.perform(get("/get-message-for-card/-1")
                .content("Id must be positive")
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @Test
    void getMessageForCard_EntityNotFound() throws Exception {
        when(messageService.findByIdForCard(1L)).thenThrow(new EntityNotFoundException());
        ResultActions response = mockMvc.perform(get("/get-message-for-card/1")
                .content("Message not found")
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void getMessageForCard() throws Exception {
        when(messageService.findByIdForCard(1L)).thenReturn(MessageResponseForCard.builder().title("title").build());
        ResultActions response = mockMvc.perform(get("/get-message-for-card/1")
                .content(objectMapper.writeValueAsString(MessageResponseForCard.builder().title("title").build()))
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(MessageResponseForCard.builder().title("title").build())));
    }
}