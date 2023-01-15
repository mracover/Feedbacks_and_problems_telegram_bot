package com.mracover.telegram_bot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity(name = "feedbacks")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "feedbackMessage")
    private String feedbackMessage;

    @ManyToOne(
            cascade = {
                    CascadeType.REFRESH,
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.PERSIST
            },
            fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "problem")
    private List<Image> images;

}
