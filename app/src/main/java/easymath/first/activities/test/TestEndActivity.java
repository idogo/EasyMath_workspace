package easymath.first.activities.test;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import easymath.first.common.Questions;
import easymath.first.common.User;
import learningmath.first.R;

/**
 * Ending screen of the test. Displays the results.
 * @author liadpc
 *
 */
public class TestEndActivity extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		
		// Handle context
		super.onCreate(savedInstanceState);
		Firebase.setAndroidContext(this);
		final Context context = this;
		
		// Handle view and background
		View view = getWindow().getDecorView();
		Drawable myIcon = getResources().getDrawable(R.drawable.sky);
		view.setBackgroundDrawable(myIcon);

		// Handle first question
		RelativeLayout parentView = new RelativeLayout(context);
		DataSnapshot questionref = Questions.QuestionsRefFortest[0];
		
		// Show score
		TextView score = new TextView(context);
		score.setId(0);
		score.setTextSize(35f);
		score.setTextColor(Color.parseColor("white"));
		score.setText(" " + "ציון:" + calculateScore(Questions.correctAnswersInTest, Questions.answersTimesInTest));
		score.setGravity(Gravity.CENTER_HORIZONTAL);
		
		// Align score text
		RelativeLayout.LayoutParams pscore = new RelativeLayout.LayoutParams(400, 200);
		pscore.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		pscore.addRule(RelativeLayout.CENTER_HORIZONTAL);
		parentView.addView(score, pscore);
		
		// Show first question + answers
		String question = (String) questionref.child("question").getValue();
		String ans1 =  questionref.child("ans1").getValue().toString();
		String ans2 =  questionref.child("ans2").getValue().toString();
		String ans3 =  questionref.child("ans3").getValue().toString();
		String ans4 =  questionref.child("ans4").getValue().toString();
		
		int studentAnswer = Questions.answersForTests[0];
		long timeTaken = Questions.answersTimesInTest[0];
		// Keep the time taken for the first question to calculate avarage time later
		double curravgtime = timeTaken;
		
		// Show and align "time taken" text
		TextView t = new TextView(context);
		t.setId(1);
		t.setTextSize(22f);
		t.setTextColor(Color.parseColor("white"));
		t.setGravity(Gravity.CENTER_HORIZONTAL);
		t.setText("\n \n" + question + " " + " (" + timeTaken + " " + "שניות" + ") ");
		RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(400, 200);
		p.addRule(RelativeLayout.BELOW , score.getId());
		p.addRule(RelativeLayout.CENTER_HORIZONTAL);
		parentView.addView(t, p);
		
		// Show and align Answer 1
		RelativeLayout.LayoutParams layAns1 = new RelativeLayout.LayoutParams
				(700,250);
		layAns1.addRule(RelativeLayout.BELOW , t.getId());
		layAns1.addRule(RelativeLayout.ALIGN_LEFT);
		Button button = new Button(context);
		button.setTextSize(22f);
		button.setText(ans1);
		button.setId(11);
		parentView.addView(button , layAns1);
		
		// Color the answer red or green of thats the chosen answer
		colorAnswer(question,ans1,studentAnswer,button);

		// Show and align Answer 2
		RelativeLayout.LayoutParams layAns2 = new RelativeLayout.LayoutParams
				(700,250);
		layAns2.addRule(RelativeLayout.ALIGN_TOP , button.getId());
		layAns2.addRule(RelativeLayout.RIGHT_OF , button.getId());
		Button button2 = new Button(context);
		button2.setTextSize(22f);
		button2.setText(ans2);
		button2.setId(21);
		parentView.addView(button2 , layAns2);
		
		// Color the answer red or green of thats the chosen answer
		colorAnswer(question,ans2,studentAnswer,button2);
		
		// Show and align Answer 3
		RelativeLayout.LayoutParams layAns3 = new RelativeLayout.LayoutParams
				(700,250);
		layAns3.addRule(RelativeLayout.BELOW , button2.getId());
		layAns3.addRule(RelativeLayout.ALIGN_LEFT);
		Button button3 = new Button(context);
		button3.setTextSize(22f);
		button3.setText(ans3);
		button3.setId(31);
		parentView.addView(button3 , layAns3);
		
		// Color the answer red or green of thats the chosen answer
		
		colorAnswer(question,ans3,studentAnswer,button3);
		// Show and align Answer 4
		RelativeLayout.LayoutParams layAns4 = new RelativeLayout.LayoutParams
				(700,250);
		layAns4.addRule(RelativeLayout.ALIGN_TOP , button3.getId());
		layAns4.addRule(RelativeLayout.RIGHT_OF , button3.getId());
		Button button4 = new Button(context);
		button4.setTextSize(22f);
		button4.setText(ans4);
		button4.setId(41);
		parentView.addView(button4 , layAns4);
		
		// Color the answer red or green of thats the chosen answer
		colorAnswer(question,ans4,studentAnswer,button4);
		
		
		// Display the rest of the questions in the same method (and align them under the first question)
		for (int i = 0; i <10 ; i++) {
			
			questionref = Questions.QuestionsRefFortest[i];
			studentAnswer = Questions.answersForTests[i];
			timeTaken = Questions.answersTimesInTest[i];
			curravgtime += timeTaken;
			
			// Get question + answers
			question = (String) questionref.child("question").getValue();
			ans1 =  questionref.child("ans1").getValue().toString();
			ans2 =  questionref.child("ans2").getValue().toString();
			ans3 =  questionref.child("ans3").getValue().toString();
			ans4 =  questionref.child("ans4").getValue().toString();
			
			// Show and align question
			t = new TextView(context);
			t.setId(i+1);
			t.setTextSize(22f);
			t.setTextColor(Color.parseColor("white"));
			t.setGravity(Gravity.CENTER_HORIZONTAL);
			t.setText("\n" + question + "\n" + " (" + timeTaken + " " + "שניות" + ") ");
			p = new RelativeLayout.LayoutParams(400, 200);
			p.addRule(RelativeLayout.BELOW , button4.getId());
			p.addRule(RelativeLayout.CENTER_HORIZONTAL);
			parentView.addView(t, p);
			
			// Show and align Answer 1
			layAns1 = new RelativeLayout.LayoutParams
					(700,250);
			layAns1.addRule(RelativeLayout.BELOW , t.getId());
			layAns1.addRule(RelativeLayout.ALIGN_LEFT);
			button = new Button(context);
			button.setTextSize(22f);
			button.setText(ans1);
			button.setId(11+i);
			parentView.addView(button , layAns1);

			// Color the answer red or green of thats the chosen answer
			colorAnswer(question,ans1,studentAnswer,button);
			
			// Show and align Answer 2
			layAns2 = new RelativeLayout.LayoutParams
					(700,250);
			layAns2.addRule(RelativeLayout.ALIGN_TOP , button.getId());
			layAns2.addRule(RelativeLayout.RIGHT_OF , button.getId());
			button2 = new Button(context);
			button2.setTextSize(22f);
			button2.setText(ans2);
			button2.setId(21+i);
			parentView.addView(button2 , layAns2);
			
			// Color the answer red or green of thats the chosen answer
			colorAnswer(question,ans2,studentAnswer,button2);
			
			// Show and align Answer 3
			layAns3 = new RelativeLayout.LayoutParams
					(700,250);
			layAns3.addRule(RelativeLayout.BELOW , button2.getId());
			layAns3.addRule(RelativeLayout.ALIGN_LEFT);
			button3 = new Button(context);
			button3.setTextSize(22f);
			button3.setText(ans3);
			button3.setId(31+i);
			parentView.addView(button3 , layAns3);
			
			// Color the answer red or green of thats the chosen answer
			colorAnswer(question,ans3,studentAnswer,button3);
			
			layAns4 = new RelativeLayout.LayoutParams
					(700,250);

			// Show and align Answer 4
			layAns4.addRule(RelativeLayout.ALIGN_TOP , button3.getId());
			layAns4.addRule(RelativeLayout.RIGHT_OF , button3.getId());
			button4 = new Button(context);
			button4.setTextSize(22f);
			button4.setText(ans4);
			button4.setId(41+i);
			parentView.addView(button4 , layAns4);
			
			// Color the answer red or green of thats the chosen answer
			colorAnswer(question,ans4,studentAnswer,button4);
		}
		
		// Calc avg time
		curravgtime = curravgtime / 10;
		final double finalAvgTime = curravgtime;
		
		// Show and align "avg time" text
		TextView tvAvg = new TextView(context);
		tvAvg.setTextSize(15f);
		tvAvg.setId(65);
		tvAvg.setTextColor(Color.parseColor("white"));
		tvAvg.setText(" " + "זמן תשובה ממוצע בשניות: " + finalAvgTime);
		tvAvg.setGravity(Gravity.CENTER_HORIZONTAL);
		RelativeLayout.LayoutParams pavg = new RelativeLayout.LayoutParams(400, 200);
		pavg.addRule(RelativeLayout.BELOW , button4.getId());
		pavg.addRule(RelativeLayout.CENTER_HORIZONTAL);
		parentView.addView(tvAvg, pavg);
		
		// Show and align the finish button
		RelativeLayout.LayoutParams layButton = new RelativeLayout.LayoutParams
				(700,400);
		layButton.addRule(RelativeLayout.BELOW , tvAvg.getId());
		layButton.addRule(RelativeLayout.CENTER_HORIZONTAL);
		Button end = new Button(context);
		end.setText("סיום");
		end.setId(51);
		end.setBackgroundResource(R.drawable.green);
		end.setTextSize(30);
		parentView.addView(end , layButton);
		
		// Calculate new levels
		calculateAddLevel();
		calculateSubLevel();

		// When pressing the finish button
		end.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
						
				// Get the new user levels
				final String addLevel = String.valueOf(Questions.addLevel);
				final String subLevel = String.valueOf(Questions.subLevel);
				
				// Fetch current user by username
				Firebase ref2 = new Firebase("https://brilliant-fire-7490.firebaseio.com/");
				Firebase studentsRef = ref2.child("users/students/");
				final Firebase userRef= studentsRef.child(User.userName);
				
				// Update the levels of the user
				userRef.addListenerForSingleValueEvent(new ValueEventListener() {
					
					@Override
					public void onDataChange(DataSnapshot snapshot) {
						
						// Set the new levels
						userRef.child("add").setValue(addLevel);
						userRef.child("sub").setValue(subLevel);
						
						// Update the Number Of Tests the user has taken (+1)
						Long newNumOfTests = Long.parseLong((String)snapshot.child("numoftests").getValue()) + 1;
						userRef.child("numoftests").setValue(String.valueOf(newNumOfTests));
						
						// Update the avg time to answer
						Double oldAvgTime = Double.parseDouble((String) snapshot.child("avgtime").getValue());
						Double newAvg = Math.floor((oldAvgTime  + finalAvgTime) / newNumOfTests * 100) / 100;
						userRef.child("avgtime").setValue(String.valueOf(newAvg));
						
						finish();	
					}
					
					@Override
					public void onCancelled(FirebaseError arg0) {
						
					}
				});
			
			}
		});
		
		
		ScrollView scroll = new ScrollView(context);
		scroll.addView(parentView);
		setContentView(scroll);
	}
	
	private void colorAnswer(String question, String trueAns, int studentAns, Button button) {
		if(Integer.parseInt(trueAns) == studentAns)
			button.getBackground().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
		if(Integer.parseInt(trueAns) == TestActivity.getAns(question))
			button.getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
	}

	/**
	 * Calculate the score beased on correct answers and answer time
	 * @param correctAnswersInTest
	 * @param answersTimesInTest
	 * @return
	 */
	private CharSequence calculateScore(boolean[] correctAnswersInTest, long[] answersTimesInTest) {
		
		int score = 0;
		
		for (int i = 0; i <10 ; i++) {
			// If correct
			if (correctAnswersInTest[i]) {
				long time = answersTimesInTest[i];
				// Full points for answering in 5 seconds or less
				if (time <= 5) {
					score+=10;
				}
				// 9/10 for answering in 6-10 seconds
				if (time >= 6 && time < 10) {
					score+=9;
				}
				// 8/10 for answering in 6-14 seconds
				if (time >= 10 && time < 14) {
					score+=8;
				}
				// 7/10 for answering in 14-18 seconds
				if (time >= 14 && time < 18) {
					score+=7;
				}
				// 6/10 for answering in 18-22 seconds
				if (time >= 18 && time < 22) {
					score+=6;
				}
				// 5/10 for answering in 22+ seconds
				if (time >= 22) {
					score+=5;
				}	
			}
		}
		return String.valueOf(score);
	}

	private void calculateAddLevel() {
		
		double sum =0;
		int correctCounter = 0;
		
		// Count correct answers and sum their coefficient
		for (int i =0 ; i<5 ; i++) {
			
			int studentAnswer = Questions.answersForTests[i];
			double coefficient = Questions.coefficient[i];
			
			DataSnapshot questionref = Questions.QuestionsRefFortest[i];
			String question = (String) questionref.child("question").getValue();
					
			
			if(studentAnswer == TestActivity.getAns(question)){
				correctCounter++;
				// *2 to Normalize the coefficient sum because maximum levels that can be gained or lost from a test are 2 levels
				sum += coefficient * 2;
			}
		}
		
		// Floor up and convert to int (Max value for levelUpFactor/levelDownFactor can be 2 if all questions are correct)
		int levelUpFactor = (int) Math.ceil(sum);
		int levelDownFactor = 2 - (int) Math.ceil(sum);
		
		// If 0 correct answers
		if(correctCounter == 0){
			
			// Punish more for level 3+
			if(Questions.addLevel > 2) {
				Questions.addLevel = Questions.addLevel-levelDownFactor;
			}
			else if (Questions.addLevel == 2) { // Can't go lower than level 1
				Questions.addLevel = (int) (Questions.addLevel-0.5 * levelDownFactor);
			}
		}
		// If 1-2 correct answers
		else if(correctCounter < 3) {
			// Punish only levels 2+
			if(Questions.addLevel > 1){
				Questions.addLevel = (int) (Questions.addLevel-0.5 * levelDownFactor);
			}
		// if 3+ correct answers
		} else {
			if(Questions.addLevel < 5) // level up for levels 1-4 (5 is already max)
				Questions.addLevel += levelUpFactor;
		}
		
		
	}
	
	
	private void calculateSubLevel() {
		double sum =0;
		int correctCounter = 0;
		
		// Count correct answers and sum their coefficient
		for (int i = 5  ; i < 10 ; i++) {
			
			int studentAnswer = Questions.answersForTests[i];
			double coefficient = Questions.coefficient[i];
			
			DataSnapshot questionref = Questions.QuestionsRefFortest[i];
			String question = (String) questionref.child("question").getValue();
					
			if(studentAnswer == TestActivity.getAns(question)){
			correctCounter++;
			// *2 to Normalize the coefficient sum because maximum levels that can be gained or lost from a test are 2 levels
			sum += coefficient * 2;
			}
		}
		
		// Floor up and convert to int (Max value for levelUpFactor/levelDownFactor can be 2 if all questions are correct)
		int levelUpFactor = (int) Math.ceil(sum);
		int levelDownFactor = 2 - (int) Math.ceil(sum);
		
		// If 0 correct answers
		if(correctCounter == 0){
			
			// Punish more for level 3+
			if(Questions.subLevel > 2) {
				Questions.subLevel = Questions.subLevel-levelDownFactor;
			}
			else if (Questions.addLevel == 2){
				// Punish half as hard
				Questions.subLevel = (int) (Questions.subLevel-0.5*levelDownFactor);
			}
		}
		
		// If 1-2 correct answers
		else if(correctCounter < 3) {
			
			// Punish only levels 2+
			if(Questions.subLevel > 1){
				Questions.subLevel = (int) (Questions.subLevel-0.5*levelUpFactor);
			}
		// if 3+ correct answers - level up
		} else { 
			if(Questions.subLevel < 5) // level up for levels 1-4 (5 is already max)
				Questions.subLevel += levelUpFactor;
		}
		
		
	}
}
