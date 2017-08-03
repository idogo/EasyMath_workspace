package com.easymath.activities;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import com.easymath.util.PropertiesUtil;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.easymath.activities.test.TestActivity;
import com.easymath.util.FirebaseDBUtils;
import com.easymath.R;

/**
 * Screen to add a new exercise for teachers
 * Makes sure the exercise fits the template, rules and doesn't exist already before adding it to the DB
 * @author liadpc
 *
 */
public class AddExScreenActivity extends Activity {

	private static final String WRONG_EX_TITLE_PROP = "add.exercise.wrongex.title";
	private static final String WRONG_EX_MSG_PROP = "add.exercise.wrongex.msg";
	private static final String NOT_MATCHING_EX_TITLE_PROP = "add.exercise.notmatchex.title";
	private static final String NOT_MATCHING_EX_MSG_PROP = "add.exercise.notmatchex.msg";
	private static final String EX_EXISTS_TITLE_PROP = "add.exercise.existsex.title";
	private static final String EX_EXISTS_MSG_PROP = "add.exercise.existsex.msg";
	private static final String ADDED_EX_TITLE_PROP = "add.exercise.addedex.title";
	private static final String ADDED_EX_MSG_PROP = "add.exercise.addedex.msg";

	// Possible subjects enum
	private enum AddExSubject {
		ADD_BASIC, ADD_UPTO20, ADD_3ELEMENTS, SUB_BASIC, SUB_UPTO20, SUB_3ELEMENTS, ADDSUB}	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Handle context
		setContentView(R.layout.addex_screen);
		final Context context = this;
		
		// Handle view and background
		View view = getWindow().getDecorView();
		Drawable myIcon = getResources().getDrawable(R.drawable.sky);
		view.setBackgroundDrawable(myIcon);
		
		// "Add exercise" button and text
		Button addEx = (Button)findViewById(R.id.btnaddEx);
		final TextView exerciseTextView = (TextView) findViewById(R.id.tvaddEx);
		
		// DB Connection with connection string to fetch all existing questions
		final Firebase questionsRef = new Firebase(FirebaseDBUtils.getDatabaseURL() + "questions");

