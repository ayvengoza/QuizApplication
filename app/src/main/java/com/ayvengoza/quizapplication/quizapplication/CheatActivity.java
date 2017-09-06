package com.ayvengoza.quizapplication.quizapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {
    private static final String EXTRA_ANSWER_IS_TRUE =
            "com.ayvengoza.quizapplication.quizapplication.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN =
            "com.ayvengoza.quizapplication.quizapplication.answer_shown";

    private static final String KEY_IS_SHOWN = "is_answer_shown";

    private static int numOfHints = 3;

    private boolean mAnswerIsTrue;
    private boolean mIsAnswerShown;

    private TextView mAnswerTextView;
    private Button mShowAnswerButton;
    private TextView mVersionTextView;
    private TextView mNumHintsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        if(savedInstanceState != null){
            mIsAnswerShown = savedInstanceState.getBoolean(KEY_IS_SHOWN);
        }

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);
        mShowAnswerButton = (Button) findViewById(R.id.show_answer_button);
        mVersionTextView = (TextView) findViewById(R.id.version_text_view);
        mNumHintsTextView = (TextView) findViewById(R.id.num_himts_text_view);

        int version = Build.VERSION.SDK_INT;

        mVersionTextView.setText("API Level " + version);
        updateHintConter();

        if(mIsAnswerShown){
            mShowAnswerButton.setVisibility(View.INVISIBLE);
            checkShowCheat();
        }

        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numOfHints > 0) {
                    numOfHints--;
                    mIsAnswerShown = true;
                    checkShowCheat();
                    setAnswerShownResult();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        int cx = mShowAnswerButton.getWidth() / 2;
                        int cy = mShowAnswerButton.getHeight() / 2;
                        float radius = mShowAnswerButton.getWidth();
                        Animator anim = ViewAnimationUtils
                                .createCircularReveal(mShowAnswerButton, cx, cy, radius, 0);
                        anim.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                mShowAnswerButton.setVisibility(View.INVISIBLE);
                            }
                        });
                        anim.start();
                    } else {
                        mShowAnswerButton.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        setAnswerShownResult();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_IS_SHOWN, mIsAnswerShown);
    }

    private void checkShowCheat(){
        if(mIsAnswerShown) {
            if (mAnswerIsTrue) {
                mAnswerTextView.setText(R.string.true_button);
            } else {
                mAnswerTextView.setText(R.string.false_button);
            }
        }
        updateHintConter();
    }

    private void updateHintConter(){
        mNumHintsTextView.setText("You have " + numOfHints + " hints");
    }

    private void setAnswerShownResult(){
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, mIsAnswerShown);
        setResult(RESULT_OK, data);
    }

    public static Intent newIntent(Context packageContext, boolean answerIsTrue){
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }

    public static boolean wasAnswerShown(Intent result){
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }
}
