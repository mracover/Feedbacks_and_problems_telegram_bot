package com.mracover.telegram_bot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "feedback")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "feedbacks")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "product_id")
    private int product_id;

    @Column(name = "feedbackMessage")
    private String feedbackMessage;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private Image image;

    public void setImage(Image image) {
        this.image = image;
        this.image.setFeedback(this);
    }

    @Override
    public String toString() {
        return "Отзыв{" +
                ", id товара=" + product_id +
                ", Сообщение='" + feedbackMessage +
                "}\n";
    }
}
