    package com.game.visailu;

    import org.springframework.http.HttpStatus;
    import org.springframework.http.HttpStatusCode;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.HashMap;
    import java.util.HashSet;
    import java.util.Map;
    import java.util.Set;


    @RestController
    @RequestMapping("/") // Base
    public class GameController {

        private final Map<String, Participants> participants = new HashMap<>();
        private final Map<Integer, Questions> questions = new HashMap<>();




        

        // Root endpoint
        @GetMapping("/")
        public ResponseEntity<String> welcome() {
            return ResponseEntity.ok("Welcome to the Visailu game! Use the endpoints to play the game. For quiz info page add /info to localhost:8080/ end");
        }


        // info screen

        @GetMapping("/info")
        public ResponseEntity<Map<String, String>> getInfo() {
        Map<String, String> response = new HashMap<>();
        response.put("info", "This is the game information.");
        response.put("instructions", " Add a username using Postmans add username and put username in Value box, add as many usernames as you want. If you want to delete a username, use postmans DELETE request, localhost:8080/users/the-username-you-want-to-delete. Use the /question endpoint to get a question and /answer to submit an answer. Restarting the application resets everyting.");
        return ResponseEntity.ok(response);
        }

        


        // adds an user 
            @PostMapping("/users")
            public ResponseEntity<String> addUser(@RequestParam String username) {
                if (!participants.containsKey(username)) {
                    Participants participant = new Participants(username);
                    participants.put(username, participant);
                    return ResponseEntity.ok("User added.");
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists.");
                }
            }

            // näyttää kaikki käyttäjät
            @GetMapping("/users")
            public ResponseEntity<Set<String>> getAllUsernames() {
                return ResponseEntity.ok(participants.keySet());
            }


            // username delete
            @DeleteMapping("/users/{username}")
            public ResponseEntity<String> deleteUser(@PathVariable String username) {
                if (participants.containsKey(username)) {
                    participants.remove(username);
                    return ResponseEntity.ok("User has been deleted");
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User doesn't exist, write a another one.");
                }
            }


            //Question
        
            public GameController() {
                    
                questions.put(1, new Questions(1, "How many keys are there on a piano?", "88"));
                questions.put(2, new Questions(2, "What is the largest planet in our Solar System?", "Jupiter"));
                questions.put(3, new Questions(3, "What is the capital city of Australia?", "Canberra"));
                questions.put(4, new Questions(4, "What is the capital of Finland?", "Helsinki"));
                questions.put(5, new Questions(5, "What do the French call the English Channel?", "la Manche"));
                questions.put(6, new Questions(6, "What language is spoken in Brazil?", "Portuguese"));
                questions.put(7, new Questions(7, "What company is also the name of one of the longest rivers in the world?", "Amazon"));
                questions.put(8, new Questions(8, "Which planet has the most moons?", "Saturn"));
                questions.put(9, new Questions(9, "What part of a plant conducts photosynthesis?", "Leaf"));
                questions.put(10, new Questions(10, "In which year was the Microsoft XP operating system released?", "2001"));
                    
            }


        @GetMapping("/question")
        public ResponseEntity<Map<String, Object>> getQuestion(@RequestParam(value = "id") Integer questionId) {
            if (questionId == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Question ID is required"));
            }
            Questions question = questions.get(questionId);
            if (question == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Question not found"));
            }
            Map<String, Object> response = new HashMap<>();
            response.put("question", question.getQuery());
            response.put("id", questionId);
            return ResponseEntity.ok(response);
        }




        //answer

        @PostMapping("/answer")
        public ResponseEntity<String> submitAnswer(@RequestBody Answers answerRequest) {
            Participants participant = participants.get(answerRequest.getUsername());
            Questions question = questions.get(answerRequest.getQuestionId());
        

            if (question == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Question not found.");
            }
        
            boolean isCorrect = question.getAnswer().equalsIgnoreCase(answerRequest.getAnswer());
            if (isCorrect) {
                participant.incrementScore();
                return ResponseEntity.ok("Correct! The score for " + answerRequest.getUsername() + " is: " + participant.getScore());
            } else {
                return ResponseEntity.ok("Wrong! The score for " + answerRequest.getUsername() + " remains: " + participant.getScore());
            }
        }


        // näyttää käyttäjän pisteytyksen
        @GetMapping("/users/{username}/score")
        public ResponseEntity<String> getUserScore(@PathVariable String username) {
            Participants participant = participants.get(username);
            if (participant != null) {
                return ResponseEntity.ok("Score for " + username + " is: " + participant.getScore());

        }
            return null;
    }



    // Gets the winner based on the highest score
        @GetMapping("/users/winner")
        public ResponseEntity<String> getWinner() {
            Participants winner = null;
            int maxScore = -1; 

            for (Participants participant : participants.values()) {
                if (participant.getScore() > maxScore) {
                    winner = participant;
                    maxScore = participant.getScore();
                }
            }

            if (winner == null) {
                return ResponseEntity.ok("No participants or scores to find a winner winner chicken dinner :(");
            }
            
            return ResponseEntity.ok("Winner is " + winner.getUsername() + " with a score of " + maxScore + " / 10");
        }
    }

