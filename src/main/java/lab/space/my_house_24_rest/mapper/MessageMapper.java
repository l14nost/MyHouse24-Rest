package lab.space.my_house_24_rest.mapper;

import lab.space.my_house_24_rest.entity.Message;
import lab.space.my_house_24_rest.model.message.MessageResponse;
import lab.space.my_house_24_rest.model.message.MessageResponseForCard;

import java.time.ZoneId;

public class MessageMapper {

    public static MessageResponse entityToMessageForMainPage(Message message){
        return MessageResponse.builder()
                .id(message.getId())
                .check(message.getIsCheck())
                .date(message.getSendDate().atZone(ZoneId.systemDefault()).toLocalDate())
                .description(message.getDescription())
                .title(message.getTitle())
                .build();
    }

    public static MessageResponseForCard entityToMessageForCard(Message message){
        return MessageResponseForCard.builder()
                .date(message.getSendDate().atZone(ZoneId.systemDefault()).toLocalDate())
                .descriptionStyle(message.getDescriptionStyle())
                .title(message.getTitle())
                .build();
    }


}
