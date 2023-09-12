package lab.space.my_house_24_rest.service.impl;

import lab.space.my_house_24_rest.entity.Apartment;
import lab.space.my_house_24_rest.entity.Message;
import lab.space.my_house_24_rest.model.message.MessageResponse;
import lab.space.my_house_24_rest.model.message.MessageResponseForCard;
import lab.space.my_house_24_rest.repository.MessageRepository;
import lab.space.my_house_24_rest.service.UserService;
import lab.space.my_house_24_rest.specification.MessageSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceImplTest {
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private MessageServiceImpl messageService;

    @Test
    void findAllForMain() {
        List<Message> messageList = List.of(
                Message.builder().id(1L).sendDate(Instant.now()).build(),
                Message.builder().id(2L).sendDate(Instant.now()).build(),
                Message.builder().id(3L).sendDate(Instant.now()).build(),
                Message.builder().id(4L).sendDate(Instant.now()).build()
        );
        when(userService.getCurrentUser()).thenReturn(1L);
        MessageSpecification messageSpecification = MessageSpecification.builder().id(1L).build();
        when(messageRepository.findAll(messageSpecification, PageRequest.of(0,10))).thenReturn(new PageImpl<>(messageList));
        Page<MessageResponse> messages =  messageService.findAllForMain(0);
        assertEquals(messageList.size(), messages.getTotalElements());
    }

    @Test
    void findByIdForCard() {
        when(messageRepository.findById(1L)).thenReturn(Optional.of(Message.builder().id(1L).sendDate(Instant.now()).build()));
        MessageResponseForCard messageResponseForCard = messageService.findByIdForCard(1L);
        assertEquals(messageResponseForCard, MessageResponseForCard.builder().date(LocalDate.now()).build());
    }

    @Test
    void changeCheck() {
        when(messageRepository.findById(1L)).thenReturn(Optional.of(Message.builder().id(1L).isCheck(false).build()));
        messageService.changeCheck(1L);
        verify(messageRepository).save(Message.builder().id(1L).isCheck(true).build());

    }
}