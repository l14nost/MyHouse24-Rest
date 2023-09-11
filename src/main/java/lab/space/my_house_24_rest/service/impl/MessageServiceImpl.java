package lab.space.my_house_24_rest.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lab.space.my_house_24_rest.entity.Message;
import lab.space.my_house_24_rest.mapper.MessageMapper;
import lab.space.my_house_24_rest.model.message.MessageResponse;
import lab.space.my_house_24_rest.model.message.MessageResponseForCard;
import lab.space.my_house_24_rest.repository.MessageRepository;
import lab.space.my_house_24_rest.service.MessageService;
import lab.space.my_house_24_rest.specification.MessageSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final UserServiceImpl userService;
    @Override
    public Page<MessageResponse> findAllForMain(Integer page) {
        MessageSpecification messageSpecification = MessageSpecification.builder().id(userService.getCurrentUser()).build();
        return messageRepository.findAll(messageSpecification,PageRequest.of(page,10)).map(MessageMapper::entityToMessageForMainPage);
    }

    @Override
    public MessageResponseForCard findByIdForCard(Long id) {
        return MessageMapper.entityToMessageForCard(findById(id));
    }

    @Override
    public void changeCheck(Long id) {
        Message message = findById(id);
        if (!message.getIsCheck()){
            message.setIsCheck(true);
            messageRepository.save(message);
        }
    }

    private Message findById(Long id){
        return messageRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Message by id: "+id+" not found"));
    }
}
