# Web_Quiz_Spring_Hibernate_Hyperskill

##What this program does - is that it allows adding custom questions to a database.

We can add a user to a database with a given username (email to be exact) with a encrypted password.
Questions need to have at least two options eg. A or B?
Answer can be: A, or B, or A and B, or NONE.
Questions are being submited by a given user.
Only that user can delete that question.
Users can answer questions and information about completion is also being stored in datatbase.
Users can get information about all their completed questions.
Getting information about all quizes or quizzes completed supports pagination.

###API consists of:
#####/api/register - andds a user, email and password needed
    {
      "email": "test@gmail.com",
      "password": "secret",
      "role": "user"
    }
#####/api/quizzes - adds a quiz:
    {
      "title": "Quiz 2",
      "text": "Quiz with two correct answers.",
      "options": ["Robot","Tea leaf","Cup of coffee","Bug"],
      "answer": [0,2]
    }
  or:
    {
      "title": "Quiz 3",
      "text": "Quiz with no correct answer.",
      "options": ["Robot","Tea leaf","Cup of coffee","Bug"],
      "answer": []
    }
#####/api/quizzes/{quizId} - gets quiz with a given id
#####/api/quizzes/{quizId}/solve - sends answers for a quiz with given id
    {
      "answer": [1]
    }
#####/api/quizzes/{quizId} - deletes quiz with given id
#####/api/actuator/shutdown - shuts down application
#####/api/quizzes/completed - gets all completed quizzes for a given user, sorted by last completed first
