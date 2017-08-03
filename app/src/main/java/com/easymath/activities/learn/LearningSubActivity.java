package com.easymath.activities.learn;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.easymath.R;

/**
 * Screen for choosing the difficulty when learning SUB - basic / 3 elements / up to 20
 * @author liadpc
 */
public class LearningSubActivity extends Activity {

	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.learnsub_main);

		// Handle view and background
		View view = getWindow().getDecorView();
		Drawable myIcon = getResources().getDrawable(R.drawable.sky);
		view.setBackgroundDrawable(myIcon);
		
		// "Basic" button
		Button basic = (Button)findViewById(R.id.btbasicadd);
		basic.setBackgroundResource(R.drawable.green);
		basic.setTextSize(30);
		
		// "3 Elements" button
		Button threeElements = (Button)findViewById(R.id.btadd3elements);
		threeElements.setBackgroundResource(R.drawable.green);
		threeElements.setTextSize(30);
		
		// "Up to 20" button
		Button upto20 = (Button)findViewById(R.id.btaddupto20);
		upto20.setBackgroundResource(R.drawable.green);
		upto20.setTextSize(20);
		
		// "Basic" clicked
		basic.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Go to video
				Intent myIntent= new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.youtube.com/watch?v=ghzikfMix20"));
				startActivity(myIntent);
			}
		});
		
		// "Up to 20" clicked 
		upto20.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Go to video
				Intent myIntent= new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.youtube.com/watch?v=KBFzM53J3XM"));
				startActivity(myIntent);
			}
		});
		
		// "3 Elements" clicked
		threeElements.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Go to video
				Intent myIntent= new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.youtube.com/watch?v=pOhLpgoJVSA"));
				startActivity(myIntent);
			}
		});
		
		
	}
}
