package com.mracover.telegram_bot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Document("user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @MongoId(FieldType.OBJECT_ID)
    private String id;
    private Long telegramUserId;
    private String name;
    @Indexed
    private String email;
    @DBRef
    private List<Message> message;
}
