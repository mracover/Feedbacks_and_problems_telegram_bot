package com.mracover.telegram_bot.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "telegramUserId")
    private long telegramUserId;

    @Column(name = "name")
    @Size(max = 20)
    private String name;

    @Column(name = "email")
    @Email
    private String email;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user")
    private List<Problem> problems = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user")
    private List<Feedback> feedbacks = new ArrayList<>();

    public void setProblem (Problem problem) {
        problems.add(problem);
        problem.setUser(this);
    }

    public void setFeedback (Feedback feedback) {
        feedbacks.add(feedback);
        feedback.setUser(this);
    }

    @Override
    public String toString() {
        StringBuilder stringProblems = new StringBuilder();
        StringBuilder stringFeedbacks = new StringBuilder();
        problems.forEach(problem -> stringProblems.append(problems.toString()));
        feedbacks.forEach(feedback -> stringFeedbacks.append(feedback.toString()));
        return  " Имя ='" + name + '\n' +
                ", email='" + email + '\n' +
                ", problems=" + stringProblems.toString() + '\n' +
                ", feedbacks=" + stringFeedbacks.toString() ;
    }
}
