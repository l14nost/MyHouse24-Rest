package lab.space.my_house_24_rest.service;

import lab.space.my_house_24_rest.model.message.MessageResponse;
import lab.space.my_house_24_rest.model.message.MessageResponseForCard;
import org.springframework.data.domain.Page;

public interface MessageService {
    Page<MessageResponse> findAllForMain(Integer page);

    MessageResponseForCard findByIdForCard(Long id);

    void changeCheck(Long id);
}
