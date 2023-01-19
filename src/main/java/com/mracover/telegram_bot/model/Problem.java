package com.mracover.telegram_bot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity (name = "problem")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "problems")
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "product_id")
    private int product_id;

    @Column(name = "problemMessage")
    private String problemMessage;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private Image image;

    public void setImage(Image image) {
        this.image = image;
        this.image.setProblem(this);
    }

    @Override
    public String toString() {
        return "Проблема{" +
                "id товара=" + product_id +
                "Cообщение='" + problemMessage +
                "}\n";
    }
}
