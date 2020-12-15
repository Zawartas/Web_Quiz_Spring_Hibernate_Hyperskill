//package com.example.demo;
//
//import com.fasterxml.jackson.core.JsonGenerator;
//import com.fasterxml.jackson.core.JsonParser;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.core.ObjectCodec;
//import com.fasterxml.jackson.databind.*;
//import org.springframework.boot.jackson.JsonComponent;
//
//import java.io.IOException;
//
//@JsonComponent
//public class QuizJsonDeserializer extends JsonDeserializer<Quiz> {
//
//    @Override
//    public Quiz deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
//        ObjectCodec cc = p.getCodec();
//        JsonNode node = cc.readTree(p);
//
//        final String title = node.get("title").asText();
//        final String text = node.get("text").asText();
//        final String[] options = node.get("options").asText().split(",");
//        final JsonNode answersNode = node.get("answers");
//        answersNode.
//        final int[] answers = node.get("answers").as;
//        return null;
//    }
//}
