package com.github.dnrklein.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;



public class QuizActivity extends Activity {

    //added 3 variables to hold the buttons
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mCheatButton;

    //the textview
    private TextView mQuestionTextView;

    //private static final String TAG = "QuizActivity";

    //array to hold all the questions defined in strings.xml
    //it is an array of TrueFalse objects
    private TrueFalse[] mQuestionBank = new TrueFalse[] {
            new TrueFalse(R.string.question_oceans, true),
            new TrueFalse(R.string.question_mideast, false),
            new TrueFalse(R.string.question_africa, false),
            new TrueFalse(R.string.question_americas, true),
            new TrueFalse(R.string.question_asia, true)
    };

    private boolean[] hasCheated = new boolean[] {false, false, false, false, false};

    //This variable is to keep track of which question should be asked
    //It is the index of the array of questions
    private int mCurrentIndex = 0;

    //final for onSave...
    //this keeps track of the current index even when screen is rotated
    private static final String KEY_INDEX = "index";

    //this keeps track of whether the user cheated
    private static final String KEY_IS_CHEATER = "isCheater";

    //this method updates the question and sets it in the textview
    private void updateQuestion(){
        int question = mQuestionBank[mCurrentIndex].getQuestion();
        mQuestionTextView.setText(question);
    }

    //this method is to check the answer and give a toast telling the user if answer was correct
    //it takes a boolean to see if user pressed true or false button
    private void checkAnswer(boolean userPressedTrue){
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isTrueQuestion();

        int messageResId;

        if(hasCheated[mCurrentIndex]){
            messageResId = R.string.judgment_toast;
        } else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        //Setting question in textview using getQuestion() from the TrueFalse class
        //it gets the question through using the id's
        mQuestionTextView = (TextView)findViewById(R.id.question_text_view);

        //trueButton and its listener
        mTrueButton = (Button)findViewById(R.id.true_button);
        //adding a listener that does something 'onClick' and you must override its method onClick --> what happens when the button is clicked
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(true);
            }
        });

        //listener for the false_button, same as for true_button
        mFalseButton = (Button)findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(false);
            }
        });

        //listener for the next button
        mNextButton = (Button)findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        //checking if the saved state isnt null, if so get the index of the current question
        //is only not null if something in configuration has changed, for example screen rotation
        if(savedInstanceState != null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            hasCheated[mCurrentIndex] = savedInstanceState.getBoolean(KEY_IS_CHEATER);
        }

        //creating an "explicit" intent
        mCheatButton = (Button)findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cheatIntent = new Intent(QuizActivity.this, CheatActivity.class);
                boolean  answerIsTrue = mQuestionBank[mCurrentIndex].isTrueQuestion();
                cheatIntent.putExtra(CheatActivity.EXTRA_ANSWER_IS_TRUE, answerIsTrue);
                startActivityForResult(cheatIntent, 0);
            }
        });

        //initial setting of the question
        updateQuestion();
    }

    //overriding to make sure question keeps the same on screen rotation
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putBoolean(KEY_IS_CHEATER, hasCheated[mCurrentIndex]);
    }

    //the rest of the activity methods
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.quiz, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data == null){
            return;
        }
        hasCheated[mCurrentIndex] = data.getBooleanExtra(CheatActivity.EXTRA_ANSWER_SHOWN, false);
    }
}
