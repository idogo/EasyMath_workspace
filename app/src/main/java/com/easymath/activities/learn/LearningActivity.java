package com.easymath.activities.learn;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.easymath.R;

/**
 * Screen for choosing to LEARN one of the following: ADD / SUB / ADDSUB - (by watching videos)
 * This screen is also used from the Add Exercise option for teachers, to choose the exercise category.
 * We can know the context from the arg passed.
 * @author liadpc
 */
public class LearningActivity extends Activity {
	
	String arg;
	String nextArg;
	
	private enum Subject {
	    LEARNING , ADD_EX}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.learn_main);
		
		// Handle view and background
		View view = getWindow().getDecorView();
		Drawable myIcon = getResources().getDrawable(R.drawable.sky);
		view.setBackgroundDrawable(myIcon);
		
		// "Add" button
		Button add = (Button)findViewById(R.id.btadd);
		add.setBackgroundResource(R.drawable.green);
		add.setTextSize(30);
		
		// "Substract" button
		Button substract = (Button)findViewById(R.id.btsubstract);
		substract.setBackgroundResource(R.drawable.green);
		substract.setTextSize(30);
		
		// "Add+Sub" button
		Button addSub = (Button)findViewById(R.id.btaddSub);
		addSub.setBackgroundResource(R.drawable.green);
		addSub.setTextSize(30);
		
		// The argument passed to this activity will help us know if we came here 
		// from "Add Exercise" (teacher) or the "Learn" option (stuent) - They both can use this activity for different purposes.
		final String passedArg = getIntent().getExtras().getString("arg");
		
		// "Add" clicked
		add.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				// Prepare the next activity - Difficulty
				Intent myIntent= null;
				myIntent = new Intent(LearningActivity.this, com.easymath.activities.DifficultyActivity.class);
				
				// Pass "Add Exercise" / "Learn Add" forward
				switch (Subject.valueOf(passedArg)) {
					case LEARNING : 
						arg = "LEARN_ADD";
					break;
					case ADD_EX : 
						arg = "ADD_EX";
					break;
				}
				myIntent.putExtra("arg",  getText());
				LearningActivity.this.startActivity(myIntent);
			}
		});
		
		// "Substract clicked"
		substract.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				// Prepare the next activity - Difficulty
				Intent myIntent= null;
				myIntent = new Intent(LearningActivity.this, com.easymath.activities.DifficultyActivity.class);
				
				// Pass "Add Exercise" / "Learn Sub" forward
				switch (Subject.valueOf(passedArg)) {
					case LEARNING : 
						arg = "LEARN_SUB";
					break;
					case ADD_EX : 
						arg = "SUB_EX";
					break;
				}
				myIntent.putExtra("arg",  getText());
				LearningActivity.this.startActivity(myIntent);
			}
		});

		// "Add+Sub" clicked
		addSub.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent myIntent= null;
				
				
				switch (Subject.valueOf(passedArg)) {
					case LEARNING : 
						// If we came here to "Learn", go to the video.
						arg = "LEARN_ADDSUB";
						myIntent= new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.youtube.com/watch?v=boYXiNVtm50"));
					break;
					case ADD_EX : 
						// If we came here to  "Add Exercise", go to the AddEx activity.
						nextArg= "ADDSUB";
						myIntent = new Intent(LearningActivity.this, com.easymath.activities.AddExScreenActivity.class);
						myIntent.putExtra("nextArg", getText());
					break;
				}
				LearningActivity.this.startActivity(myIntent);
			}
		});
		
	}
	
	public String getText() {
		return arg;
	}
	public String getNextText() {
		return nextArg;
	}	
	
}
