package com.easymath.activities;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.Exchanger;

import com.easymath.util.Constants;
import com.easymath.util.PropertiesUtil;
import com.firebase.client.Firebase;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.easymath.activities.test.TestActivity;
import com.easymath.common.Questions;
import com.easymath.R;

/**
 * Screen for explaining a question to the student using visualization of the question.
 * 3 elements questions will be broken down to 2 small parts.
 * @author liadpc
 */
@SuppressLint("ClickableViewAccessibility")
public class QuestionExplanationActivity extends Activity{

	final int ADDTVID = 100; 
	final int ADDDROPID = 101; 
	final int SUBTVID = 200;
	private static MediaPlayer mediaPlayer;

	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Handle Context
		Firebase.setAndroidContext(this);
		setContentView(R.layout.question_explenation);
		Context context = this;
		
		boolean isAddSubQuestion = false;
		boolean isFirstHalf = false;
		
		// Handle view and background
		View view = getWindow().getDecorView();
		Drawable myIcon = getResources().getDrawable( R.drawable.sky );
		view.setBackgroundDrawable(myIcon);
		
		// Finish button
		Button finish = (Button)findViewById(R.id.btnFinishExp);
		
		// Show half exercise for 3 elements questions
		TextView halfExercise = (TextView)findViewById(R.id.tvQuestionExpExc);
		halfExercise.setTextColor(Color.parseColor(Constants.WHITE_COLOR));
		halfExercise.setTextSize(50);
		
		// Show the exercise for regular questions
		TextView allExercise = (TextView)findViewById(R.id.tvAllQuestions);
		allExercise.setTextColor(Color.parseColor(Constants.WHITE_COLOR));
		allExercise.setTextSize(70);
		finish.setVisibility(View.INVISIBLE);
		finish.setClickable(false);

