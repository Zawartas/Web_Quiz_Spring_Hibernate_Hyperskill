package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @GetMapping(path = "/api/quizzes", produces = "application/json")
    public ResponseEntity<Page<Quiz>> getAllQuizzes(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize/*,
            @RequestParam(defaultValue = "id") String sortBy*/) {

        Pageable paging = PageRequest.of(page, pageSize);
        Page<Quiz> pagedResult = quizRepository.findAll(paging);

        System.out.println("this: " + pagedResult.toString());
        return ResponseEntity.ok().body(pagedResult);
    }

    @PostMapping(path = "/api/quizzes", consumes = "application/json")
    public ResponseEntity<String> addQuiz(@Valid @RequestBody Quiz quiz,
                                          @AuthenticationPrincipal MyUserDetails myUserDetails)
            throws JsonProcessingException {

        quiz.setUser(myUserDetails.getUser());
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

    @DeleteMapping(path = "/api/quizzes/{quizId}")
    public ResponseEntity<String> deleteQuiz(@PathVariable long quizId,
                                             @AuthenticationPrincipal MyUserDetails myUserDetails)
            throws ResourceNotFoundException {

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found for this id: " + quizId));
//        System.out.println(quiz.getUser() + "\n");
//        System.out.println(myUserDetails.getUser());
        if (quiz.getUser().getUserId() != myUserDetails.getUser().getUserId()) {
            System.out.println("well we're here");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        quizRepository.delete(quiz);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping(path = "/actuator/shutdown")
    public ResponseEntity<String> shutDownWebQuizEngine()  {
        return ResponseEntity.ok().build();
    }
}
