package com.example.quiz_eg;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quiz_eg.utils.Constants;
import com.example.quiz_eg.utils.RandomUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class QuizActivity extends AppCompatActivity {

	Questions questions;
	Questions.Question currentQuestion;
	int currentQuestionIndex;
	int score;
	int questionNumber;

	@BindView(R.id.questionTextView)
	TextView questionTextView;
	@BindView(R.id.scoreTextView)
	TextView scoreTextView;
	@BindView(R.id.answersListView)
	ListView answersListView;

	@OnItemClick(R.id.answersListView)
	public void setAnswersListView_onClick(int pos) {
		String answer = (String) answersListView.getItemAtPosition(pos);

		if (answer.equals(currentQuestion.answer)) {
			score += 1;
			Toast.makeText(this, R.string.goodAnswer, Toast.LENGTH_SHORT).show();
		} else
			Toast.makeText(this, R.string.badAnswer, Toast.LENGTH_SHORT).show();
		currentQuestionIndex += 1;
		if (currentQuestionIndex < questions.number())
			updateQuestion();
		else {
			scoreTextView.setText(getString(R.string.scoreTextView, score, questionNumber));
			gameOver();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz);

		ButterKnife.bind(this);

		File file = (File) getIntent().getSerializableExtra(Constants.QUIZ_EXTRA);
		questions = new Questions(file);

		currentQuestionIndex = 0;
		score = 0;
		questionNumber = questions.number();

		updateQuestion();
	}

	private void updateQuestion() {

		scoreTextView.setText(getString(R.string.scoreTextView, score, questionNumber));

		currentQuestion = questions.getQuestion(currentQuestionIndex);
		questionTextView.setText(currentQuestion.body);

		int
			banswerNumber = currentQuestion.badAnswer.size(),
			answerNumber = banswerNumber + 1;

		String[] answers = new String[answerNumber];
		answers[0] = currentQuestion.answer;
		for (int i = 0; i < banswerNumber; ++i)
			answers[i] = currentQuestion.badAnswer.get(i);
		answers[banswerNumber] = currentQuestion.answer;
		RandomUtils.ShuffleArray(answers);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				this.getApplicationContext(),
				R.layout.quiz_item,
				R.id.quiz_item,
				answers
		);
		answersListView.setAdapter(adapter);
	}

    /* Dialogs and exiting */

	private void gameOver() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(QuizActivity.this);
		alertDialogBuilder
				.setMessage(getString(R.string.gameOverMsg, score))
				.setCancelable(false)
				.setPositiveButton(R.string.tryAgainLabel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						startActivity(getIntent());
						finish();
					}
				})
				.setNegativeButton(R.string.exitLabel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}
}
