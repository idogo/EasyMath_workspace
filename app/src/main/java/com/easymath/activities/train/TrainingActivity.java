package com.easymath.activities.train;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.easymath.R;

/**
 *	Screen for choosing what to train: ADD / SUBTRACT / ADD & SUBSTRACT 
 * @author liadpc
 */
public class TrainingActivity extends Activity {
	String arg;


	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.learn_main);
		
		// Handle view and background
		View view = getWindow().getDecorView();
		Drawable myIcon = getResources().getDrawable(R.drawable.sky);
		view.setBackgroundDrawable(myIcon);
		
		// "Add" option
		Button add = (Button)findViewById(R.id.btadd);
		add.setBackgroundResource(R.drawable.green);
		add.setTextSize(30);
		
		// "Subtract" option
		Button substract = (Button)findViewById(R.id.btsubstract);
		substract.setBackgroundResource(R.drawable.green);
		substract.setTextSize(30);
		
		// "AddSub" option
		Button addSub = (Button)findViewById(R.id.btaddSub);
		addSub.setBackgroundResource(R.drawable.green);
		addSub.setTextSize(30);
		
		// Clicked "Add"
		add.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Redirect to choose difficulty (DifficultyActivity)
				Intent myIntent= null;
				myIntent = new Intent(TrainingActivity.this, com.easymath.activities.DifficultyActivity.class);
				// Set argument so the next activity knows we came from Training Screen
				arg = "TRAIN_ADD";
				myIntent.putExtra("arg",  getText());
				TrainingActivity.this.startActivity(myIntent);
			}
		});
		
		// Clicked "Subtract"
		substract.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Redirect to choose difficulty (DifficultyActivity)
				Intent myIntent= null;
				myIntent = new Intent(TrainingActivity.this, com.easymath.activities.DifficultyActivity.class);
				// Set argument so the next activity knows we came from Training Screen
				arg = "TRAIN_SUB";
				myIntent.putExtra("arg",  getText());
				TrainingActivity.this.startActivity(myIntent);
			}
		});
		
		// Clicked "AddSub"
		addSub.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Redirect to choose difficulty (DifficultyActivity)
				Intent myIntent= null;
				myIntent = new Intent(TrainingActivity.this, com.easymath.activities.DifficultyActivity.class);
				// Set argument so the next activity knows we came from Training Screen
				arg = "TRAIN_ADDSUB";
				myIntent.putExtra("arg",  getText());
				TrainingActivity.this.startActivity(myIntent);
			}
		});
		
	}
	
	public String getText() {
		return arg;
	}
	
}