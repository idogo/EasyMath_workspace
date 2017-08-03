package easymath.first.activities;

import java.util.ArrayList;
import java.util.Collections;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import easymath.first.activities.test.TestActivity;
import easymath.first.common.Questions;
import easymath.first.util.FirebaseDBUtils;
import learningmath.first.R;

/**
 * Activity to choose difficulty (basic / up to 20 / 3 elements).
 * This screen can be used from the "Add Exercise" option for teachers or "Train" / "Learn" options for students.
 * We can know which activity directed us here by using the arguments.
 * @author liadpc
 *
 */
@SuppressLint("UseValueOf")
public class DifficultyActivity extends Activity {
	
	String nextArg;
	
	// Actions enum
	private enum Action {
	    LEARN_ADD, LEARN_SUB, LEARN_ADDSUB ,
	    TRAIN_ADD, TRAIN_SUB, TRAIN_ADDSUB ,
	    ADD_EX, SUB_EX, ADDSUB_EX }
	
	// Subjects enum
	private enum TrainSubject {
		ADD_BASIC, ADD_UPTO20, ADD_3ELEMENTS, SUB_BASIC, SUB_UPTO20, SUB_3ELEMENTS, ADDSUB_BASIC}	
	
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.difficulty_main);

		// Handle view and background
		View view = getWindow().getDecorView();
		Drawable myIcon = getResources().getDrawable(R.drawable.sky);
		view.setBackgroundDrawable(myIcon);
		
		// Read argument
		String passedArg = getIntent().getExtras().getString("arg");
		
		// "Basic" Button
		Button basic = (Button)findViewById(R.id.btbasic);
		basic.setBackgroundResource(R.drawable.green);
		basic.setTextSize(30);
		
		// "Up to 20" Button
		Button upTo20 = (Button)findViewById(R.id.btupto20);
		upTo20.setBackgroundResource(R.drawable.green);
		upTo20.setTextSize(30);
		
		// "3 Elements" Button
		Button threeElements = (Button)findViewById(R.id.bt3elements);
		threeElements.setBackgroundResource(R.drawable.green);
		threeElements.setTextSize(20);
		
		// For each of the actions, display the text on the subjects buttons
		// Learn action redirects to videos
		// Train action prepares questions and redirects to TrainScreenActivity
		// Add Exercise action redirects to AddExScreenActivity
		switch (Action.valueOf(passedArg)) {
			case LEARN_ADD : 
				basic.setText("חיבור בסיסי");
				upTo20.setText("חיבור עד 20");
				threeElements.setText("חיבור של שלושה גורמים");
				basic.setOnClickListener(new View.OnClickListener() {
	
					@Override
					public void onClick(View v) {
						Intent myIntent= new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.youtube.com/watch?v=boYXiNVtm50"));
						startActivity(myIntent);
					}
				});
				upTo20.setOnClickListener(new View.OnClickListener() {
	
					@Override
					public void onClick(View v) {
						Intent myIntent= new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.youtube.com/watch?v=5ALDBbWKGjc"));
						startActivity(myIntent);
					}
				});
				
				threeElements.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent myIntent= new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.youtube.com/watch?v=nJ0a9CbjKnM"));
						startActivity(myIntent);
					}
				});
	
					
				break;
			case LEARN_SUB :	
				basic.setText("חיסור בסיסי");
				upTo20.setText("חיסור עד 20");
				threeElements.setText("חיסור של שלושה גורמים");
				basic.setOnClickListener(new View.OnClickListener() {
	
					@Override
					public void onClick(View v) {
						Intent myIntent= new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.youtube.com/watch?v=ghzikfMix20"));
						startActivity(myIntent);
					}
				});
				upTo20.setOnClickListener(new View.OnClickListener() {
	
					@Override
					public void onClick(View v) {
						Intent myIntent= new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.youtube.com/watch?v=KBFzM53J3XM"));
						startActivity(myIntent);
					}
				});
				
				threeElements.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent myIntent= new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.youtube.com/watch?v=pOhLpgoJVSA"));
						startActivity(myIntent);
					}
				});
				break;			
			case TRAIN_ADD:
				basic.setText("חיבור בסיסי");
				upTo20.setText("חיבור עד 20");
				threeElements.setText("חיבור של שלושה גורמים");
				basic.setOnClickListener(new View.OnClickListener() {
	
					@Override
					public void onClick(View v) {
						
						nextArg = "ADD_BASIC";
						prepareQuestions(nextArg);
						
					}
	
	
				});
				upTo20.setOnClickListener(new View.OnClickListener() {
	
					@Override
					public void onClick(View v) {
						nextArg = "ADD_UPTO20";
						prepareQuestions(nextArg);
					}
				});
				
				threeElements.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						nextArg = "ADD_3ELEMENTS";
						prepareQuestions(nextArg);
					}
				});
				break;
			case TRAIN_SUB:
				basic.setText("חיסור בסיסי");
				upTo20.setText("חיסור עד 20");
				threeElements.setText("חיסור של שלושה גורמים");
				basic.setOnClickListener(new View.OnClickListener() {
	
					@Override
					public void onClick(View v) {
						nextArg = "SUB_BASIC";
						prepareQuestions(nextArg);
					}
				});
				upTo20.setOnClickListener(new View.OnClickListener() {
	
					@Override
					public void onClick(View v) {
						nextArg = "SUB_UPTO20";
						prepareQuestions(nextArg);
					}
				});
				
				threeElements.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						nextArg = "SUB_3ELEMENTS";
						prepareQuestions(nextArg);
					}
				});
				break;
			
			case TRAIN_ADDSUB:
				basic.setVisibility(View.GONE);
				upTo20.setVisibility(View.GONE);
				threeElements.setVisibility(View.GONE);
				nextArg = "ADDSUB_BASIC";
				prepareQuestions(nextArg);
				break;
				
			case ADD_EX:
				
				basic.setText("חיבור בסיסי");
				upTo20.setText("חיבור עד 20");
				threeElements.setText("חיבור של שלושה גורמים");
				basic.setOnClickListener(new View.OnClickListener() {
	
					@Override
					public void onClick(View v) {
						nextArg = "ADD_BASIC";
						Intent myIntent = new Intent(DifficultyActivity.this, easymath.first.activities.AddExScreenActivity.class);
						myIntent.putExtra("nextArg",  getText());
						DifficultyActivity.this.startActivity(myIntent);
					}
				});
				upTo20.setOnClickListener(new View.OnClickListener() {
	
					@Override
					public void onClick(View v) {
						nextArg = "ADD_UPTO20";
						Intent myIntent = new Intent(DifficultyActivity.this, easymath.first.activities.AddExScreenActivity.class);
						myIntent.putExtra("nextArg",  getText());
						DifficultyActivity.this.startActivity(myIntent);
					}
				});
				
				threeElements.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						nextArg = "ADD_3ELEMENTS";
						Intent myIntent = new Intent(DifficultyActivity.this, easymath.first.activities.AddExScreenActivity.class);
						myIntent.putExtra("nextArg",  getText());
						DifficultyActivity.this.startActivity(myIntent);
					}
				});
	
				break;
			case SUB_EX:
				
				basic.setText("חיסור בסיסי");
				upTo20.setText("חיסור עד 20");
				threeElements.setText("חיסור של שלושה גורמים");
				basic.setOnClickListener(new View.OnClickListener() {
	
					@Override
					public void onClick(View v) {
						nextArg = "SUB_BASIC";
						Intent myIntent = new Intent(DifficultyActivity.this, easymath.first.activities.AddExScreenActivity.class);
						myIntent.putExtra("nextArg",  getText());
						DifficultyActivity.this.startActivity(myIntent);
					}
				});
				upTo20.setOnClickListener(new View.OnClickListener() {
	
					@Override
					public void onClick(View v) {
						nextArg = "SUB_UPTO20";
						Intent myIntent = new Intent(DifficultyActivity.this, easymath.first.activities.AddExScreenActivity.class);
						myIntent.putExtra("nextArg",  getText());
						DifficultyActivity.this.startActivity(myIntent);
					}
				});
				
				threeElements.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						nextArg = "SUB_3ELEMENTS";
						Intent myIntent = new Intent(DifficultyActivity.this, easymath.first.activities.AddExScreenActivity.class);
						myIntent.putExtra("nextArg",  getText());
						DifficultyActivity.this.startActivity(myIntent);
					}
				});
				break;
	
			default:
				break;	
			}
	}
	
	/**
	 * Prepare Train questions and redirect to TrainScreenActivity
	 * @param nextArg
	 */
	public void prepareQuestions(String nextArg) {
		
		// Access DB with connection string
		Firebase myFirebaseRef2 = new Firebase(FirebaseDBUtils.getDatabaseURL());
		// Fetch questions
		Firebase ref = myFirebaseRef2.child("questions");
		
		// Fetch questions in the needed subject (from argument)
		switch (TrainSubject.valueOf(nextArg)) {
			case ADD_BASIC : 
				ref=ref.child("add/basic");
				Questions.subject = "add";
				break;
			case ADD_UPTO20 :
				ref=ref.child("add/upto20");
				Questions.subject = "add";
				break;
			case ADD_3ELEMENTS:
				ref=ref.child("add/3elements");
				Questions.subject = "add3";
				break;
			case SUB_BASIC : 
				ref=ref.child("sub/basic");
				Questions.subject = "sub";
				break;
			case SUB_UPTO20 :
				ref=ref.child("sub/upto20");
				Questions.subject = "sub";
				break;
			case SUB_3ELEMENTS:
				ref=ref.child("sub/3elements");
				Questions.subject = "sub3";
				break;		
			case ADDSUB_BASIC:
				ref=ref.child("addsub/basic");
				Questions.subject = "addSub";
				break;	
		}	
		
		// Save the DB reference for later use in the TrainScreenActivity
		Questions.ref = ref;
		// Init number of tries
		Questions.numOfTry =1;
		
		// Receive events
		ref.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot snapshot) {
				// List all the questions fetched before
				ArrayList <Integer> list = new ArrayList<Integer>();
				for (int i=1; i<snapshot.getChildrenCount()+1; i++) {
					list.add(new Integer(i));
				}
				// Shuffle the list
				Collections.shuffle(list);
				
				// Prepare the training questions
				Questions.questionNumbers[0] = list.get(0).toString();
				Questions.questionNumbers[1] = list.get(1).toString();
				Questions.questionNumbers[2] = list.get(2).toString();
				Questions.questionNumbers[3] = list.get(3).toString();
				Questions.questionNumbers[4] = list.get(4).toString();
				Questions.numOfQuestion=1;
				
				// Prepare the first question				
				String question = (String) snapshot.child(list.get(0).toString() +"/question").getValue();
				// Prepare answers
				String ans1 =  snapshot.child(list.get(0).toString() + "/ans1").getValue().toString();
				String ans2 =  snapshot.child(list.get(0).toString() + "/ans2").getValue().toString();
				String ans3 =  snapshot.child(list.get(0).toString() + "/ans3").getValue().toString();
				String ans4 =  snapshot.child(list.get(0).toString() + "/ans4").getValue().toString();
				Questions.question = question;
				Questions.points=0;
				Questions.ans1 = ans1;
				Questions.ans2 = ans2;
				Questions.ans3 = ans3;
				Questions.ans4 = ans4;
				Questions.ans = TestActivity.getAns(Questions.question);
				
				// Redirect to TrainScreenActivity
				Intent myIntent= null;
				myIntent = new Intent(DifficultyActivity.this, easymath.first.activities.train.TrainScreenActivity.class);
				DifficultyActivity.this.startActivity(myIntent);
				finish();
			}
			@Override
			public void onCancelled(FirebaseError arg0) {
			}
		});
	}
	
	public String getText() {
		return nextArg;
	}
}
