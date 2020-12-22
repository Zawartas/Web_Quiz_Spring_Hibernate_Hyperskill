package com.example.demo;

import javax.persistence.*;
import javax.sound.midi.Soundbank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ANSWERS_TO_QUESTIONS")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long answerId;

    @ElementCollection/* @CollectionTable(name="ANSWERS", joinColumns = @JoinColumn(name="answerId"))*/
    private List<Integer> answer = new ArrayList<>();

    public Answer() {
    }

    public Answer(List<Integer> answer) {
        System.out.println("answers: ");
        answer.forEach(integer -> System.out.print(integer + " "));
        System.out.println();
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
