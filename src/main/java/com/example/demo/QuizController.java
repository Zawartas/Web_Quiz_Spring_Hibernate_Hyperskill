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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(("/api"))
public class QuizController {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CompletedRepository completedRepository;
    @Autowired
    private PasswordEncoder encoder;

    public QuizController() {
    }

    @PostMapping(path = "register", consumes = "application/json")
    public ResponseEntity<String> addUser(@Valid @RequestBody User user)
            throws JsonProcessingException, EmailExistsException {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailExistsException(
                    "There is an account with that email address:" + user.getEmail());
        }

        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        //objectMapper used to map to string
        return ResponseEntity.ok().body(objectMapper.writeValueAsString(user));
    }

    @PostMapping(path = "quizzes/{quizId}/solve", consumes = "application/json")
    public ResponseEntity<String> sendAnswer(@PathVariable long quizId,
                                             @RequestBody Answer answer,
                                             @AuthenticationPrincipal MyUserDetails myUserDetails) {

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found for this id: " + quizId));

        if (quiz.getAnswer().equals(answer)) {
            Completed completed = new Completed(quizId, myUserDetails.getUser().getUserId());
            completedRepository.save(completed);
            return ResponseEntity.ok()
                    .body("{\"success\":true,\"feedback\":\"Congratulations, you're right!\"}");
        }
        return ResponseEntity.ok()
                .body("{\"success\":false,\"feedback\":\"Wrong answer! Please, try again.\"}");
    }

    @GetMapping(path = "quizzes/{quizId}")
    public ResponseEntity<Quiz> getQuiz(@PathVariable long quizId) throws ResourceNotFoundException {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found for this id: " + quizId));
        return ResponseEntity.ok().body(quiz);
    }

    @GetMapping(path = "quizzes")
    public ResponseEntity<Page<Quiz>> getAllQuizzes(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize/*,
            @RequestParam(defaultValue = "id") String sortBy*/) {

        Pageable paging = PageRequest.of(page, pageSize);
        Page<Quiz> pagedResult = quizRepository.findAll(paging);

        return ResponseEntity.ok().body(pagedResult);
    }

    @GetMapping(path = "quizzes/completed", produces = "application/json")
    public ResponseEntity<Page<Completed>> getCompletedQuizzes(
            @AuthenticationPrincipal MyUserDetails myUserDetails,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "completedAt") String sortBy) {

        System.out.println("getCompletedQuizzes invoked");
        Pageable paging = PageRequest.of(page, pageSize, Sort.by(sortBy).descending());
        Page<Completed> pagedResult = completedRepository.findByUserId(myUserDetails.getUser().getUserId(), paging);

        System.out.println("this: " + pagedResult.toString());
        return ResponseEntity.ok().body(pagedResult);
    }

    @PostMapping(path = "quizzes", consumes = "application/json")
    public ResponseEntity<Quiz> addQuiz(@Valid @RequestBody Quiz quiz,
                                        @AuthenticationPrincipal MyUserDetails myUserDetails) {

        quiz.setUser(myUserDetails.getUser());

        quizRepository.save(quiz);
        return ResponseEntity.ok().body(/*objectMapper.writeValueAsString(quiz)*/quiz);
    }

    @DeleteMapping(path = "quizzes/{quizId}")
    public ResponseEntity<String> deleteQuiz(@PathVariable long quizId,
                                             @AuthenticationPrincipal MyUserDetails myUserDetails)
            throws ResourceNotFoundException {

        System.out.println("Delete Invoked");
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found for this id: " + quizId));

        System.out.println("title: " + quiz.getTitle());
        System.out.println("quizId: " + quiz.getId() + " user: " + quiz.getUser() + "\n");
        System.out.println(myUserDetails.getUser());

        if (quiz.getUser() == null) {
            System.out.println("we're here when user is null");
            quizRepository.delete(quiz);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }


        if ((quiz.getUser().getUserId() != myUserDetails.getUser().getUserId())) {
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
