package com.example.quiz_eg;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    Questions questions;

    Random random;

    @BindView(R.id.answer1Button)
    Button answer1Button;
    @BindView(R.id.answer2Button)
    Button answer2Button;
    @BindView(R.id.answer3Button)
    Button answer3Button;
    @BindView(R.id.answer4Button)
    Button answer4Button;

    @BindView(R.id.questionTextView)
    TextView questionTextView;
    @BindView(R.id.scoreTextView)
    TextView scoreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questions = new Questions();

        random = new Random();

        ButterKnife.bind(this);
        scoreTextView.setText(getString(R.string.scoreTextView, 0));

        updateQuestion();
    }

    @OnClick(R.id.answer1Button)
    public void answer1Button_onClick(Button b) {

    }

    void updateQuestion() {
        Question question = questions.getQuestion(0);

        questionTextView.setText(question.body);
    }
}
