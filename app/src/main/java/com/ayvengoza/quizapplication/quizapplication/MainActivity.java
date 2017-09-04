package com.ayvengoza.quizapplication.quizapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getCanonicalName();
    //Logging messages
    private static final String ON_CREATE = "onCreate(Bundle) called";
    private static final String ON_START = "onStart() called";
    private static final String ON_RESUME = "onResume() called";
    private static final String ON_PAUSE = "onPause() called";
    private static final String ON_RESTART = "onRestart() called";
    private static final String ON_STOP = "onStop() called";
    private static final String ON_DESTROY = "onDestroy() called";
    private static final String SAVE_INSTANCE_STATE = "onSaveInstanceState";

    //Saving keys
    private static final String KEY_INDEX = "index";
    private static final String KEY_ANSWER_STATUS = "answerStatus";
    private static final String KEY_RESTART = "restart";

    private TextView mQuestionTextView;
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mPrevButton;
    private Button mRestartButton;

    private final Question[] mQuestions = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };

    private static final int NO_ANSWER = 0;
    private static final int CORRECT_ANSWER = 1;
    private static final int WRONG_ANSWER = 2;
    private int mCurrentIndex;
    private int[] mAnswerStatus;
    private boolean mRestartModeOn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, ON_CREATE);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX);
            mAnswerStatus = savedInstanceState.getIntArray(KEY_ANSWER_STATUS);
            mRestartModeOn = savedInstanceState.getBoolean(KEY_RESTART);
        }
        else{
            mCurrentIndex = 0;
            mAnswerStatus = new int[mQuestions.length];
            mRestartModeOn = false;
        }

        //Bind Views
        mQuestionTextView = (TextView)findViewById(R.id.question_text_view);
        mTrueButton = (Button)findViewById(R.id.true_button);
        mFalseButton = (Button) findViewById(R.id.false_button);
        mNextButton = (Button) findViewById(R.id.next_button);
        mPrevButton = (Button) findViewById(R.id.prev_button);
        mRestartButton = (Button) findViewById(R.id.restart_quiz_button);

        updateQuestion();
        setMode();

        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNextQuestion();
            }
        });

        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNextQuestion();
            }
        });

        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePrevQuestion();
            }
        });

        mRestartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetAnsProgress();
                mCurrentIndex = 0;
                updateQuestion();
                setMode();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, ON_START);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, ON_RESUME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, ON_PAUSE);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, ON_RESTART);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, SAVE_INSTANCE_STATE);
        outState.putInt(KEY_INDEX, mCurrentIndex);
        outState.putIntArray(KEY_ANSWER_STATUS, mAnswerStatus);
        outState.putBoolean(KEY_RESTART, mRestartModeOn);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, ON_STOP);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, ON_DESTROY);
    }

    private void updateNextQuestion(){
        mCurrentIndex = (mCurrentIndex + 1) % mQuestions.length;
        updateQuestion();

    }

    private void updatePrevQuestion(){
        mCurrentIndex = (mCurrentIndex > 0) ? (mCurrentIndex - 1) % mQuestions.length :
                mQuestions.length - 1;
        updateQuestion();
    }

    private void updateQuestion(){
        int question = mQuestions[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
        setEnabledAnswer();
    }

    private void checkAnswer(boolean userPressed){
        boolean answerTrue = mQuestions[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;
        if(userPressed == answerTrue){
            messageResId = R.string.correct_toast;
            mAnswerStatus[mCurrentIndex] = CORRECT_ANSWER;
        } else {
            messageResId = R.string.incorrect_toast;
            mAnswerStatus[mCurrentIndex] = WRONG_ANSWER;
        }
        setEnabledAnswer();

        Toast.makeText(MainActivity.this, messageResId, Toast.LENGTH_SHORT).show();
        finishChecker();
        setMode();
    }

    private void setEnabledAnswer(){
        switch (mAnswerStatus[mCurrentIndex]){
            case NO_ANSWER:
                mTrueButton.setEnabled(true);
                mFalseButton.setEnabled(true);
                break;
            case CORRECT_ANSWER:
            case WRONG_ANSWER:
                mTrueButton.setEnabled(false);
                mFalseButton.setEnabled(false);
        }
    }

    private void setMode(){
        if(mRestartModeOn){
            runRestartMode();
        } else {
            runUsualMode();
        }
    }

    private void runUsualMode(){
        mRestartButton.setVisibility(View.INVISIBLE);
        mNextButton.setVisibility(View.VISIBLE);
        mPrevButton.setVisibility(View.VISIBLE);
    }

    private void runRestartMode(){
        mRestartButton.setVisibility(View.VISIBLE);
        mNextButton.setVisibility(View.INVISIBLE);
        mPrevButton.setVisibility(View.INVISIBLE);
    }

    private int getNumOfAnswers(int answerType){
        int counter = 0;
        for(int i : mAnswerStatus)
            if(i == answerType)
                counter++;
        return counter;
    }

    private void resetAnsProgress(){
        for(int i =0; i<mAnswerStatus.length; i++)
            mAnswerStatus[i] = NO_ANSWER;
        mRestartModeOn = false;
    }

    private void finishChecker(){
        if(getNumOfAnswers(NO_ANSWER) == 0){
            int correctAns = getNumOfAnswers(CORRECT_ANSWER);
            int allQuest = mQuestions.length;
            String msg = "Correct " + correctAns + " of " + allQuest + ".";
            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
            mRestartModeOn = true;
        }
    }
}
