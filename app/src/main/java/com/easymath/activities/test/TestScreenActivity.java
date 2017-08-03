package com.easymath.activities.test;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import com.easymath.util.PropertiesUtil;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.easymath.common.Questions;
import com.easymath.R;

/**
 * Screen to display a test question + 4 answer buttons
 * @author liadpc
 *
 */
public class TestScreenActivity extends Activity  implements TextToSpeech.OnInitListener{

	private static final String RIGHT_ANSWER_TITLE = "test.right.answer.title";
	private static final String RIGHT_ANSWER_MSG = "test.right.answer.msg";
	private static final String WRONG_ANSWER_TITLE = "test.wrong.answer.title";
	private static final String WRONG_ANSWER_MSG = "test.wrong.answer.msg";
	private static final String FINISH_TITLE = "test.finish.title";
	private static final String FINISH_MSG1 = "test.finish.msg1";
	private static final String FINISH_MSG2 = "test.finish.msg2";

	TextToSpeech t1;

	
	 @Override
     public void onInit(int status) {
		 // Use Android TextToSpeech to speak the question
        if(status != TextToSpeech.ERROR) {
           t1.setLanguage(Locale.US);
           String soundExercise = prepareSound();
           t1.speak(soundExercise, TextToSpeech.QUEUE_FLUSH, null);
        }
     }	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Handle context
		Firebase.setAndroidContext(this);
		setContentView(R.layout.train_screen);
		final Context context = this;
		
		// Handle view and background
		View view = getWindow().getDecorView();
		Drawable myIcon = getResources().getDrawable(R.drawable.sky);
		view.setBackgroundDrawable(myIcon);
		
		// Show question
		TextView questionView = (TextView)findViewById(R.id.tv_question);
		questionView.setTextColor(Color.parseColor("white"));
		
		// Answer 1
		Button button1 = (Button)findViewById(R.id.ans1);
		button1.setBackgroundResource(R.drawable.green);
		button1.setTextSize(30);
		
		// Answer 2
		Button button2 = (Button)findViewById(R.id.ans2);
		button2.setBackgroundResource(R.drawable.green);
		button2.setTextSize(30);
		
		// Answer 3
		Button button3 = (Button)findViewById(R.id.ans3);
		button3.setBackgroundResource(R.drawable.green);
		button3.setTextSize(30);
		
		// Answer 4
		Button button4 = (Button)findViewById(R.id.ans4);
		button4.setBackgroundResource(R.drawable.green);
		button4.setTextSize(30);

		// Keep track of starting time
		final long startTime = System.currentTimeMillis();
		
		// Init TextToSpeech object
		t1 = new TextToSpeech(getApplicationContext(), this);	

		// Set text for the question and answers
		questionView.setText(Questions.question);
		questionView.setTextSize(60);
		button1.setText(Questions.ans1);
		button2.setText(Questions.ans2);
		button3.setText(Questions.ans3);
		button4.setText(Questions.ans4);
	
		// Click listener for what happens when an answer is clicked
		OnClickListener clickListener = new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// Calc time taken
				long answerTime = System.currentTimeMillis();
				long timeTakenInSeconds = TimeUnit.MILLISECONDS.toSeconds(answerTime- startTime);
				
				// True answer
				int ans = TestActivity.getAns(Questions.question);
				// Chosen answer
				Button b = (Button)v;
				int buttonText = Integer.parseInt(b.getText().toString());
				
				// Remember the answer and "time taken" for later
				Questions.answersForTests[Questions.numOfQuestion-1] = buttonText;
				Questions.answersTimesInTest[Questions.numOfQuestion-1] = timeTakenInSeconds;
				
