package com.example.demo;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class CompletedJsonSerializer extends JsonSerializer<Completed> {
    @Override
    public void serialize(Completed completed, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("id", completed.getQuizId());
        gen.writeStringField("completedAt", completed.getCompletedAt().toString());
        gen.writeEndObject();
    }
}
