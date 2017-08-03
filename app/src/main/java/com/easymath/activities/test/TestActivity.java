package com.easymath.activities.test;

import java.util.ArrayList;
import java.util.Collections;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import com.easymath.common.Questions;
import com.easymath.common.User;
import com.easymath.util.FirebaseDBUtils;
import com.easymath.R;

/**
 * Intro screen before the test. This activity prepares the test and the questions.
 * @author liadpc
 */
@SuppressLint("UseValueOf")
public class TestActivity extends Activity{

	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Handle view and background
		View view = getWindow().getDecorView();
		Drawable myIcon = getResources().getDrawable(R.drawable.sky);
		view.setBackgroundDrawable(myIcon);
		
		// Create an Alert with message and button
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("מבחן");
		alertDialogBuilder.setMessage("עליך לענות כעת על 10 שאלות. בהצלחה!").setCancelable(false)
		.setPositiveButton("אישור",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// After accepting the message, close the dialog and prepare the test
				dialog.cancel();
				prepareTest();
			}
		});
		
		// Show the alert
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();	
	}

	private void prepareTest() {
		
		// DB connection to Firebase with connection string
		Firebase myFirebaseRef2 = new Firebase(FirebaseDBUtils.getDatabaseURL());
		// Fetch the Student object with the current username
		Firebase ref = myFirebaseRef2.child("users/students/");
		Firebase ref2= ref.child(User.userName);
		
		// Receive events about data changes
		ref2.addListenerForSingleValueEvent(new ValueEventListener() {
			
			@Override
			public void onDataChange(DataSnapshot snapShot) {
				// Fetch the Student's ADD and SUB levels
				int addLevel = Integer.parseInt(snapShot.child("add").getValue().toString());
				int subLevel = Integer.parseInt(snapShot.child("sub").getValue().toString());
				// Prepare the levels in the Questions structure
				Questions.addLevel = addLevel;
				Questions.subLevel = subLevel;
				// Get questions matching the levels for each subject
				getQuestions(addLevel , subLevel);
				
			}
			
			/**
			 * Prepare random questions from the DB
			 * @param addLevel
			 * @param subLevel
			 */
			private void getQuestions(final double addLevel ,final double subLevel) {
				
					// DB connection to Firebase with connection string
					Firebase ref = new Firebase(FirebaseDBUtils.getDatabaseURL());
					// Fetch the Questions objects
					Firebase ref2 = ref.child("questions");
					
					// Receive events about data changes
					ref2.addListenerForSingleValueEvent(new ValueEventListener() {	
					@Override
					public void onDataChange(DataSnapshot snapshot) {
						
						// Determine the number of questions in each difficulty based on the level
						int numOfEasyAdd = 0 , numOfMediumAdd = 0 , numOfHardAdd = 0;
						if (addLevel == 0) {
							// Level 0
							numOfEasyAdd = 3;
							numOfMediumAdd =2;
							numOfHardAdd = 0;
							Questions.coefficient[0] = 0.1;
							Questions.coefficient[1] = 0.1;
							Questions.coefficient[2] = 0.1;
							Questions.coefficient[3] = 0.35;
							Questions.coefficient[4] = 0.35;
						} else if (addLevel == 1) {
							// Level 1
							numOfEasyAdd = 3;
							numOfMediumAdd =1;
							numOfHardAdd = 1;
							Questions.coefficient[0] = 0.1;
							Questions.coefficient[1] = 0.1;
							Questions.coefficient[2] = 0.1;
							Questions.coefficient[3] = 0.3;
							Questions.coefficient[4] = 0.4;
						} else if (addLevel == 2) {
							// Level 2
							numOfEasyAdd = 2;
							numOfMediumAdd =2;
							numOfHardAdd = 1;
							Questions.coefficient[0] = 0.1;
							Questions.coefficient[1] = 0.1;
							Questions.coefficient[2] = 0.2;
							Questions.coefficient[3] = 0.2;
							Questions.coefficient[4] = 0.4;
						} else if (addLevel == 3) {
							// Level 3
							numOfEasyAdd = 1;
							numOfMediumAdd =2;
							numOfHardAdd = 2;
							Questions.coefficient[0] = 0.1;
							Questions.coefficient[1] = 0.15;
							Questions.coefficient[2] = 0.15;
							Questions.coefficient[3] = 0.3;
							Questions.coefficient[4] = 0.3;
						} else if (addLevel == 4) {
							// Level 4
							numOfEasyAdd = 0;
							numOfMediumAdd =3;
							numOfHardAdd = 2;
							Questions.coefficient[0] = 0.1;
							Questions.coefficient[1] = 0.1;
							Questions.coefficient[2] = 0.1;
							Questions.coefficient[3] = 0.35;
							Questions.coefficient[4] = 0.35;
						} else  {
							// Level 5
							numOfEasyAdd = 0;
							numOfMediumAdd =2;
							numOfHardAdd = 3;
							Questions.coefficient[0] = 0.05;
							Questions.coefficient[1] = 0.05;
							Questions.coefficient[2] = 0.3;
							Questions.coefficient[3] = 0.3;
							Questions.coefficient[4] = 0.3;
							
						}
						
						int numOfEasySub = 0, numOfMediumSub = 0, numOfHardSub = 0;
						if (subLevel == 0) {
							// Level 0
							numOfEasySub = 3;
							numOfMediumSub =2;
							numOfHardSub = 0;
							Questions.coefficient[5] = 0.1;
							Questions.coefficient[6] = 0.1;
							Questions.coefficient[7] = 0.1;
							Questions.coefficient[8] = 0.35;
							Questions.coefficient[9] = 0.35;
						} else if (subLevel == 1) {
							// Level 1
							numOfEasySub = 3;
							numOfMediumSub =1;
							numOfHardSub = 1;
							Questions.coefficient[5] = 0.1;
							Questions.coefficient[6] = 0.1;
							Questions.coefficient[7] = 0.1;
							Questions.coefficient[8] = 0.3;
							Questions.coefficient[9] = 0.4;
						} else if (subLevel == 2) {
							// Level 2
							numOfEasySub = 2;
							numOfMediumSub =2;
							numOfHardSub = 1;
							Questions.coefficient[5] = 0.1;
							Questions.coefficient[6] = 0.1;
							Questions.coefficient[7] = 0.2;
							Questions.coefficient[8] = 0.2;
							Questions.coefficient[9] = 0.4;
						} else if (subLevel == 3) {
							// Level 3
							numOfEasySub = 1;
							numOfMediumSub =2;
							numOfHardSub = 2;
							Questions.coefficient[5] = 0.1;
							Questions.coefficient[6] = 0.15;
							Questions.coefficient[7] = 0.15;
							Questions.coefficient[8] = 0.3;
							Questions.coefficient[9] = 0.3;
						} else if (subLevel == 4) {
							// Level 4
							numOfEasySub = 0;
							numOfMediumSub =3;
							numOfHardSub = 2;
							Questions.coefficient[5] = 0.1;
							Questions.coefficient[6] = 0.1;
							Questions.coefficient[7] = 0.1;
							Questions.coefficient[8] = 0.35;
							Questions.coefficient[9] = 0.35;
						} else  {
							// Level 5
							numOfEasySub = 0;
							numOfMediumSub =2;
							numOfHardSub = 3;
							Questions.coefficient[5] = 0.05;
							Questions.coefficient[6] = 0.05;
							Questions.coefficient[7] = 0.3;
							Questions.coefficient[8] = 0.3;
							Questions.coefficient[9] = 0.3;
							
						}
						
						// Randomise easy ADD questions if needed
						if(numOfEasyAdd != 0) {
							easyAdd(snapshot, numOfEasyAdd);
						}
						// Randomise medium ADD questions (always needed)
						mediumAdd(snapshot ,numOfEasyAdd, numOfMediumAdd);
						// Randomise hard ADD questions if needed
						if(numOfHardAdd != 0) {
							hardAdd(snapshot ,numOfEasyAdd, numOfMediumAdd , numOfHardAdd);
						}
						
						// Randomise easy SUB questions if needed
						if(numOfEasySub != 0) {
							easySub(snapshot, numOfEasySub);
						}
						// Randomise medium SUB questions (always needed)
						mediumSub(snapshot ,numOfEasySub, numOfMediumSub);
						// Randomise hard SUB questions if needed
						if(numOfHardSub != 0) {
							hardSub(snapshot ,numOfEasySub, numOfMediumSub , numOfHardSub);
						}
						
						// Prepare the first question for display
						DataSnapshot firstQuestion = Questions.QuestionsRefFortest[0];
						
						// Fetch the question and it's answers from the DB
						String question =  (String) firstQuestion.child("/question").getValue();
						String ans1 =  firstQuestion.child("/ans1").getValue().toString();
						String ans2 =  firstQuestion.child("/ans2").getValue().toString();
						String ans3 =  firstQuestion.child("/ans3").getValue().toString();
						String ans4 =  firstQuestion.child("/ans4").getValue().toString();
						Questions.question = question;
						Questions.points=0;
						Questions.ans1 = ans1;
						Questions.ans2 = ans2;
						Questions.ans3 = ans3;
						Questions.ans4 = ans4;
						Questions.ans = getAns(Questions.question);
						Questions.numOfQuestion=1;
						
						// Redirect to the Test Screen activity where the question will be displayed
						Intent myIntent= null;
						myIntent = new Intent(TestActivity.this, com.easymath.activities.test.TestScreenActivity.class);
						TestActivity.this.startActivity(myIntent);
						finish();
						}

						/**
						 * Randomise Hard Sub
						 */
						private void hardSub(DataSnapshot snapshot,int numOfEasySub, int numOfMediumSub, int numOfHardSub) {
							
							// Fetch hard sub questions
							DataSnapshot child = snapshot.child("sub/3elements");
							
							// Iterate over all of the questions fetched and list them
							int numOfChild = (int) child.getChildrenCount();
							ArrayList <Integer> list = new ArrayList<Integer>();
							for (int i=1; i<=numOfChild; i++) {
								list.add(Integer.valueOf(i));
							}
							
							// Shuffle the list
							Collections.shuffle(list);
							// Pick the needed number of questions
							for (int j=5 ; j<5+ numOfHardSub ; j++){
								Questions.QuestionsRefFortest[j+ numOfEasySub + numOfMediumSub] = child.child(String.valueOf(list.get(j+1))); 
							}
							
						}

						/**
						 * Randomise Medium Sub
						 */
						private void mediumSub(DataSnapshot snapshot,int numOfEasySub, int numOfMediumSub) {
							
							// Fetch medium sub questions
							DataSnapshot child = snapshot.child("sub/upto20");
							
							// Iterate over all of the questions fetched and list them
							int numOfChild = (int) child.getChildrenCount();
							ArrayList <Integer> list = new ArrayList<Integer>();
							for (int i=1; i<=numOfChild; i++) {
								list.add(new Integer(i));
							}
							
							// Shuffle the list
							Collections.shuffle(list);
							
							// Pick the needed number of questions
							for (int j=5 ; j< 5 + numOfMediumSub ; j++){
								Questions.QuestionsRefFortest[j+numOfEasySub] = child.child(String.valueOf(list.get(j+1))); 
							}
							
						}

						/**
						 * Randomise Easy Sub
						 */
						private void easySub(DataSnapshot snapshot, int numOfEasySub) {
							
							// Fetch easy sub questions
							DataSnapshot child = snapshot.child("sub/basic");
							
							// Iterate over all of the questions fetched and list them
							int numOfChild = (int) child.getChildrenCount();
							ArrayList <Integer> list = new ArrayList<Integer>();
							for (int i=1; i<=numOfChild; i++) {
								list.add(new Integer(i));
							}
							
							// Shuffle the list
							Collections.shuffle(list);
							
							// Pick the needed number of questions
							for (int j=5 ; j< 5 + numOfEasySub ; j++){
								Questions.QuestionsRefFortest[j] = child.child(String.valueOf(list.get(j+1))); 
							}
							
						}

						/**
						 * Randomise Hard Add
						 */
						private void hardAdd(DataSnapshot snapshot,int numOfEasyAdd, int numOfMediumAdd, int numOfHardAdd) {
							
							// Fetch hard add questions
							DataSnapshot child = snapshot.child("add/3elements");
							
							// Iterate over all of the questions fetched and list them
							int numOfChild = (int) child.getChildrenCount();
							ArrayList <Integer> list = new ArrayList<Integer>();
							for (int i=1; i<=numOfChild; i++) {
								list.add(new Integer(i));
							}
							
							// Shuffle the list
							Collections.shuffle(list);
							
							// Pick the needed number of questions
							for (int j=0 ; j< numOfHardAdd ; j++){
								Questions.QuestionsRefFortest[j+numOfEasyAdd+numOfMediumAdd] = child.child(String.valueOf(list.get(j+1))); 
							}
							
							
						}

						/**
						 * Randomise Medium Add
						 */
						private void mediumAdd(DataSnapshot snapshot, int numOfEasyAdd, int numOfMediumAdd) {
							
							// Fetch medium add questions
							DataSnapshot child = snapshot.child("add/upto20");
							
							// Iterate over all of the questions fetched and list them
							int numOfChild = (int) child.getChildrenCount();
							ArrayList <Integer> list = new ArrayList<Integer>();
							for (int i=1; i<=numOfChild; i++) {
								list.add(new Integer(i));
							}
							
							// Shuffle the list
							Collections.shuffle(list);
							
							// Pick the needed number of questions
							for (int j=0 ; j< numOfMediumAdd ; j++){
								Questions.QuestionsRefFortest[j+numOfEasyAdd] = child.child(String.valueOf(list.get(j+1))); 
							}
							
						}

						/**
						 * Randomise Easy Add
						 */
						private void easyAdd(DataSnapshot snapshot, int numOfEasyAdd) {
							
							// Fetch easy add questions
							DataSnapshot child = snapshot.child("add/basic");
							
							// Iterate over all of the questions fetched and list them
							int numOfChild = (int) child.getChildrenCount();
							ArrayList <Integer> list = new ArrayList<Integer>();
							for (int i=1; i<=numOfChild; i++) {
								list.add(new Integer(i));
							}
							
							// Shuffle the list
							Collections.shuffle(list);
							
							// Pick the needed number of questions
							for (int j=0 ; j< numOfEasyAdd ; j++){
								Questions.QuestionsRefFortest[j] = child.child(String.valueOf(list.get(j+1))); 
							}
						}
						
						@Override
						public void onCancelled(FirebaseError arg0) {
						}
					});
	
				
			}

			@Override
			public void onCancelled(FirebaseError arg0) {
			}
		});
		
	}
	
	/**
	 * Calculate the true answer of a question
	 * @param question
	 */
	public static int getAns(String question) {

		String[] parts = question.split("\\b");
		int ans = 0;
		
		// For short questions
		if(parts.length == 4) {
			if(parts[2].equals("+")){
				ans =  Integer.parseInt(parts[1]) + Integer.parseInt(parts[3]);
			}
			if(parts[2].equals("*")){
				ans =  Integer.parseInt(parts[1]) * Integer.parseInt(parts[3]);
			}
			if(parts[2].equals("-")){
				ans =  Integer.parseInt(parts[1]) - Integer.parseInt(parts[3]);
			}
			if(parts[2].equals("/")){
				ans =  Integer.parseInt(parts[1]) / Integer.parseInt(parts[3]);
			}
		}

		// For 3 elements questions (longer)
		if(parts.length == 6) {
			if(parts[2].equals("+")){
				ans =  Integer.parseInt(parts[1]) + Integer.parseInt(parts[3]) + Integer.parseInt(parts[5]);
			}
			if(parts[2].equals("*")){
				ans =  Integer.parseInt(parts[1]) * Integer.parseInt(parts[3]) * Integer.parseInt(parts[5]);
			}
			if(parts[2].equals("-")){
				ans =  Integer.parseInt(parts[1]) - Integer.parseInt(parts[3]) - Integer.parseInt(parts[5]);
			}
			if(parts[2].equals("/")){
				ans =  Integer.parseInt(parts[1]) / Integer.parseInt(parts[3]) / Integer.parseInt(parts[5]);
			}
		}

		return ans;
	}

}
