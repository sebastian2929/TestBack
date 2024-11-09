package org.eci.model;

import java.util.ArrayList;
import java.util.List;

public class QuestionProvider {

    public static List<Question> getQuestions() {
        List<Question> questions = new ArrayList<>();

        // Preguntas de Ciencia
        questions.add(new Question("Ciencia", "¿Cuál es el planeta más grande del sistema solar?",
                List.of("A) Venus", "B) Júpiter", "C) Saturno", "D) Marte"), "B"));
        questions.add(new Question("Ciencia", "¿Cuál es la fórmula química del agua?",
                List.of("A) H2O2", "B) CO2", "C) H2O", "D) O2"), "C"));
        questions.add(new Question("Ciencia", "¿Qué partícula subatómica tiene carga negativa?",
                List.of("A) Protón", "B) Neutrón", "C) Electrón", "D) Quark"), "C"));
        questions.add(new Question("Ciencia", "¿Qué científico propuso la teoría de la relatividad?",
                List.of("A) Isaac Newton", "B) Galileo Galilei", "C) Albert Einstein", "D) Nikola Tesla"), "C"));
        questions.add(new Question("Ciencia", "¿Cuál es la unidad de medida de la resistencia eléctrica?",
                List.of("A) Voltio", "B) Ohmio", "C) Amperio", "D) Julio"), "B"));

        // Preguntas de Geografía
        questions.add(new Question("Geografía", "¿Cuál es el río más largo del mundo?",
                List.of("A) Amazonas", "B) Nilo", "C) Yangtsé", "D) Misisipi"), "A"));
        questions.add(new Question("Geografía", "¿Cuál es la capital de Australia?",
                List.of("A) Sídney", "B) Melbourne", "C) Canberra", "D) Brisbane"), "C"));
        questions.add(new Question("Geografía", "¿Cuál es el país más grande del mundo por área?",
                List.of("A) China", "B) Canadá", "C) Estados Unidos", "D) Rusia"), "D"));
        questions.add(new Question("Geografía", "¿En qué continente se encuentra el desierto del Sahara?",
                List.of("A) Asia", "B) América", "C) África", "D) Oceanía"), "C"));
        questions.add(new Question("Geografía", "¿Cuál es la montaña más alta del mundo?",
                List.of("A) K2", "B) Kangchenjunga", "C) Everest", "D) Makalu"), "C"));

        // Preguntas de Cultura
        questions.add(new Question("Cultura", "¿Quién escribió 'Cien años de soledad'?",
                List.of("A) Pablo Neruda", "B) Mario Vargas Llosa", "C) Gabriel García Márquez", "D) Jorge Luis Borges"), "C"));
        questions.add(new Question("Cultura", "¿En qué país se originó el tango?",
                List.of("A) Brasil", "B) España", "C) Argentina", "D) México"), "C"));
        questions.add(new Question("Cultura", "¿Cuál es el idioma oficial de Brasil?",
                List.of("A) Portugués", "B) Español", "C) Inglés", "D) Francés"), "A"));
        questions.add(new Question("Cultura", "¿Qué pintor es famoso por su obra 'La noche estrellada'?",
                List.of("A) Pablo Picasso", "B) Vincent van Gogh", "C) Salvador Dalí", "D) Claude Monet"), "B"));
        questions.add(new Question("Cultura", "¿Qué instrumento es característico de la música andina?",
                List.of("A) Guitarra", "B) Piano", "C) Charango", "D) Violín"), "C"));

        // Preguntas de Historia
        questions.add(new Question("Historia", "¿Quién fue el primer presidente de los Estados Unidos?",
                List.of("A) Thomas Jefferson", "B) Abraham Lincoln", "C) George Washington", "D) John Adams"), "C"));
        questions.add(new Question("Historia", "¿En qué año comenzó la Segunda Guerra Mundial?",
                List.of("A) 1914", "B) 1939", "C) 1945", "D) 1929"), "B"));
        questions.add(new Question("Historia", "¿Quién descubrió América en 1492?",
                List.of("A) Marco Polo", "B) Cristóbal Colón", "C) Amerigo Vespucci", "D) Vasco da Gama"), "B"));
        questions.add(new Question("Historia", "¿Qué imperio construyó la Muralla China?",
                List.of("A) Imperio Romano", "B) Imperio Otomano", "C) Dinastía Ming", "D) Dinastía Qin"), "D"));
        questions.add(new Question("Historia", "¿Qué documento fue firmado en 1215 limitando el poder del rey de Inglaterra?",
                List.of("A) Carta Magna", "B) Declaración de Independencia", "C) Tratado de Versalles", "D) Código Napoleónico"), "A"));

        return questions;
    }
}
