package org.eci.controller;

import org.eci.model.Question;
import org.eci.model.QuestionProvider;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class GameController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private Map<String, List<String>> lobbies = new HashMap<>();
    private Map<String, Integer> playerScores = new HashMap<>();
    private List<Question> questions = QuestionProvider.getQuestions();
    private Map<String, Integer> currentQuestionIndex = new HashMap<>();
    private Map<String, String> currentTurnPlayer = new HashMap<>();
    private Map<String, String> currentTopics = new HashMap<>(); // Para almacenar el tema actual de cada juego
    private List<Map<String, String>> games = new ArrayList<>(); // Lista para almacenar los juegos
    private Map<String, Set<String>> usedQuestions = new HashMap<>();

    public GameController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void clearAllGames() {
        lobbies.clear();
        playerScores.clear();
        currentQuestionIndex.clear();
        currentTurnPlayer.clear();
        currentTopics.clear();
        games.clear(); // Limpiar la lista de juegos también
    }
    
    @MessageMapping("/create")
    public void createGame(String gameData) {
        try {
            Map<String, String> gameDetails = new ObjectMapper().readValue(gameData, Map.class);
            games.add(gameDetails); // Añadir los detalles del juego a la lista de juegos
            simpMessagingTemplate.convertAndSend("/topic/games", games);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @MessageMapping("/join")
    public void joinLobby(Map<String, String> data) {
        String gameName = data.get("gameName");
        String userName = data.get("userName");

        lobbies.putIfAbsent(gameName, new ArrayList<>());
        if (!lobbies.get(gameName).contains(userName)) {
            lobbies.get(gameName).add(userName);
        }

        simpMessagingTemplate.convertAndSend("/topic/lobby/" + gameName, lobbies.get(gameName));
    }

    @MessageMapping("/startGame")
    public void startGame(String gameName) {
        List<String> players = lobbies.getOrDefault(gameName, new ArrayList<>());
        if (!players.isEmpty()) {
            simpMessagingTemplate.convertAndSend("/topic/gameStart/" + gameName, players);
            usedQuestions.put(gameName, new HashSet<>()); // Inicializar el conjunto para este juego
            selectRandomPlayerForTurn(gameName);
        }
    }


    private void selectRandomPlayerForTurn(String gameName) {
        List<String> players = lobbies.get(gameName);
        if (players != null && !players.isEmpty()) {
            Random random = new Random();
            String selectedPlayer = players.get(random.nextInt(players.size()));
            currentTurnPlayer.put(gameName, selectedPlayer);
            simpMessagingTemplate.convertAndSend("/topic/turn/" + gameName, selectedPlayer);
        }
    }

    @MessageMapping("/topicSelected")
    public void selectTopic(Map<String, String> data) {
        String gameName = data.get("gameName");
        String selectedTopic = data.get("topic");

        // Guardar el tema actual para el juego
        currentTopics.put(gameName, selectedTopic);

        // Enviar mensaje a todos sobre el tema seleccionado
        simpMessagingTemplate.convertAndSend("/topic/topicSelected/" + gameName, selectedTopic);

        // Ahora que se ha seleccionado el tema, obtenemos la pregunta
        sendRandomQuestion(gameName, selectedTopic);
    }

    @MessageMapping("/spinWheel")
    public void spinWheel(String gameName) {
        // Obtener el tema actual del juego
        String currentTopic = currentTopics.get(gameName);
        sendRandomQuestion(gameName, currentTopic);
    }

    private void sendRandomQuestion(String gameName, String topic) {
    // Obtener todas las preguntas de la categoría
    List<Question> filteredQuestions = questions.stream()
            .filter(q -> q.getCategory().equals(topic))
            .collect(Collectors.toList()); // Filtrar preguntas por categoría

    // Inicializar el conjunto de preguntas usadas si no existe
    usedQuestions.putIfAbsent(gameName, new HashSet<>());

    // Filtrar preguntas que no han sido usadas
    List<Question> availableQuestions = filteredQuestions.stream()
            .filter(q -> !usedQuestions.get(gameName).contains(questions.indexOf(q))) // Usar índice para comprobar si se ha usado
            .collect(Collectors.toList());

    // Si hay preguntas disponibles, seleccionar una aleatoriamente
    if (!availableQuestions.isEmpty()) {
        Random random = new Random();
        Question question = availableQuestions.get(random.nextInt(availableQuestions.size())); // Seleccionar pregunta aleatoria
        simpMessagingTemplate.convertAndSend("/topic/question/" + gameName, question);
        currentQuestionIndex.put(gameName, questions.indexOf(question)); // Almacenar índice de la pregunta actual
    } else {
        // Si no hay preguntas disponibles, puedes manejar la lógica para reiniciar o finalizar el juego
        simpMessagingTemplate.convertAndSend("/topic/question/" + gameName, "No hay más preguntas disponibles.");
    }
    }
    @MessageMapping("/submitAnswer")
    public void submitAnswer(Map<String, String> data) {
        String gameName = data.get("gameName");
        String userName = data.get("userName");
        String selectedOption = data.get("answer"); // Opción seleccionada por el usuario (A, B, C o D)

        // Obtener la pregunta actual usando el índice almacenado
        int index = currentQuestionIndex.getOrDefault(gameName, -1);
        if (index == -1) {
            // Manejo de error si no hay pregunta actual
            System.out.println("No hay pregunta actual.");
            return;
        }

        Question currentQuestion = questions.get(index);
        String correctAnswer = currentQuestion.getCorrectAnswer(); // Respuesta correcta de la pregunta

        // Mensajes de depuración
        System.out.println("Pregunta actual: " + currentQuestion.getQuestionText());
        System.out.println("Opción seleccionada por " + userName + ": " + selectedOption);
        System.out.println("Respuesta correcta: " + correctAnswer);

        // Registrar que el jugador ha respondido
        usedQuestions.get(gameName).add(userName);

        // Extraer solo la letra de la opción seleccionada (A, B, C, D)
        String selectedLetter = selectedOption.split(" ")[0].replace(")", ""); // Obtener solo "A", "B", "C", "D"

        // Comparar la opción seleccionada con la respuesta correcta
        boolean isCorrect = selectedLetter.equals(correctAnswer);
        System.out.println("¿La respuesta es correcta? " + isCorrect);

        if (isCorrect) {
            // Sumar punto al jugador si la respuesta es correcta
            int newScore = playerScores.getOrDefault(userName, 0) + 1;
            playerScores.put(userName, newScore);
            simpMessagingTemplate.convertAndSend("/topic/scores/" + gameName, playerScores);
            simpMessagingTemplate.convertAndSend("/topic/pointWinner/" + gameName, userName + " ganó el punto!");
            System.out.println(userName + " respondió correctamente y ganó un punto. Puntuación actual: " + newScore);
        }

        // Verificar si todos los jugadores han respondido
        boolean allResponded = lobbies.get(gameName).stream().allMatch(player -> usedQuestions.get(gameName).contains(player));
        System.out.println("Todos los jugadores han respondido: " + allResponded);

        if (allResponded) {
            // Comprobar si algún jugador ha respondido correctamente
            boolean anyoneCorrect = playerScores.values().stream().anyMatch(score -> score > 0);
            
            if (!anyoneCorrect) {
                // Si nadie ha respondido correctamente, enviar un mensaje
                simpMessagingTemplate.convertAndSend("/topic/pointWinner/" + gameName, "Nadie ganó el punto.");
                System.out.println("Nadie ganó el punto."); // Mensaje de depuración
            }

            // Reiniciar el conjunto de respuestas usadas para la siguiente ronda
            resetGame(gameName); // Llamada a resetGame para reiniciar el juego
        }

        // Verificar si hay un ganador
        checkWinner(gameName);
    }

    

    private void resetGame(String gameName) {
        // Reiniciar el estado del juego para el siguiente turno
        usedQuestions.put(gameName, new HashSet<>()); // Limpiar las respuestas usadas
        currentQuestionIndex.remove(gameName); // Limpiar el índice de la pregunta actual

        // Seleccionar un nuevo jugador para girar la ruleta
        selectRandomPlayerForTurn(gameName);
    }






    private void checkWinner(String gameName) {
        for (Map.Entry<String, Integer> entry : playerScores.entrySet()) {
            if (entry.getValue() >= 5) {
                simpMessagingTemplate.convertAndSend("/topic/winner/" + gameName, entry.getKey());
                resetGame(gameName);
                break;
            }
        }
    }
    

    
}
