package com.example.demo;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "completed_quizzes")
@JsonPropertyOrder({"id", "completedAt"})
//@JsonSerialize(using = CompletedJsonSerializer.class)
public class Completed {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long quizId;
    private long userId;
    @CreationTimestamp
    private LocalDateTime completedAt;

    public Completed() {
    }

    public Completed(long quizId, long userId) {
        this.quizId = quizId;
        this.userId = userId;
    }

    public long getId() {
        return id;
    }

    public long getQuizId() {
        return quizId;
    }

    public long getUserId() {
        return userId;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setQuizId(long quizId) {
        this.quizId = quizId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    //    @UpdateTimestamp
//    private LocalDateTime updateDateTime;
}

