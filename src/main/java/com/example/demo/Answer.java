package com.example.demo;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ANSWERS_REPO")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long answerId;

    @ElementCollection @CollectionTable(name="ANSWER_INDEXES"/*, joinColumns = @JoinColumn(name="AnswerId")*/)
    private List<Integer> answer = new ArrayList<>();

    public Answer() {
    }

    public Answer(List<Integer> answer) {
        this.answer = answer;
    }

    public List<Integer> getAnswer() {
        return answer;
    }

    public void setAnswer(List<Integer> answer) {
        this.answer = answer;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof Answer)) return false;
        Answer answer = (Answer) o;
        return answer.getAnswer().containsAll(this.getAnswer())
                && this.getAnswer().containsAll(answer.getAnswer());
    }
}