		addEx.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Get arg
				String passedArg = getIntent().getExtras().getString("nextArg");
				final String exercise = exerciseTextView.getText().toString();
                try {
                    // Switch case for each subject
                    switch (AddExSubject.valueOf(passedArg)) {
                        case ADD_BASIC:
                            final Firebase ref = questionsRef.child("add/basic");
                            if (!exercise.contains("+")) { // Check that the exercise contains "+"

						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
						alertDialogBuilder.setTitle(WRONG_EX_TITLE_PROP);
						alertDialogBuilder.setMessage(WRONG_EX_MSG_PROP).setCancelable(false).setPositiveButton("אישור",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								dialog.cancel();
							}
						});
						AlertDialog alertDialog = alertDialogBuilder.create();
						alertDialog.show();
					}
					else {
						final String[] parts = exercise.split("[+]");
						 // Check that the exercise contains 2 elements
						if(!(parts[0].matches("\\d+"))||!(parts[1].matches("\\d+"))) {
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
							alertDialogBuilder.setTitle(WRONG_EX_TITLE_PROP);
							alertDialogBuilder.setMessage(WRONG_EX_MSG_PROP).setCancelable(false).setPositiveButton("אישור",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									dialog.cancel();
								}
							});
							AlertDialog alertDialog = alertDialogBuilder.create();
							alertDialog.show();
						}
						 // Check that the exercise sum isn't less than 0 or more than 10
						else if (((Integer.parseInt(parts[0]) + Integer.parseInt(parts[1]) > 10)||
								((Integer.parseInt(parts[0])<0)))||(Integer.parseInt(parts[1]) < 0)) {
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
									context);
							alertDialogBuilder.setTitle(NOT_MATCHING_EX_TITLE_PROP);
							alertDialogBuilder.setMessage(NOT_MATCHING_EX_MSG_PROP).setCancelable(false).setPositiveButton("אישור",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									dialog.cancel();
								}
							});
							AlertDialog alertDialog = alertDialogBuilder.create();
							alertDialog.show();

						}						
						else { // The exercise is valid, add it
							ref.addListenerForSingleValueEvent(new ValueEventListener() {

								@Override
								public void onDataChange(DataSnapshot snapShot) {
									// Iterate over all the questions
									long childrenCount = snapShot.getChildrenCount();
									boolean found = false;
									for(int i = 1; i<=childrenCount ; i++){
										String childNum = String.valueOf(i);
										// Question already exists
										if(snapShot.child(childNum).child("question").getValue().equals(exercise)) {
											found = true;
											AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
													context);
											alertDialogBuilder.setTitle(EX_EXISTS_TITLE_PROP);
											alertDialogBuilder.setMessage(EX_EXISTS_MSG_PROP).setCancelable(false).setPositiveButton("אישור",new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog,int id) {
													dialog.cancel();
												}
											});
											AlertDialog alertDialog = alertDialogBuilder.create();
											alertDialog.show();
										}
									}
									// Question doesn't exist, add it
									if(!found){

										ArrayList <Integer> list = new ArrayList<Integer>();
										for (int i=1; i<11; i++) {
											list.add(Integer.valueOf(i));
										}
										Collections.shuffle(list);
										list.remove((Integer)TestActivity.getAns(exercise));
										String numOfExercise = (String.valueOf(childrenCount+1));
										Firebase newRef = ref.child(numOfExercise);
										newRef.child("question").setValue(exercise);
										newRef.child("ans1").setValue(String.valueOf(TestActivity.getAns(exercise)));
										newRef.child("ans2").setValue(list.get(0).toString());
										newRef.child("ans3").setValue(list.get(1).toString());
										newRef.child("ans4").setValue(list.get(2).toString());
										AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
												context);
										alertDialogBuilder.setTitle(ADDED_EX_TITLE_PROP);
										alertDialogBuilder.setMessage(ADDED_EX_MSG_PROP).setCancelable(false).setPositiveButton("אישור",new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog,int id) {
												dialog.cancel();
											}
										});
										AlertDialog alertDialog = alertDialogBuilder.create();
										alertDialog.show();
									}	
								}
								@Override
								public void onCancelled(FirebaseError arg0) {
								}
							});

						}

					}
					break;

				case ADD_UPTO20 : 
					final Firebase ref2 = questionsRef.child("add/upto20");
					if (!exercise.contains("+")) { // Check that the exercise contains "+"

                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                        context);
                                alertDialogBuilder.setTitle("תרגיל שגוי");
                                alertDialogBuilder.setMessage("תרגיל לא נכתב טוב, אנא נסה שוב").setCancelable(false).setPositiveButton(PropertiesUtil.getOkMessage(), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                            } else {
                                final String[] parts = exercise.split("[+]");
                                // Check that the exercise contains 2 elements
                                if (!(parts[0].matches("\\d+")) || !(parts[1].matches("\\d+"))) {
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                            context);
                                    alertDialogBuilder.setTitle("תרגיל שגוי");
                                    alertDialogBuilder.setMessage("תרגיל לא נכתב טוב, אנא נסה שוב").setCancelable(false)
                                            .setPositiveButton(PropertiesUtil.getOkMessage(), new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                }
                                // Check that the exercise sum isn't less than 0 or more than 20
                                else if (Integer.parseInt(parts[0]) + Integer.parseInt(parts[1]) > 20) {
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                            context);
                                    alertDialogBuilder.setTitle("תרגיל לא מתאים");
                                    alertDialogBuilder.setMessage("התרגיל לא מתאים לרמה זאת , אנא נסה תרגיל אחר").setCancelable(false).setPositiveButton(PropertiesUtil.getOkMessage(), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								context);
						alertDialogBuilder.setTitle(WRONG_EX_TITLE_PROP);
						alertDialogBuilder.setMessage(WRONG_EX_MSG_PROP).setCancelable(false).setPositiveButton("אישור",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								dialog.cancel();
							}
						});
						AlertDialog alertDialog = alertDialogBuilder.create();
						alertDialog.show();
					}
					else {
						final String[] parts = exercise.split("[+]");
						// Check that the exercise contains 2 elements
						if(!(parts[0].matches("\\d+"))||!(parts[1].matches("\\d+"))) {
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
									context);
							alertDialogBuilder.setTitle(WRONG_EX_TITLE_PROP);
							alertDialogBuilder.setMessage(WRONG_EX_MSG_PROP).setCancelable(false)
							.setPositiveButton("אישור",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									dialog.cancel();
								}
							});
							AlertDialog alertDialog = alertDialogBuilder.create();
							alertDialog.show();
						}
						 // Check that the exercise sum isn't less than 0 or more than 20
						else if (Integer.parseInt(parts[0]) + Integer.parseInt(parts[1]) > 20) {
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
									context);
							alertDialogBuilder.setTitle(NOT_MATCHING_EX_TITLE_PROP);
							alertDialogBuilder.setMessage(NOT_MATCHING_EX_MSG_PROP).setCancelable(false).setPositiveButton("אישור",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									dialog.cancel();
								}
							});
							AlertDialog alertDialog = alertDialogBuilder.create();
							alertDialog.show();

						}		
						// The exercise is valid, add it
						else {
							ref2.addListenerForSingleValueEvent(new ValueEventListener() {

								@Override
								public void onDataChange(DataSnapshot snapShot) {

									long childrenCount = snapShot.getChildrenCount();
									boolean found = false;
									
									// Iterate over all the questions
									for(int i = 1; i<=childrenCount ; i++){
										String childNum = String.valueOf(i);
										// Question already exists
										if(snapShot.child(childNum).child("question").getValue().equals(exercise)) {
											found = true;
											AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
													context);
											alertDialogBuilder.setTitle(EX_EXISTS_TITLE_PROP);
											alertDialogBuilder.setMessage(EX_EXISTS_MSG_PROP).setCancelable(false).setPositiveButton("אישור",new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog,int id) {
													dialog.cancel();
												}
											});
											AlertDialog alertDialog = alertDialogBuilder.create();
											alertDialog.show();
										}
									}
									// Question doesn't exist, add it
									if(!found){

										ArrayList <Integer> list = new ArrayList<Integer>();
										for (int i=1; i<21; i++) {
											list.add(Integer.valueOf(i));
										}
										Collections.shuffle(list);
										list.remove((Integer)TestActivity.getAns(exercise));
										String numOfExercise = (String.valueOf(childrenCount+1));
										Firebase newRef = ref2.child(numOfExercise);
										newRef.child("question").setValue(exercise);
										newRef.child("ans1").setValue(String.valueOf(TestActivity.getAns(exercise)));
										newRef.child("ans2").setValue(list.get(0).toString());
										newRef.child("ans3").setValue(list.get(1).toString());
										newRef.child("ans4").setValue(list.get(2).toString());
										AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
												context);
										alertDialogBuilder.setTitle(ADDED_EX_TITLE_PROP);
										alertDialogBuilder.setMessage(ADDED_EX_MSG_PROP).setCancelable(false).setPositiveButton("אישור",new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog,int id) {
												dialog.cancel();
											}
										});
										AlertDialog alertDialog = alertDialogBuilder.create();
										alertDialog.show();
									}	
								}
								@Override
								public void onCancelled(FirebaseError arg0) {
								}
							});

						}

					}
					break;

				case ADD_3ELEMENTS : 
					final Firebase refAddElem = questionsRef.child("add/3elements");
					if (!exercise.contains("+")) { // Check that the exercise contains "+"

						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
						alertDialogBuilder.setTitle(WRONG_EX_TITLE_PROP);
						alertDialogBuilder.setMessage(WRONG_EX_MSG_PROP).setCancelable(false).setPositiveButton("אישור",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
						AlertDialog alertDialog = alertDialogBuilder.create();
						alertDialog.show();
					}
					else {
						final String[] parts = exercise.split("[+]");
						// Check that the exercise contains 3 elements
						if(parts.length!=3) {
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
									context);
							alertDialogBuilder.setTitle(WRONG_EX_TITLE_PROP);
							alertDialogBuilder.setMessage(WRONG_EX_MSG_PROP).setCancelable(false)
							.setPositiveButton("אישור",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									dialog.cancel();
								}
							});
							AlertDialog alertDialog = alertDialogBuilder.create();
							alertDialog.show();
						}
						else if(!(parts[0].matches("\\d+"))||!(parts[1].matches("\\d+"))||!(parts[2].matches("\\d+"))) {
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
									context);
							alertDialogBuilder.setTitle(WRONG_EX_TITLE_PROP);
							alertDialogBuilder.setMessage(WRONG_EX_MSG_PROP).setCancelable(false)
							.setPositiveButton("אישור",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									dialog.cancel();
								}
							});
							AlertDialog alertDialog = alertDialogBuilder.create();
							alertDialog.show();
						}
						// Check that the exercise sum isn't less than 0 or more than 20
						else if (Integer.parseInt(parts[0]) + Integer.parseInt(parts[1]) + Integer.parseInt(parts[2])> 20) {
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
									context);
							alertDialogBuilder.setTitle(NOT_MATCHING_EX_TITLE_PROP);
							alertDialogBuilder.setMessage(NOT_MATCHING_EX_MSG_PROP).setCancelable(false)
							.setPositiveButton("אישור",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									dialog.cancel();
								}
							});
							AlertDialog alertDialog = alertDialogBuilder.create();
							alertDialog.show();

						}		
						// The exercise is valid, add it
						else {
							refAddElem.addListenerForSingleValueEvent(new ValueEventListener() {

								@Override
								public void onDataChange(DataSnapshot snapShot) {

									long childrenCount = snapShot.getChildrenCount();
									boolean found = false;
									
									// Iterate over all the questions
									for(int i = 1; i<=childrenCount ; i++){
										String childNum = String.valueOf(i);
										// Question already exists
										if(snapShot.child(childNum).child("question").getValue().equals(exercise)) {
											found = true;
											AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
													context);
											alertDialogBuilder.setTitle(EX_EXISTS_TITLE_PROP);
											alertDialogBuilder.setMessage(EX_EXISTS_MSG_PROP).setCancelable(false)
											.setPositiveButton("אישור",new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog,int id) {
													dialog.cancel();
												}
											});
											AlertDialog alertDialog = alertDialogBuilder.create();
											alertDialog.show();
										}
									}
									// Question doesn't exist, add it
									if(!found){
										ArrayList <Integer> list = new ArrayList<Integer>();
										for (int i=1; i<21; i++) {
											list.add(Integer.valueOf(i));
										}
										Collections.shuffle(list);
										list.remove((Integer)TestActivity.getAns(exercise));
										String numOfExercise = (String.valueOf(childrenCount+1));
										Firebase newRef = refAddElem.child(numOfExercise);
										newRef.child("question").setValue(exercise);
										newRef.child("ans1").setValue(String.valueOf(TestActivity.getAns(exercise)));
										newRef.child("ans2").setValue(list.get(0).toString());
										newRef.child("ans3").setValue(list.get(1).toString());
										newRef.child("ans4").setValue(list.get(2).toString());
										AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
												context);
										alertDialogBuilder.setTitle(ADDED_EX_TITLE_PROP);
										alertDialogBuilder.setMessage(ADDED_EX_MSG_PROP).setCancelable(false)
										.setPositiveButton("אישור",new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog,int id) {
												dialog.cancel();
											}
										});
										AlertDialog alertDialog = alertDialogBuilder.create();
										alertDialog.show();
									}	
								}


								@Override
								public void onCancelled(FirebaseError arg0) {

								}
							});

						}

					}
					break;

				case SUB_BASIC : 
					final Firebase ref3 = questionsRef.child("sub/basic");
					if (!exercise.contains("-")) {

						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								context);
						alertDialogBuilder.setTitle(WRONG_EX_TITLE_PROP);
						alertDialogBuilder.setMessage(WRONG_EX_MSG_PROP).setCancelable(false)
						.setPositiveButton("אישור",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								dialog.cancel();
							}
						});
						AlertDialog alertDialog = alertDialogBuilder.create();
						alertDialog.show();
					}
					else {
						// Check that the exercise contains "-"
						final String[] parts = exercise.split("[-]");
						// Check that the exercise contains 2 elements
						if(!(parts[0].matches("\\d+"))||!(parts[1].matches("\\d+"))) {
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
							alertDialogBuilder.setTitle(WRONG_EX_TITLE_PROP);
							alertDialogBuilder.setMessage(WRONG_EX_MSG_PROP).setCancelable(false).setPositiveButton("אישור",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									dialog.cancel();
								}
							});
							AlertDialog alertDialog = alertDialogBuilder.create();
							alertDialog.show();
						}
						 // Check that the exercise sum isn't less than 0 or more than 10
						else if ((Integer.parseInt(parts[0]) - Integer.parseInt(parts[1]) < 0) || (Integer.parseInt(parts[0]) > 10)) {
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
									context);
							alertDialogBuilder.setTitle(NOT_MATCHING_EX_TITLE_PROP);
							alertDialogBuilder.setMessage(NOT_MATCHING_EX_MSG_PROP).setCancelable(false)
							.setPositiveButton("אישור",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									dialog.cancel();
								}
							});
							AlertDialog alertDialog = alertDialogBuilder.create();
							alertDialog.show();

						}		
						// The exercise is valid, add it
						else {
							ref3.addListenerForSingleValueEvent(new ValueEventListener() {

								@Override
								public void onDataChange(DataSnapshot snapShot) {

									long childrenCount = snapShot.getChildrenCount();
									boolean found = false;
									// Iterate over all the questions
									for(int i = 1; i<=childrenCount ; i++){
										String childNum = String.valueOf(i);
										// Question already exists
										if(snapShot.child(childNum).child("question").getValue().equals(exercise)) {
											found = true;
											AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
													context);
											alertDialogBuilder.setTitle(EX_EXISTS_TITLE_PROP);
											alertDialogBuilder.setMessage(EX_EXISTS_MSG_PROP).setCancelable(false)
											.setPositiveButton("אישור",new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog,int id) {
													dialog.cancel();
												}
											});
											AlertDialog alertDialog = alertDialogBuilder.create();
											alertDialog.show();
										}
									}
									// Question doesn't exist, add it
									if(!found){
										ArrayList <Integer> list = new ArrayList<Integer>();
										for (int i=1; i<11; i++) {
											list.add(Integer.valueOf(i));
										}
										Collections.shuffle(list);
										list.remove((Integer)TestActivity.getAns(exercise));
										String numOfExercise = (String.valueOf(childrenCount+1));
										Firebase newRef = ref3.child(numOfExercise);
										newRef.child("question").setValue(exercise);
										newRef.child("ans1").setValue(String.valueOf(TestActivity.getAns(exercise)));
										newRef.child("ans2").setValue(list.get(0).toString());
										newRef.child("ans3").setValue(list.get(1).toString());
										newRef.child("ans4").setValue(list.get(2).toString());
										AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
												context);
										alertDialogBuilder.setTitle(ADDED_EX_TITLE_PROP);
										alertDialogBuilder.setMessage(ADDED_EX_MSG_PROP).setCancelable(false)
										.setPositiveButton("אישור",new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog,int id) {
												dialog.cancel();
											}
										});
										AlertDialog alertDialog = alertDialogBuilder.create();
										alertDialog.show();
									}	
								}


								@Override
								public void onCancelled(FirebaseError arg0) {

								}
							});

						}

					}
					break;

				case SUB_UPTO20 : 
					final Firebase ref4 = questionsRef.child("sub/upto20");
					// Check that the exercise contains "-"
					if (!exercise.contains("-")) {
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
						alertDialogBuilder.setTitle(WRONG_EX_TITLE_PROP);
						alertDialogBuilder.setMessage(WRONG_EX_MSG_PROP).setCancelable(false).setPositiveButton("אישור",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								dialog.cancel();
							}
						});
						AlertDialog alertDialog = alertDialogBuilder.create();
						alertDialog.show();
					}
					else {
						final String[] parts = exercise.split("[-]");
						// Check that the exercise contains 2 elements
						if(!(parts[0].matches("\\d+"))||!(parts[1].matches("\\d+"))) {
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
									context);
							alertDialogBuilder.setTitle(WRONG_EX_TITLE_PROP);
							alertDialogBuilder.setMessage(WRONG_EX_MSG_PROP).setCancelable(false)
							.setPositiveButton("אישור",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									dialog.cancel();
								}
							});
							AlertDialog alertDialog = alertDialogBuilder.create();
							alertDialog.show();
						}
						 // Check that the exercise sum isn't less than 0 or more than 20
						else if ((Integer.parseInt(parts[0]) - Integer.parseInt(parts[1]) < 0) || (Integer.parseInt(parts[0]) > 20)) {
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
									context);
							alertDialogBuilder.setTitle(NOT_MATCHING_EX_TITLE_PROP);
							alertDialogBuilder.setMessage(NOT_MATCHING_EX_MSG_PROP).setCancelable(false)
							.setPositiveButton("אישור",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									dialog.cancel();
								}
							});
							AlertDialog alertDialog = alertDialogBuilder.create();
							alertDialog.show();

						}		
						// The exercise is valid, add it
						else {
							ref4.addListenerForSingleValueEvent(new ValueEventListener() {

								@Override
								public void onDataChange(DataSnapshot snapShot) {

									long childrenCount = snapShot.getChildrenCount();
									boolean found = false;
									// Iterate over all the questions
									for(int i = 1; i<=childrenCount ; i++){
										String childNum = String.valueOf(i);
										// Question already exists
										if(snapShot.child(childNum).child("question").getValue().equals(exercise)) {
											found = true;
											AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
													context);
											alertDialogBuilder.setTitle(EX_EXISTS_TITLE_PROP);
											alertDialogBuilder.setMessage(EX_EXISTS_MSG_PROP).setCancelable(false)
											.setPositiveButton("אישור",new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog,int id) {
													dialog.cancel();
												}
											});
											AlertDialog alertDialog = alertDialogBuilder.create();
											alertDialog.show();
										}
									}
									// Question doesn't exist, add it
									if(!found){
										ArrayList <Integer> list = new ArrayList<Integer>();
										for (int i=1; i<21; i++) {
											list.add(Integer.valueOf(i));
										}
										Collections.shuffle(list);
										list.remove((Integer)TestActivity.getAns(exercise));
										String numOfExercise = (String.valueOf(childrenCount+1));
										Firebase newRef = ref4.child(numOfExercise);
										newRef.child("question").setValue(exercise);
										newRef.child("ans1").setValue(String.valueOf(TestActivity.getAns(exercise)));
										newRef.child("ans2").setValue(list.get(0).toString());
										newRef.child("ans3").setValue(list.get(1).toString());
										newRef.child("ans4").setValue(list.get(2).toString());
										AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
												context);
										alertDialogBuilder.setTitle(ADDED_EX_TITLE_PROP);
										alertDialogBuilder.setMessage(ADDED_EX_MSG_PROP).setCancelable(false)
										.setPositiveButton("אישור",new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog,int id) {
												dialog.cancel();
											}
										});
										AlertDialog alertDialog = alertDialogBuilder.create();
										alertDialog.show();
									}	
								}


								@Override
								public void onCancelled(FirebaseError arg0) {

								}
							});

						}

					}
					break;

				case SUB_3ELEMENTS : 
					final Firebase refSubElem = questionsRef.child("sub/3elements");
					// Check that the exercise contains "-"
					if (!exercise.contains("-")) {
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
						alertDialogBuilder.setTitle(WRONG_EX_TITLE_PROP);
						alertDialogBuilder.setMessage(WRONG_EX_MSG_PROP).setCancelable(false).setPositiveButton("אישור",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								dialog.cancel();
							}
						});
						AlertDialog alertDialog = alertDialogBuilder.create();
						alertDialog.show();
					}
					else {
						final String[] parts = exercise.split("[-]");
						// Check that the exercise contains 3 elements
						if(parts.length!=3) {
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
							alertDialogBuilder.setTitle(WRONG_EX_TITLE_PROP);
							alertDialogBuilder.setMessage(WRONG_EX_MSG_PROP).setCancelable(false).setPositiveButton("אישור",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									dialog.cancel();
								}
							});
							AlertDialog alertDialog = alertDialogBuilder.create();
							alertDialog.show();
						}
						else if(!(parts[0].matches("\\d+"))||!(parts[1].matches("\\d+"))||!(parts[2].matches("\\d+"))) {
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
									context);
							alertDialogBuilder.setTitle(WRONG_EX_TITLE_PROP);
							alertDialogBuilder.setMessage(WRONG_EX_MSG_PROP).setCancelable(false).setPositiveButton("אישור",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									dialog.cancel();
								}
							});
							AlertDialog alertDialog = alertDialogBuilder.create();
							alertDialog.show();
						}
						// Check that the exercise sum isn't less than 0 or more than 20
						else if  ((Integer.parseInt(parts[0]) - Integer.parseInt(parts[1]) - Integer.parseInt(parts[2])< 0) || (Integer.parseInt(parts[0]) > 20)) {
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
							alertDialogBuilder.setTitle(NOT_MATCHING_EX_TITLE_PROP);
							alertDialogBuilder.setMessage(NOT_MATCHING_EX_MSG_PROP).setCancelable(false).setPositiveButton("אישור",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									dialog.cancel();
								}
							});
							AlertDialog alertDialog = alertDialogBuilder.create();
							alertDialog.show();

						}			
						// The exercise is valid, add it
						else {
							refSubElem.addListenerForSingleValueEvent(new ValueEventListener() {

								@Override
								public void onDataChange(DataSnapshot snapShot) {

									long childrenCount = snapShot.getChildrenCount();
									boolean found = false;
									// Iterate over all the questions
									for(int i = 1; i<=childrenCount ; i++){
										String childNum = String.valueOf(i);
										// Question already exists
										if(snapShot.child(childNum).child("question").getValue().equals(exercise)) {
											found = true;
											AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
											alertDialogBuilder.setTitle(EX_EXISTS_TITLE_PROP);
											alertDialogBuilder.setMessage(EX_EXISTS_MSG_PROP).setCancelable(false).setPositiveButton("אישור",new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog,int id) {
													dialog.cancel();
												}
											});
											AlertDialog alertDialog = alertDialogBuilder.create();
											alertDialog.show();
										}
									}
									// Question doesn't exist, add it
									if(!found){
										ArrayList <Integer> list = new ArrayList<Integer>();
										for (int i=1; i<21; i++) {
											list.add(Integer.valueOf(i));
										}
										Collections.shuffle(list);
										list.remove((Integer)TestActivity.getAns(exercise));
										String numOfExercise = (String.valueOf(childrenCount+1));
										Firebase newRef = refSubElem.child(numOfExercise);
										newRef.child("question").setValue(exercise);
										newRef.child("ans1").setValue(String.valueOf(TestActivity.getAns(exercise)));
										newRef.child("ans2").setValue(list.get(0).toString());
										newRef.child("ans3").setValue(list.get(1).toString());
										newRef.child("ans4").setValue(list.get(2).toString());
										AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
										alertDialogBuilder.setTitle(ADDED_EX_TITLE_PROP);
										alertDialogBuilder.setMessage(ADDED_EX_MSG_PROP).setCancelable(false).setPositiveButton("אישור",new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog,int id) {
												dialog.cancel();
											}
										});
										AlertDialog alertDialog = alertDialogBuilder.create();
										alertDialog.show();
									}	
								}


								@Override
								public void onCancelled(FirebaseError arg0) {

								}
							});

						}

					}
					break;

				case ADDSUB : 	
					final Firebase refAddSub = questionsRef.child("addsub/basic");
					// Check that the exercise contains "-" or "+"
					if ((!exercise.contains("-"))||(!exercise.contains("+"))) {

						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								context);
						alertDialogBuilder.setTitle(WRONG_EX_TITLE_PROP);
						alertDialogBuilder.setMessage(WRONG_EX_MSG_PROP).setCancelable(false).setPositiveButton("אישור",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								dialog.cancel();
							}
						});
						AlertDialog alertDialog = alertDialogBuilder.create();
						alertDialog.show();
					}
					else {
						// Check that the exercise contains 3 elements
						String[] parts = exercise.split("\\b");
						if(parts.length!=6) {
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
									context);
							alertDialogBuilder.setTitle(WRONG_EX_TITLE_PROP);
							alertDialogBuilder.setMessage(WRONG_EX_MSG_PROP).setCancelable(false).setPositiveButton("אישור",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									dialog.cancel();
								}
							});
							AlertDialog alertDialog = alertDialogBuilder.create();
							alertDialog.show();
						}
						// Check that the exercise contains the right order of 3 elements between the operators
						else if(!(parts[1].matches("\\d+"))||!(parts[3].matches("\\d+"))||!(parts[5].matches("\\d+"))) {
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
									context);
							alertDialogBuilder.setTitle(WRONG_EX_TITLE_PROP);
							alertDialogBuilder.setMessage(WRONG_EX_MSG_PROP).setCancelable(false).setPositiveButton("אישור",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									dialog.cancel();
								}
							});
							AlertDialog alertDialog = alertDialogBuilder.create();
							alertDialog.show();
						}

						 // if the first operator is "+"
						else if( parts[2].equals("+")) {
							 // Check that the exercise sum isn't more than 20
							if(Integer.parseInt(parts[1])+Integer.parseInt(parts[3])>20){
								AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
										context);
								alertDialogBuilder.setTitle(NOT_MATCHING_EX_TITLE_PROP);
								alertDialogBuilder.setMessage(NOT_MATCHING_EX_MSG_PROP).setCancelable(false).setPositiveButton("אישור",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										dialog.cancel();
									}
								});
								AlertDialog alertDialog = alertDialogBuilder.create();
								alertDialog.show();
							} else { // if it has "-"
								 // Check that the exercise less than 0
								if(Integer.parseInt(parts[1])+Integer.parseInt(parts[3]) - Integer.parseInt(parts[5])<0) {
									AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
											context);
									alertDialogBuilder.setTitle(NOT_MATCHING_EX_TITLE_PROP);
									alertDialogBuilder.setMessage(NOT_MATCHING_EX_MSG_PROP).setCancelable(false).setPositiveButton("אישור",new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,int id) {
											dialog.cancel();
										}
									});
									AlertDialog alertDialog = alertDialogBuilder.create();
									alertDialog.show();
								}
								// The exercise is valid, add it
								else {
									refAddSub.addListenerForSingleValueEvent(new ValueEventListener() {

										@Override
										public void onDataChange(DataSnapshot snapShot) {

											long childrenCount = snapShot.getChildrenCount();
											boolean found = false;
											// Iterate over all the questions
											for(int i = 1; i<=childrenCount ; i++){
												String childNum = String.valueOf(i);
												// Question already exists
												if(snapShot.child(childNum).child("question").getValue().equals(exercise)) {
													found = true;
													AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
															context);
													alertDialogBuilder.setTitle(EX_EXISTS_TITLE_PROP);
													alertDialogBuilder.setMessage(EX_EXISTS_MSG_PROP).setCancelable(false).setPositiveButton("אישור",new DialogInterface.OnClickListener() {
														public void onClick(DialogInterface dialog,int id) {
															dialog.cancel();
														}
													});
													AlertDialog alertDialog = alertDialogBuilder.create();
													alertDialog.show();
												}
											}
											// Question doesn't exist, add it
											if(!found){
												ArrayList <Integer> list = new ArrayList<Integer>();
												for (int i=1; i<21; i++) {
													list.add(Integer.valueOf(i));
												}
												Collections.shuffle(list);
												list.remove((Integer)TestActivity.getAns(exercise));
												String numOfExercise = (String.valueOf(childrenCount+1));
												Firebase newRef = refAddSub.child(numOfExercise);
												newRef.child("question").setValue(exercise);
												newRef.child("ans1").setValue(String.valueOf(TestActivity.getAns(exercise)));
												newRef.child("ans2").setValue(list.get(0).toString());
												newRef.child("ans3").setValue(list.get(1).toString());
												newRef.child("ans4").setValue(list.get(2).toString());
												AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
												alertDialogBuilder.setTitle(ADDED_EX_TITLE_PROP);
												alertDialogBuilder.setMessage(ADDED_EX_MSG_PROP).setCancelable(false).setPositiveButton("אישור",new DialogInterface.OnClickListener() {
													public void onClick(DialogInterface dialog,int id) {
														dialog.cancel();
													}
												});
												AlertDialog alertDialog = alertDialogBuilder.create();
												alertDialog.show();
											}	
										}
										@Override
										public void onCancelled(FirebaseError arg0) {
										}
									});
								}

							}
						// if the first operator is "-"
						} else if( parts[2].equals("-")) {
							 // Check that the exercise sum isn't less than 0
							if(Integer.parseInt(parts[1])-Integer.parseInt(parts[3])<0){
								AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
								alertDialogBuilder.setTitle(NOT_MATCHING_EX_TITLE_PROP);
								alertDialogBuilder.setMessage(NOT_MATCHING_EX_MSG_PROP).setCancelable(false).setPositiveButton("אישור",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										dialog.cancel();
									}
								});
								AlertDialog alertDialog = alertDialogBuilder.create();
								alertDialog.show();
							} else {
								 // Check that the exercise sum isn't more than 20
								if(Integer.parseInt(parts[1])-Integer.parseInt(parts[3]) + Integer.parseInt(parts[5])>20) {
									AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
									alertDialogBuilder.setTitle(NOT_MATCHING_EX_TITLE_PROP);
									alertDialogBuilder.setMessage(NOT_MATCHING_EX_MSG_PROP).setCancelable(false).setPositiveButton("אישור",new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,int id) {
											dialog.cancel();
										}
									});
									AlertDialog alertDialog = alertDialogBuilder.create();
									alertDialog.show();
								}
								// The exercise is valid, add it
								else {
									
									refAddSub.addListenerForSingleValueEvent(new ValueEventListener() {

										@Override
										public void onDataChange(DataSnapshot snapShot) {

											long childrenCount = snapShot.getChildrenCount();
											boolean found = false;
											// Iterate over all the questions
											for(int i = 1; i<=childrenCount ; i++){
												String childNum = String.valueOf(i);
												// Question already exists
												if(snapShot.child(childNum).child("question").getValue().equals(exercise)) {
													found = true;
													AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
													alertDialogBuilder.setTitle(EX_EXISTS_TITLE_PROP);
													alertDialogBuilder.setMessage(EX_EXISTS_MSG_PROP).setCancelable(false)	.setPositiveButton("אישור",new DialogInterface.OnClickListener() {
														public void onClick(DialogInterface dialog,int id) {
															dialog.cancel();
														}
													});
													AlertDialog alertDialog = alertDialogBuilder.create();
													alertDialog.show();
												}
											}
											// Question doesn't exist, add it
											if(!found){
												ArrayList <Integer> list = new ArrayList<Integer>();
												for (int i=1; i<21; i++) {
													list.add(Integer.valueOf(i));
												}
												Collections.shuffle(list);
												list.remove((Integer)TestActivity.getAns(exercise));
												String numOfExercise = (String.valueOf(childrenCount+1));
												Firebase newRef = refAddSub.child(numOfExercise);
												newRef.child("question").setValue(exercise);
												newRef.child("ans1").setValue(String.valueOf(TestActivity.getAns(exercise)));
												newRef.child("ans2").setValue(list.get(0).toString());
												newRef.child("ans3").setValue(list.get(1).toString());
												newRef.child("ans4").setValue(list.get(2).toString());
												AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
												alertDialogBuilder.setTitle(ADDED_EX_TITLE_PROP);
												alertDialogBuilder.setMessage(ADDED_EX_MSG_PROP).setCancelable(false).setPositiveButton("אישור",new DialogInterface.OnClickListener() {
													public void onClick(DialogInterface dialog,int id) {
														dialog.cancel();
													}
												});
												AlertDialog alertDialog = alertDialogBuilder.create();
												alertDialog.show();
											}	
										}

										@Override
										public void onCancelled(FirebaseError arg0) {

										}
									});
								}
							}
						}
					}
					break;
				}
			}
		});
	}
}
