package com.example.demo;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import java.io.IOException;

@JsonComponent
public class QuizJsonSerializer extends JsonSerializer<Quiz> {

    @Override
    public void serialize(Quiz quiz, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("id", quiz.getId());
        gen.writeStringField("title", quiz.getTitle());
        gen.writeStringField("text", quiz.getText());
        gen.writeFieldName("options");
            gen.writeArray(quiz.getOptions(), 0, quiz.getOptions().length);
        gen.writeEndObject();
    }
}
