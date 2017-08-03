package easymath.first.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import easymath.first.common.User;
import learningmath.first.R;

/**
 * The menu screen for students
 * @author liadpc
 */
public class MenuActivity extends Activity {
	
	String arg;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_main);
		
		// Handle view and background
		View view = getWindow().getDecorView();
		Drawable myIcon = getResources().getDrawable(R.drawable.classbackground);
		view.setBackgroundDrawable(myIcon);
				
		// Set the buttons and texts for the actions: Learn / Train / Test
		TextView text1 = (TextView)findViewById(R.id.tvLearn);
		TextView text2 = (TextView)findViewById(R.id.tvTrain);
		TextView text3 = (TextView)findViewById(R.id.tvTest);
		text1.setTextColor(Color.parseColor("white"));
		text2.setTextColor(Color.parseColor("white"));
		text3.setTextColor(Color.parseColor("white"));
		
		ImageButton learning = (ImageButton)findViewById(R.id.btnlearning);
		ImageButton training = (ImageButton) findViewById(R.id.btntraining);
		ImageButton test = (ImageButton) findViewById(R.id.btntest);
		
		// Set the Profile and Help buttons
		ImageButton profile  = (ImageButton) findViewById(R.id.btnProfile);
		ImageButton help = (ImageButton) findViewById(R.id.btnHelpStud);

		// Clicked "Learn"
		learning.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent myIntent= null;
				// Prepare arguemnt for new activity
				arg = "LEARNING";
				// Redirect to LearningActivity
				myIntent = new Intent(MenuActivity.this, easymath.first.activities.learn.LearningActivity.class);
				myIntent.putExtra("arg",  getText());
				MenuActivity.this.startActivity(myIntent);
			}
		});
		
		// Clicked "Help"
		help.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Redirect to HelpActivity
				Intent myIntent= null;
				myIntent = new Intent(MenuActivity.this, easymath.first.activities.HelpActivity.class);
				MenuActivity.this.startActivity(myIntent);
				

			}
		});
		
		// Clicked "Train"
		training.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Redirect to TrainingActivity
				Intent myIntent= null;
				myIntent = new Intent(MenuActivity.this, easymath.first.activities.train.TrainingActivity.class);
				MenuActivity.this.startActivity(myIntent);
			}
		});
		
		// Clicked "Profile"
		profile.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Prepare arguemnt for new activity
				arg = User.userName;
				// Redirect to ProfileActivity
				Intent myIntent= null;
				myIntent = new Intent(MenuActivity.this, easymath.first.activities.ProfileActivity.class);
				myIntent.putExtra("arg",  getText());
				MenuActivity.this.startActivity(myIntent);
			}
		});
		
		// Clicked "Test"
		test.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Redirect to TestActivity
				Intent myIntent= null;
				myIntent = new Intent(MenuActivity.this, easymath.first.activities.test.TestActivity.class);
				MenuActivity.this.startActivity(myIntent);
			}
		});
		
	}
	
	private String getText() {
		return arg;
	}
	
	/**
	 * When exiting the app
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
		
	    if (keyCode == KeyEvent.KEYCODE_BACK ) {
	    	// Alert "Are you sure you want to exit?" message
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			alertDialogBuilder.setTitle("לצאת?");
			alertDialogBuilder.setMessage("האם ברצונך לצאת מהאפליקציה").setCancelable(false).setPositiveButton("יציאה",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
					finish();
				}
			}).setNegativeButton("רוצה להישאר", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();

				}
			});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
}
