package com.example.quiz_eg;

public class Questions {
    class Question {
        final String body;
        final String answer;
        final String[] badAnswer;
        Question (String body, String answer, String... badAnswer) {
            this.body = body;
            this.answer = answer;
            this.badAnswer = badAnswer;
        }
    };
    Question[] question;


}