				// If answer is right
				if(buttonText == ans) {
					// Add a point
					Questions.points ++;
					// Set true for the boolean list
					Questions.correctAnswersInTest[Questions.numOfQuestion-1] = true;
					// Speak "Correct!"
					rightAnswer(context);
				}
				else {
					// Set false for the boolean list
					Questions.correctAnswersInTest[Questions.numOfQuestion-1] = false;
					// Speak "Wrong"
					wrongAnswer(context, ans);
				}
			}

		};
		
		// Set the click listener for all of the answers
		button1.setOnClickListener(clickListener);
		button2.setOnClickListener(clickListener);
		button3.setOnClickListener(clickListener);
		button4.setOnClickListener(clickListener);
		
	}

	/**
	 * @return A textual representation of the question, ready for the TextToSpeech
	 */
	private String prepareSound() {
		String sound ="";
		// Get the question
		String question = Questions.question;
		// Split by spaces
		String[] parts = question.split("\\b");
		
		// First number can stay
		sound+= parts[1];
		
		// Replace "+" and "-" with "plus" and "minus"
		if(parts[2].equals("+")){
			sound += " plus " + parts[3];
		}
		else {
			sound += " minus " + parts[3];
		}
		
		// For long questions (3 elements)
		if(parts.length == 6){
			if(parts[4].equals("+")){
				sound += " plus " + parts[5];
			}
			else {
				sound += " minus " + parts[5];
			}
		}
		return sound;
	}

	/**
	 * Speak "Correct!"
	 */
	private void rightAnswer(final Context context) {
		try {
			t1.speak("Correct!", TextToSpeech.QUEUE_FLUSH, null);
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					context);
			alertDialogBuilder.setTitle(RIGHT_ANSWER_TITLE);
			alertDialogBuilder.setMessage(RIGHT_ANSWER_MSG).setCancelable(false).setPositiveButton(PropertiesUtil.getOkMessage(), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
					prepareNextQuestion(context);

				}
			});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Speak "Wrong!"
	 */
	private void wrongAnswer(final Context context , int ans) {
		try {
			t1.speak("Wrong!", TextToSpeech.QUEUE_FLUSH, null);
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
			alertDialogBuilder.setTitle(WRONG_ANSWER_TITLE);
			alertDialogBuilder.setMessage(WRONG_ANSWER_MSG).setCancelable(false).setPositiveButton(PropertiesUtil.getOkMessage(), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
					prepareNextQuestion(context);

				}
			});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Prepare the next question in the Questions stracture
	 * @param context
	 */
	private void prepareNextQuestion(Context context) {
		// If this was the last question, then go to textEnd
		if( Questions.numOfQuestion == 10) {
			testEnd(context);
		}
		else { 
			// Fetch next question
			DataSnapshot questionref = Questions.QuestionsRefFortest[Questions.numOfQuestion];
			Questions.numOfQuestion++;
			String question = (String) questionref.child("question").getValue();
			
			// Fetch answers
			String ans1 =  questionref.child("ans1").getValue().toString();
			String ans2 =  questionref.child("ans2").getValue().toString();
			String ans3 =  questionref.child("ans3").getValue().toString();
			String ans4 =  questionref.child("ans4").getValue().toString();
			
			// Set the question and the answers for display later
			Questions.question = question;
			Questions.ans1 = ans1;
			Questions.ans2 = ans2;
			Questions.ans3 = ans3;
			Questions.ans4 = ans4;
			Questions.numOfTry = 1;
			Questions.ans = TestActivity.getAns(Questions.question);
			
			// Redirect to another TestScreenActivity for the next question
			Intent myIntent= null;
			myIntent = new Intent(TestScreenActivity.this, com.easymath.activities.test.TestScreenActivity.class);
			TestScreenActivity.this.startActivity(myIntent);
			finish();

		}
	}
	
	/**
	 * Alerts the number of correct answers and redirects to TestEndActivity
	 */
	private void testEnd(final Context context) {
		try {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
			alertDialogBuilder.setTitle(FINISH_TITLE);
			alertDialogBuilder.setMessage(FINISH_MSG1 + Questions.points + FINISH_MSG2).setCancelable(false).setPositiveButton(PropertiesUtil.getOkMessage(), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// Redirect to TestEndActivity
					Intent myIntent = null;
					myIntent = new Intent(TestScreenActivity.this, com.easymath.activities.test.TestEndActivity.class);
					TestScreenActivity.this.startActivity(myIntent);
					finish();
				}
			});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

}
