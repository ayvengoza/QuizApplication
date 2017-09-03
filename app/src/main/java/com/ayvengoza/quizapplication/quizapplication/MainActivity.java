package com.ayvengoza.quizapplication.quizapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getCanonicalName();
    private static final String ON_CREATE = "onCreate(Bundle) called";
    private static final String ON_START = "onStart() called";
    private static final String ON_RESUME = "onResume() called";
    private static final String ON_PAUSE = "onPause() called";
    private static final String ON_RESTART = "onRestart() called";
    private static final String ON_STOP = "onStop() called";
    private static final String ON_DESTROY = "onDestroy() called";

    private TextView mQuestuinTextView;
    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextImageButton;
    private ImageButton mPrevImageButton;

    private Question[] mQuestions = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };

    private int mCurrentIndex = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, ON_CREATE);
        setContentView(R.layout.activity_main);

        //Bind Views
        mQuestuinTextView = (TextView)findViewById(R.id.question_text_view);
        mTrueButton = (Button)findViewById(R.id.true_button);
        mFalseButton = (Button) findViewById(R.id.false_button);
        mNextImageButton = (ImageButton) findViewById(R.id.next_image_button);
        mPrevImageButton = (ImageButton) findViewById(R.id.prev_image_button);

        updateQuestion();

        mQuestuinTextView.setOnClickListener(new View.OnClickListener() {
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

        mNextImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNextQuestion();
            }
        });

        mPrevImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePrevQuestion();
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
        mQuestuinTextView.setText(question);
    }

    private void checkAnswer(boolean usetPressed){
        boolean answerTrue = mQuestions[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;
        if(usetPressed == answerTrue){
            messageResId = R.string.correct_toast;
        } else {
            messageResId = R.string.incorrect_toast;
        }
        Toast.makeText(MainActivity.this, messageResId, Toast.LENGTH_SHORT).show();
    }
}
