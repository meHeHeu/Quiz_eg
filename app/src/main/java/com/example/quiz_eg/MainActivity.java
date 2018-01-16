package com.example.quiz_eg;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    /* Question container and settings */

    Questions questions;
    Questions.Question currentQuestion;
    int currentQuestionIndex;

    /* UI elements */

    int score;

    @BindView(R.id.questionTextView)
    TextView questionTextView;
    @BindView(R.id.scoreTextView)
    TextView scoreTextView;

    @BindViews({R.id.answer1Button, R.id.answer2Button, R.id.answer3Button, R.id.answer4Button})
    List<Button> answerButtons;

    @OnClick({R.id.answer1Button, R.id.answer2Button, R.id.answer3Button, R.id.answer4Button})
    public void answerButton_onClick(Button b) {
        if(b.getText().equals(currentQuestion.answer)) {
            ++score;
            scoreTextView.setText(getString(R.string.scoreTextView, score));
            Toast.makeText(this, R.string.goodAnswer, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.badAnswer, Toast.LENGTH_SHORT).show();
        }
        if((++currentQuestionIndex)<questions.amount())
            updateQuestion();
        else
            gameOver();
    }

    /* Quiz flow (?) */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            requestStoragePermission();

        scoreTextView.setText(getString(R.string.scoreTextView, 0));

        String[] quizQuestionsPath = new String[]{
                Environment.getExternalStorageDirectory().getPath()+"/"+getString(R.string.app_name),
                Environment.getExternalStorageDirectory().getPath()+"/Android/data/"+getString(R.string.app_name),
                Environment.getExternalStorageDirectory().getPath()+"/Documents/data/"+getString(R.string.app_name)
        };
        questions = new Questions(quizQuestionsPath);
        if(currentQuestionIndex >= questions.amount()) {
            noQuestionFileFound();
            return;
        }
        currentQuestionIndex = 0;

        score = 0;

        updateQuestion();
    }

    private void updateQuestion() {

        currentQuestion = questions.getQuestion(currentQuestionIndex);

        questionTextView.setText(currentQuestion.body);

        String[] answers = new String[4];
        answers[0] = currentQuestion.answer;
        for(int i=1; i<4; ++i)
            answers[i] = currentQuestion.badAnswer[i-1];
        MyUtils.<String>ShuffleArray(answers);

        for(int i=0; i<4; ++i)
            answerButtons.get(i).setText(answers[i]);
    }

    /* Dialogs and exiting */

    private void gameOver() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder
                .setMessage(getString(R.string.gameOverMsg, score))
                .setCancelable(false)
                .setPositiveButton(R.string.tryAgainLabel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                })
                .setNegativeButton(R.string.exitLabel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        AlertDialog alertDialog=alertDialogBuilder.create();
        alertDialog.show();
    }

    private void noQuestionFileFound() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder
                .setMessage(R.string.noQuestionFileFoundMsg)
                .setCancelable(false)
                .setPositiveButton(R.string.exitLabel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        AlertDialog alertDialog=alertDialogBuilder.create();
        alertDialog.show();
    }

        /* Permissions */

    private final int STORAGE_PERMISSION_CODE = 1;

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.permissionReqTitle)
                    .setMessage(R.string.permissionReqMsg)
                    .setPositiveButton(R.string.okLabel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton(R.string.cancelLabel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == STORAGE_PERMISSION_CODE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                finish();
            }
        }
    }
}
