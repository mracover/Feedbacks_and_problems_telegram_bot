package com.mracover.telegram_bot.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "telegramUserId")
    private long telegramUserId;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user")
    private List<Problem> problems = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user")
    private List<Feedback> feedbacks = new ArrayList<>();

    public void setProblems(List<Problem> problems) {
        if (problems !=null) {
            problems.forEach(s -> s.setUser(this));
        }
        this.problems = problems;
    }

    public void setFeedbacks(List<Feedback> feedbacks) {
        if (feedbacks !=null) {
            feedbacks.forEach(s -> s.setUser(this));
        }
        this.feedbacks = feedbacks;
    }

    public void setProblem (Problem problem) {
        problems.add(problem);
        problem.setUser(this);
    }

    public void setFeedback (Feedback feedback) {
        feedbacks.add(feedback);
        feedback.setUser(this);
    }
}
