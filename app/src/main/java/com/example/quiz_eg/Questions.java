package com.example.quiz_eg;

class Question {

    public final String body;
    public final String answer;
    public final String[] badAnswer;

    Question (String body, String answer, String... badAnswer) {
        this.body = body;
        this.answer = answer;
        this.badAnswer = badAnswer;
    }
}

class Questions {

    private Question[] question;

    public Questions() {
        question = new Question[3];
        question[0] = new Question("Czy pierniki są słodkie?", "Yes", "No", "I dunno", "Yes and no");
        question[1] = new Question("Czy ziemniaki są słodkie?", "Yes", "No", "I dunno", "Yes and no");
        question[2] = new Question("Czy bułeczki są słodkie?", "Yes", "No", "I dunno", "Yes and no");
    }

    public Question getQuestion(int i) {
        return question[i];
    }
}


