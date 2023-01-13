package com.mracover.telegram_bot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document("message")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @MongoId (FieldType.OBJECT_ID)
    private String id;
    @Indexed
    @Field (name = "user")
    private User user;

    private String message;
    private byte[] image;
}
