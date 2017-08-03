package easymath.first.activities;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import easymath.first.util.FirebaseDBUtils;
import learningmath.first.R;

/**
 * Screen for showing students profile
 * Profile contains the student's levels and average answer time in tests.
 * The argument that passes to this activity is the student's username
 * @author liadpc
 */
public class ProfileActivity extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Handle context
		setContentView(R.layout.profile_screen);
		final Context context = this ;

		// Handle view and background
		View view = getWindow().getDecorView();
		Drawable myIcon = getResources().getDrawable(R.drawable.sky);
		view.setBackgroundDrawable(myIcon);
		
		// Show Add & Sub levels
		final TextView tvAdd = (TextView)findViewById(R.id.tvProfileAddDb);
		final TextView tvSub = (TextView)findViewById(R.id.tvProfileSubDb);
		// Show average answer time in tests
		final TextView tvAvgTime = (TextView)findViewById(R.id.tvProfileAvgTimeDb);
		
		// Fetch argument (student name)
		String passedArg = getIntent().getExtras().getString("arg");
		
		// Fetch athe student from DB
		Firebase studentRef = new Firebase(FirebaseDBUtils.getDatabaseURL() + "users/students");
		studentRef = studentRef.child(passedArg);
		// Receive events
		studentRef.addListenerForSingleValueEvent(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot snapShot) {
				// Fetch add level, sub level, avg time
				String addLevel =(String) snapShot.child("add").getValue();
				String subLevel =(String) snapShot.child("sub").getValue();
				String avgTime =(String) snapShot.child("avgtime").getValue();
				
				// Show the information in the text views
				tvAdd.setText(addLevel);
				tvSub.setText(subLevel);
				if (avgTime != null && avgTime != "") {
					tvAvgTime.setText(avgTime);
				}

				// Alignments for add level
				RelativeLayout parentView = (RelativeLayout)findViewById(R.id.relativeLayoutProfile);
				RelativeLayout.LayoutParams layAdd = new RelativeLayout.LayoutParams
						(250,250);
				layAdd.addRule(RelativeLayout.LEFT_OF , tvAdd.getId());
				layAdd.addRule(RelativeLayout.ALIGN_TOP , tvAdd.getId());

				// Set representing image for the add level
				ImageView imageAdd = new ImageView(context);
				if(addLevel.equals("1")){
					imageAdd.setImageResource(R.drawable.baby);
				}
				else if(addLevel.equals("2")) {
					imageAdd.setImageResource(R.drawable.kid);
				}
				else if(addLevel.equals("3")) {
					imageAdd.setImageResource(R.drawable.teenager);
				}
				else if(addLevel.equals("4")) {
					imageAdd.setImageResource(R.drawable.man);
				}
				else if(addLevel.equals("5")) {
					imageAdd.setImageResource(R.drawable.proffesor);
				}
				parentView.addView(imageAdd, layAdd);
				
				// Alignments for sub level
				RelativeLayout.LayoutParams laySub = new RelativeLayout.LayoutParams
						(250,250);
				laySub.addRule(RelativeLayout.LEFT_OF , tvSub.getId());
				laySub.addRule(RelativeLayout.ALIGN_TOP , tvSub.getId());

				// Set representing image for the sub level
				ImageView imageSub = new ImageView(context);
				if(subLevel.equals("1")){
					imageSub.setImageResource(R.drawable.baby);
				}
				else if(subLevel.equals("2")) {
					imageSub.setImageResource(R.drawable.kid);
				}
				else if(subLevel.equals("3")) {
					imageSub.setImageResource(R.drawable.teenager);
				}
				else if(subLevel.equals("4")) {
					imageSub.setImageResource(R.drawable.man);
				}
				else if(subLevel.equals("5")) {
					imageSub.setImageResource(R.drawable.proffesor);
				}
				parentView.addView(imageSub, laySub);
			}
			@Override
			public void onCancelled(FirebaseError arg0) {
			}
		});
	}
}