		// Draw the needed game animations, depends on the question subject
		if ((Questions.subject.equals("add")) || Questions.subject.equals("add3") ) {
			allExercise.setVisibility(View.GONE);
			halfExercise.setText(Questions.question);
			visualizeAddGame(context, finish , halfExercise , isFirstHalf , isAddSubQuestion);
		}
		if ((Questions.subject.equals("sub")) || Questions.subject.equals("sub3")) {
			allExercise.setVisibility(View.GONE);
			halfExercise.setText(Questions.question);
			visualizeSubGame(context ,finish ,halfExercise , isFirstHalf , isAddSubQuestion);
		}
		if(Questions.subject.equals("addSub")) {
			allExercise.setText(Questions.question);
			visualizeAddSubGame(context , finish , halfExercise , allExercise);
		}


	}

	/**
	 * Visualized animated game for AddSub questions (split to 2 half games)
	 */
	private void visualizeAddSubGame(Context context, Button finish, TextView exercise , TextView allExercise) {
		
		// Split by spaces
		String[] parts = Questions.question.split("\\b");
		boolean firstHalf = true;
		
		// If the half is a "add" question, load "add" game.
		if(parts[2].equals("+")){
			String halfExercise = parts[1] + parts[2] +parts[3];
			exercise.setText(halfExercise);
			visualizeAddGame(context, finish, exercise , firstHalf , true);
		}
		else { // load the "sub" game
			String halfExercise = parts[1] + parts[2] +parts[3];
			exercise.setText(halfExercise);
			visualizeSubGame(context, finish, exercise, firstHalf , true);
		}
	}


	/**
	 * Visualized animated game for Sub questions
	 */
	@SuppressWarnings("ResourceType")
	private void visualizeSubGame( final Context context, final Button finish, final TextView exercise, final boolean firstHalf, boolean addSub) {
		
		// Radomise picture (balloon, flower etc..)
		Random r = new Random();
		int randomPic = r.nextInt(1) + 1;
		
		// Split the numbers in the question
		final String[] parts = exercise.getText().toString().split("[-]");

		try {
			// Alert with game rules
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
			alertDialogBuilder.setTitle("עזרה");
			alertDialogBuilder.setMessage("במשחק זה עלייך לפוצץ את מספר הבלונים שצריך להחסיר").setCancelable(false).setPositiveButton(PropertiesUtil.getOkMessage(), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		}catch (Exception e){
			e.printStackTrace();
		}
		// Remember the amount of balloons in the Questions structure
		Questions.questionExplanationVar = Integer.parseInt(parts[0]);
		
		// In case of an AddSub question (3 elements)
		if(addSub && !firstHalf) {
			// Set invisible utility objects
			ImageView delete = (ImageView) findViewById(ADDDROPID);
			TextView tvDelete = (TextView)findViewById(ADDTVID);
			tvDelete.setVisibility(View.GONE);
			delete.setVisibility(View.GONE);
		}
		
		// Alignments
		RelativeLayout parentView = (RelativeLayout)findViewById(R.id.relativeLayout);
		RelativeLayout.LayoutParams lay = new RelativeLayout.LayoutParams(150,150);
		lay.addRule(RelativeLayout.BELOW , exercise.getId());
		lay.addRule(RelativeLayout.ALIGN_LEFT);

		// Show question text
		final TextView t = new TextView(context);
		t.setGravity(Gravity.CENTER);
		t.setTextColor(Color.parseColor(Constants.WHITE_COLOR));
		t.setTextSize(35f);
		t.setId(SUBTVID);
		t.setText(String.valueOf(Questions.questionExplanationVar));
		
		// Alignments
		RelativeLayout.LayoutParams pText = new RelativeLayout.LayoutParams(200, 200);
		pText.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		pText.addRule(RelativeLayout.CENTER_HORIZONTAL);
		parentView.addView(t, pText);

		// Show the first image
		ImageView imageView = new ImageView(context);
		imageView.setId(1);
		if(randomPic == 1) {
			imageView.setImageResource(R.drawable.a);
		}
		else {
			imageView.setImageResource(R.drawable.b1);
		}
		
		// On image click
		imageView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
		
				// Fadeout animation
				Animation fadeOut = new AlphaAnimation(1, 0);
				fadeOut.setInterpolator(new AccelerateInterpolator());
				fadeOut.setDuration(1000);
				v.startAnimation(fadeOut);   
				v.setVisibility(View.INVISIBLE);
				
				// Update the amount of balloons with one less
				Questions.questionExplanationVar --;
				t.setText(String.valueOf(Questions.questionExplanationVar));
				
				// Play a sound
				try {
		        	mediaPlayer.stop();
		        	mediaPlayer.release();
		        	mediaPlayer=null;
		        } catch (Exception e) {e.printStackTrace();}; 
				mediaPlayer = MediaPlayer.create(context, R.raw.pop3);
				try {
					mediaPlayer.prepare();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				mediaPlayer.start();

				// If the answer is reached
				if(Questions.questionExplanationVar == TestActivity.getAns(exercise.getText().toString())) {
					// If it's the end of the question
					if(!firstHalf) {
						try {
							// Alert and finish
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
							alertDialogBuilder.setMessage(" התשובה הנכונה היא " + Questions.ans).setCancelable(false).setPositiveButton(PropertiesUtil.getOkMessage(), new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									finish();
								}
							});
							AlertDialog alertDialog = alertDialogBuilder.create();
							alertDialog.show();
						}catch (Exception e){
							e.printStackTrace();
						}
					}
					else{

						try {
							// There is 1 more half
							// Alert and go to next half explanation
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
							alertDialogBuilder.setMessage(" התשובה הנכונה היא " + TestActivity.getAns(exercise.getText().toString())).setCancelable(false).setPositiveButton(PropertiesUtil.getOkMessage(), new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									// Get second half
									String[] secondHalfQuestion = Questions.question.split("\\b");
									// The second half is the sum of the first half and parts 4,5
									String halfExercise = Questions.questionExplanationVar + secondHalfQuestion[4] + secondHalfQuestion[5];
									// Show the question
									exercise.setText(halfExercise);
									// Start another game for the second half with "false" for the boolean firstHalf
									// (Add game because this part was Sub game so next one must be Add)
									visualizeAddGame(context, finish, exercise, false, true);
								}
							});
							AlertDialog alertDialog = alertDialogBuilder.create();
							alertDialog.show();
						}catch (Exception e){
							e.printStackTrace();
						}
					}
				}
			}
		});
		parentView.addView(imageView , lay);

		// Show the rest of the images
		for (int i=2 ; i < Integer.parseInt(parts[0])+1 ; i++) {

			// Alignments
			RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(150, 150);
			if((i-1)%7!=0){
				p.addRule(RelativeLayout.ALIGN_TOP , imageView.getId());
				p.addRule(RelativeLayout.RIGHT_OF, imageView.getId());
			}
			else
			{
				p.addRule(RelativeLayout.BELOW , imageView.getId());
				p.addRule(RelativeLayout.ALIGN_LEFT);
			}
			
			// Show image
			imageView = new ImageView(context);
			imageView.setId(i);
			if(randomPic == 1) {
				imageView.setImageResource(R.drawable.a);
			}
			else {
				imageView.setImageResource(R.drawable.b1);
			}
			
			// On image click
			imageView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// Fadeout animation
					Animation fadeOut = new AlphaAnimation(1, 0);
					fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
					fadeOut.setDuration(1000);
					v.startAnimation(fadeOut);
					v.setVisibility(View.INVISIBLE);
					
					// Update the amount of balloons with one less
					Questions.questionExplanationVar --;
					t.setText(String.valueOf(Questions.questionExplanationVar));

					// Play a sound
					try {
			        	mediaPlayer.stop();
			        	mediaPlayer.release();
			        	mediaPlayer=null;
			        } catch (Exception e) {e.printStackTrace();}; 
					mediaPlayer = MediaPlayer.create(context, R.raw.pop3);
					try {
						mediaPlayer.prepare();
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					mediaPlayer.start();
					
					// If the answer is reached
					if(Questions.questionExplanationVar == TestActivity.getAns(exercise.getText().toString())) {
						// If it's the end of the question
						if(!firstHalf) {
							try {
								AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
								alertDialogBuilder.setMessage(" התשובה הנכונה היא " + Questions.ans).setCancelable(false).setPositiveButton(PropertiesUtil.getOkMessage(), new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										// finish
										finish();
									}
								});
								AlertDialog alertDialog = alertDialogBuilder.create();
								alertDialog.show();
							}catch (Exception e){
								e.printStackTrace();
							}
					}
					else { // There is one more half
						// Alert and go to next half explanation
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(	context);
							try {
								alertDialogBuilder.setMessage(" התשובה של החלק הראשון היא " + TestActivity.getAns(exercise.getText().toString())).setCancelable(false).setPositiveButton(PropertiesUtil.getOkMessage(), new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										// Show the question
										String[] secondHalfQuestion = Questions.question.split("\\b");
										String halfExercise = Questions.questionExplanationVar + secondHalfQuestion[4] + secondHalfQuestion[5];
										exercise.setText(halfExercise);
										// Start another game for the second half with "false" for the boolean firstHalf
										// (Add game because this part was Sub game so next one must be Add)
										visualizeAddGame(context, finish, exercise, false, true);
									}
								});
								AlertDialog alertDialog = alertDialogBuilder.create();
								alertDialog.show();
							}catch (Exception e){
								e.printStackTrace();
							}
						}
					}
				}
			});

			parentView.addView(imageView, p);
		}


	}

	/**
	 * Visualized animated game for Add questions
	 */
	@SuppressWarnings("ResourceType")
	private void visualizeAddGame(final Context context, final Button finish, final TextView exercise, final boolean firstHalf, boolean addSub) {
		// Radomise picture (balloon, flower etc..)
		Random r = new Random();
		int randomPic = r.nextInt(2) + 1;

		// Split the numbers in the question
		final String[] parts = exercise.getText().toString().split("[+]");
		Questions.questionExplanationVar = 0;	
		
		// Alert with game rules
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setTitle("עזרה");
		
		// Prepare text for game rules
		if(randomPic == 1) {
			alertDialogBuilder.setMessage("במשחק זה עלייך לגרור את כל הבלונים לתוך הקופסא. מתחת לקופסא נספרים הבלונים שהכנסת").setCancelable(false);
		}
		else {
			alertDialogBuilder.setMessage("במשחק זה עלייך לגרור את כל הפרפרים לפרח. מתחת לפרח נספרים הפרפרים שהגיעו").setCancelable(false);
		}
		try {
			alertDialogBuilder.setPositiveButton(PropertiesUtil.getOkMessage(), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});
		}catch (Exception e){
			e.printStackTrace();
		}
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

		// In case of an AddSub question (3 elements)
		if(addSub && !firstHalf) {
			TextView tvDelete = (TextView)findViewById(SUBTVID);
			// Set invisible utility objects
			tvDelete.setVisibility(View.GONE);
			String fullQuestion = Questions.question;
			ImageView imageDelete = (ImageView)findViewById(1);
			imageDelete.setVisibility(View.GONE);
			final String[] firstNumber = fullQuestion.split("[-]");
			for (int i=2 ; i < Integer.parseInt(firstNumber[0])+1 ; i++){
				imageDelete = (ImageView)findViewById(i);
				imageDelete.setVisibility(View.GONE);
			}
		}
		
		// Alignments
		RelativeLayout parentView = (RelativeLayout)findViewById(R.id.relativeLayout);
		RelativeLayout.LayoutParams lay = new RelativeLayout.LayoutParams(150,150);
		lay.addRule(RelativeLayout.BELOW , exercise.getId());
		lay.addRule(RelativeLayout.ALIGN_LEFT);
		
		// Show first image
		ImageView imageView = new ImageView(context);
		imageView.setId(1);
		if(randomPic == 1) {
			imageView.setImageResource(R.drawable.a);
		}
		else {
			imageView.setImageResource(R.drawable.b1);
		}
		
		// Set the image as LongClickable so we can drag it
		imageView.setLongClickable(true);
		
		// Drag image
		imageView.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					ClipData data = ClipData.newPlainText("", "");
					View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
					view.startDrag(data, shadowBuilder, view, 0);
					return true;
				}
				else {
					return false;
				}
			}
		}); 
		parentView.addView(imageView , lay);

		// Show the rest of the images
		for (int i=2 ; i < TestActivity.getAns(exercise.getText().toString())+1 ; i++) {

			// Alignments
			RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(150, 150);
			if((i-1)%7!=0){
				p.addRule(RelativeLayout.ALIGN_TOP , imageView.getId());
				p.addRule(RelativeLayout.RIGHT_OF, imageView.getId());
			}
			else
			{
				p.addRule(RelativeLayout.BELOW , imageView.getId());
				p.addRule(RelativeLayout.ALIGN_LEFT);
			}

			// Show image
			imageView = new ImageView(context);
			imageView.setId(i);
			// If the image represents the first number in the question (for the color of the balloon)
			if(i <= Integer.parseInt(parts[0])) {
				if(randomPic == 1)
					imageView.setImageResource(R.drawable.a);
				else
					imageView.setImageResource(R.drawable.b1);
			}
			else {// If this image represents the second number in the question (different color balloon)
				if(Questions.subject.equals("add")) {
					if (randomPic == 1)
						imageView.setImageResource(R.drawable.b);
					else
						imageView.setImageResource(R.drawable.b2);
				}
				else { // If this image represents the third number in the question (different color balloon)
					if( i<= Integer.parseInt(parts[1])+Integer.parseInt(parts[0]) ) {
						if(randomPic == 1)
							imageView.setImageResource(R.drawable.b);
						else
							imageView.setImageResource(R.drawable.b2);
					}
					else { // If this image represents the forth number in the question (different color balloon)
						if(randomPic == 1)
							imageView.setImageResource(R.drawable.c);
						else
							imageView.setImageResource(R.drawable.b3);
					}
				}

			}
			parentView.addView(imageView, p);

			// Drag image
			imageView.setOnTouchListener(new View.OnTouchListener() {

				@Override
				public boolean onTouch(View view, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						ClipData data = ClipData.newPlainText("", "");
						View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
						view.startDrag(data, shadowBuilder, view, 0);
						return true;
					}
					else {
						return false;
					}
				}
			});
		}
		
		// Show question text
		final TextView t = new TextView(context);
		t.setGravity(Gravity.CENTER);
		t.setId(ADDTVID);
		t.setTextColor(Color.parseColor(Constants.WHITE_COLOR));
		t.setTextSize(35f);
		
		// Image for the box to drop the balloons at
		imageView = new ImageView(context);
		imageView.setId(ADDDROPID);
		t.setTextSize(30f);
		t.setText(String.valueOf(Questions.questionExplanationVar));
		if(randomPic == 1)
			imageView.setImageResource(R.drawable.box);
		else 
			imageView.setImageResource(R.drawable.f);
		
		// Alignments
		RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(200, 200);
		p.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		p.addRule(RelativeLayout.CENTER_HORIZONTAL);
		parentView.addView(t, p);
		int lastImageId = imageView.getId();
		RelativeLayout.LayoutParams p2 = new RelativeLayout.LayoutParams(300, 300);
		p2.addRule(RelativeLayout.BELOW , lastImageId);
		p2.addRule(RelativeLayout.ABOVE , t.getId());
		p2.addRule(RelativeLayout.CENTER_HORIZONTAL);
		parentView.addView(imageView, p2);
		parentView.requestLayout();
		
		// Play sound when object is dropped at the place (balloon got to the box)
		imageView.setOnDragListener(new View.OnDragListener() {

			@Override
			public boolean onDrag(View v, DragEvent event) {
				switch (event.getAction()) {
					case DragEvent.ACTION_DROP:
						// Dropped, reassign View to invisible
						ImageView view = (ImageView) event.getLocalState();
						view.setVisibility(View.INVISIBLE);
						
						// Update the amount of balloons to one more
						Questions.questionExplanationVar++;
						t.setText(String.valueOf(Questions.questionExplanationVar));
						
						// Play a sound
						try {
				        	mediaPlayer.stop();
				        	mediaPlayer.release();
				        	mediaPlayer=null;
				        } catch (Exception e) {e.printStackTrace();}; 
						mediaPlayer = MediaPlayer.create(context, R.raw.click3);
						try {
							mediaPlayer.prepare();
						} catch (IllegalStateException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						mediaPlayer.start();
						
						// If we reached the answer
						if(Questions.questionExplanationVar == TestActivity.getAns(exercise.getText().toString())) {
							// If the question is finished
							if(!firstHalf) {
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
								try {
									alertDialogBuilder.setMessage(" התשובה הנכונה היא " + Questions.ans).setCancelable(false).setPositiveButton(PropertiesUtil.getOkMessage(), new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int id) {
											finish();
										}
									});
								}catch (Exception e){
									e.printStackTrace();
								}
							AlertDialog alertDialog = alertDialogBuilder.create();
							alertDialog.show();
							}
							else {
								try {
									// There is one more half
									// Alert and go to next half explanation
									AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
									alertDialogBuilder.setMessage(" התשובה של החלק הראשון היא " + TestActivity.getAns(exercise.getText().toString())).setCancelable(false)
											.setPositiveButton(PropertiesUtil.getOkMessage(), new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog, int id) {
													// Show the question
													String[] secondHalfQuestion = Questions.question.split("\\b");
													String halfExercise = Questions.questionExplanationVar + secondHalfQuestion[4] + secondHalfQuestion[5];
													exercise.setText(halfExercise);
													// Start another game for the second half with "false" for the boolean firstHalf
													// (Sub game because this part was Add game so next one must be Sub)
													visualizeSubGame(context, finish, exercise, false, true);
												}
											});
									AlertDialog alertDialog = alertDialogBuilder.create();
									alertDialog.show();
								}catch (Exception e){
									e.printStackTrace();
								}
								
							}
						}
						break;
					default:
						break;
					}
					return true;
				}
		});
	}
}
