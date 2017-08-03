package com.easymath.activities.train;

import java.util.Locale;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import android.annotation.SuppressLint;
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
import com.easymath.activities.test.TestActivity;
import com.easymath.common.Questions;
import com.easymath.R;

/**
 *  Screen to display a training question + 4 answer buttons
 * @author liadpc
 */
public class TrainScreenActivity extends Activity  implements TextToSpeech.OnInitListener{


	int questions [] = new int[5];
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
	
	@SuppressLint("NewApi")
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
		
		// Init TextToSpeech object
		t1 = new TextToSpeech(getApplicationContext(), this);						

		questionView.setText(Questions.question);
		questionView.setTextSize(60);

		// Set text for the question and answers
		button1.setText(Questions.ans1);
		button2.setText(Questions.ans2);
		button3.setText(Questions.ans3);
		button4.setText(Questions.ans4);

		// Click listener for what happens when an answer is clicked
		OnClickListener clickListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// True answer
				int ans = TestActivity.getAns(Questions.question);
				// Chosen answer
				Button b = (Button)v;
				int buttonText = Integer.parseInt(b.getText().toString());
				// If answer is right
				if(buttonText == ans) {
					Questions.numOfQuestion++;
					// Add a point
					Questions.points ++;
					// Speak "Correct!"
					rightAnswer(context);

				}
				else {
					// Speak "Wrong" and ask if explanation is needed
					wrongAnswer(context, ans);
				}

			}

		};
		button1.setOnClickListener(clickListener);
		button2.setOnClickListener(clickListener);
		button3.setOnClickListener(clickListener);
		button4.setOnClickListener(clickListener);
		

	}

	/**
	 * Show alert with number of right answers
	 * @param context
	 */
	private void trainEnd(final Context context) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);
		alertDialogBuilder.setTitle("סוף תרגול");
		alertDialogBuilder.setMessage(" היו לך " + Questions.points +" תשובות נכונות ").setCancelable(false).setPositiveButton("אישור",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				finish();
			}
		});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}


	/**
	 * Speak "Wrong!" and redirect to question explanation
	 * @param context
	 * @param ans
	 */
	private void wrongAnswer(final Context context, int ans) {
		// Speak
		t1.speak("Wrong!", TextToSpeech.QUEUE_FLUSH, null);
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setTitle("תשובה שגויה");
		alertDialogBuilder.setMessage("נסה שוב או קבל הסבר").setCancelable(false).setPositiveButton("נסה שוב",new DialogInterface.OnClickListener() {
			// Positive Button acts as "try again"
			public void onClick(DialogInterface dialog,int id) {
				// Just close the alert to try again
				Questions.numOfTry ++;
				dialog.cancel();
			}
		}).setNegativeButton("הסבר על השאלה", new DialogInterface.OnClickListener() {
			// Negative button acts as "Get explanation"
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Redirect to Question Explanation Activity
				Intent myIntent= null;
				myIntent = new Intent(TrainScreenActivity.this, com.easymath.activities.QuestionExplanationActivity.class);
				TrainScreenActivity.this.startActivity(myIntent);
				
				// When done with explaination
				Questions.numOfQuestion++;
				// Alert for going to the next question
				AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(context);
				alertDialogBuilder2.setTitle("שאלה הבאה");
				alertDialogBuilder2.setMessage("מעבר לשאלה הבאה").setCancelable(false).setPositiveButton("אישור",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
						// Prepare next question
						prepareNextQuestion(context);

					}
				});
				AlertDialog alertDialog2 = alertDialogBuilder2.create();
				alertDialog2.show();

			}
		});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	/**
	 * Speak "Correct!"
	 * @param context
	 */
	private void rightAnswer(final Context context) {
		// Speak
		t1.speak("Correct!", TextToSpeech.QUEUE_FLUSH, null);
		// Alert
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setTitle("תשובה נכונה!");
		alertDialogBuilder.setMessage("כל הכבוד!").setCancelable(false).setPositiveButton("אישור",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
				prepareNextQuestion(context);
			}
		});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}


	/**
	 * 
	 * @param context
	 */
	private void prepareNextQuestion(Context context) {
		// If this was the last question, go to trainEnd
		if( Questions.numOfQuestion == 6) {
			trainEnd(context);
		}
		else {
			// Get the question's DB reference
			Firebase ref = Questions.ref;
			Firebase ref2 = ref.child(Questions.questionNumbers[Questions.numOfQuestion-1]);

			ref2.addListenerForSingleValueEvent(new ValueEventListener() {
				@Override
				public void onDataChange(DataSnapshot snapshot) {
					
					// Fetch next question
					String question = (String) snapshot.child("question").getValue();
					
					// Fetch answers
					String ans1 =  snapshot.child("ans1").getValue().toString();
					String ans2 =  snapshot.child("ans2").getValue().toString();
					String ans3 =  snapshot.child("ans3").getValue().toString();
					String ans4 =  snapshot.child("ans4").getValue().toString();
					
					// Set the question and the answers for display later
					Questions.question = question;
					Questions.ans1 = ans1;
					Questions.ans2 = ans2;
					Questions.ans3 = ans3;
					Questions.ans4 = ans4;
					Questions.numOfTry = 1;
					Questions.ans = TestActivity.getAns(Questions.question);
					
					// Redirect to another TrainScreenActivity for the next question
					Intent myIntent= null;
					myIntent = new Intent(TrainScreenActivity.this, com.easymath.activities.train.TrainScreenActivity.class);
					TrainScreenActivity.this.startActivity(myIntent);
					finish();
				}


				@Override
				public void onCancelled(FirebaseError arg0) {
				}

			});
		}
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
}