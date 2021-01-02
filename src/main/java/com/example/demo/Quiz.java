package com.example.demo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "quizzes")
@JsonSerialize(using = QuizJsonSerializer.class)
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotBlank(message = "Title is mandatory")
    private String title;
    @NotBlank(message = "Text is mandatory")
    private String text;
    @NotNull(message = "Options can't be null.")
    @Size(min = 2, message = "Need at least two options in a quiz.")
    @ElementCollection
    @OrderColumn
    private String[] options;
    @OneToOne(cascade = CascadeType.ALL)
    private Answer answer = new Answer();
    @ManyToOne
    @JoinColumn(name ="userId")
    private User user;

    public Quiz() {
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public String[] getOptions() {
        return options;
    }

    public Answer getAnswer() {
        return answer;
    }

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public void setAnswer(List<Integer> answer) {
        this.answer = new Answer(answer);
    }
}