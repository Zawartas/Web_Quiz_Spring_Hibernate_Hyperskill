package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class QuizController {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;

    public QuizController() {
    }

    @PostMapping(path = "/api/quizzes/{quizId}/solve", consumes = "application/json")
    public ResponseEntity<String> sendAnswer(@PathVariable long quizId, @RequestBody Answer answer) {

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found for this id: " + quizId));

        if (quiz.getAnswer().equals(answer)) {
            return ResponseEntity.ok()
                    .body("{\"success\":true,\"feedback\":\"Congratulations, you're right!\"}");
        }
        return ResponseEntity.ok()
                .body("{\"success\":false,\"feedback\":\"Wrong answer! Please, try again.\"}");
    }

    @GetMapping(path = "/api/quizzes/{quizId}")
    public ResponseEntity<String> getQuiz(@PathVariable long quizId)
            throws JsonProcessingException, ResourceNotFoundException {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found for this id: " + quizId));
        return ResponseEntity.ok().body(objectMapper.writeValueAsString(quiz));
    }

    @GetMapping(path = "/api/quizzes")
    public ResponseEntity<String> getQuizzes() throws JsonProcessingException {
        return ResponseEntity.ok().body(objectMapper.writeValueAsString(quizRepository.findAll()));
    }

    @PostMapping(path = "/api/quizzes", consumes = "application/json")
    public ResponseEntity<String> addQuiz(@Valid @RequestBody Quiz quiz) throws JsonProcessingException {

        quizRepository.save(quiz);
        return ResponseEntity.ok().body(objectMapper.writeValueAsString(quiz));
    }

    @PostMapping(path = "/api/register", consumes = "application/json")
    public ResponseEntity<String> addUser(@Valid @RequestBody User user)
            throws JsonProcessingException, EmailExistsException {

        if (userRepository.getUserByUsername(user.getUsername()) != null) {
            throw new EmailExistsException(
                    "There is an account with that email address:" + user.getUsername());
        }

        System.out.println(user.toString());
        user.setPassword(encoder.encode(user.getPassword()));
        System.out.println(user.toString());

        userRepository.save(user);
        return ResponseEntity.ok().body(objectMapper.writeValueAsString(user));
    }

    @PostMapping(path = "/actuator/shutdown")
    public ResponseEntity<String> shutDownWebQuizEngine()  {
        return ResponseEntity.ok().build();
    }
}
