package easymath.first.activities;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import easymath.first.common.User;
import easymath.first.util.FirebaseDBUtils;
import learningmath.first.R;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Main activity is the first activity the loads up.
 * In this screen users can register/login as students/teachers
 * @author liadpc
 */
public class MainActivity extends Activity {

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Handle context
		Firebase.setAndroidContext(this);
		setContentView(R.layout.activity_main);
		final Context context = this;

		// Handle view and background
		View view = getWindow().getDecorView();
		Drawable myIcon = getResources().getDrawable(R.drawable.classbackground);
		view.setBackgroundDrawable(myIcon);
		
		// Make sure the device has internet access
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		if( activeNetworkInfo == null) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					context);
			alertDialogBuilder.setTitle("אין חיבור");
			alertDialogBuilder.setMessage("האפליקציה דורשת חיבור אינטרנט, ולפלאפון אין חיבור זמין כרגע").setCancelable(false).setPositiveButton("יציאה",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
					finish();
				}
			});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		}

		// Connect button
		Button connect = (Button) findViewById(R.id.btconnect);
		connect.setBackgroundResource(R.drawable.green);
		connect.setTextSize(20);
		connect.setTextColor(Color.parseColor("black"));
		
		// Register new user button
		Button newUser = (Button) findViewById(R.id.btnewUser);
		newUser.setBackgroundResource(R.drawable.green);
		newUser.setTextSize(20);
		newUser.setTextColor(Color.parseColor("black"));
		
		// Spinner (dropdown list) to choose teacher or student
		Spinner spinner = (Spinner) findViewById(R.id.spinnerStudTeacher);
		List<String> list = new ArrayList<String>();
		list.add("מורה");
		list.add("תלמיד");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);

		// Clicked connect
		connect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Connect to DB with connection string
				Firebase databaseReference = new Firebase(FirebaseDBUtils.getDatabaseURL());
				Firebase objectRef;
				
				// Get user and password from text views
				final TextView userName = (TextView) findViewById(R.id.tfuserName);
				final TextView password = (TextView) findViewById(R.id.tfpassword);
				// Get user type from spinner (teacher or student)
				final Spinner spinner = (Spinner) findViewById(R.id.spinnerStudTeacher);
				final String userKind = spinner.getSelectedItem().toString();
				
				// Fetch the needed objects from the DB
				if(userKind.equals("מורה")) {
					objectRef = databaseReference.child("users/teachers");
				}
				else
				{
					objectRef = databaseReference.child("users/students");
				}

				// Receive events
				objectRef.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot snapshot) {
						
						// Authenticate user and passoword
						final String userNameString = userName.getText().toString().trim();
						final String passwordString = password.getText().toString();

						// If a user with this name exists
						if (snapshot.child(userNameString).exists()) {
							DataSnapshot data = snapshot.child(userNameString);
							String password = (String) data.child("password").getValue();
							// If the passwird doesn't match
							if(!password.equals(passwordString)){
								AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
								alertDialogBuilder.setTitle("סיסמא שגויה");
								alertDialogBuilder.setMessage("סיסמא לא נכונה, אנא נסה שוב").setCancelable(false).setPositiveButton("אישור",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										dialog.cancel();
									}
								});
								AlertDialog alertDialog = alertDialogBuilder.create();
								alertDialog.show();
							}
							// Password matches, connect
							else{
								AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
								alertDialogBuilder.setTitle("התחברות");
								alertDialogBuilder.setMessage("התחברת בהצלחה").setCancelable(false).setPositiveButton("אישור",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										// Redirect students to the student menu
										if (userKind.equals("תלמיד")){
											User.userName = userNameString;
											User.password = passwordString;
											Intent myIntent = null;
											MainActivity.this.finish();
											myIntent = new Intent(MainActivity.this,easymath.first.activities.MenuActivity.class);
											MainActivity.this.startActivity(myIntent); }
										else {
											// Redirect teachers to the teachers menu
											User.userName = userNameString;
											User.password = passwordString;
											Intent myIntent = null;
											MainActivity.this.finish();
											myIntent = new Intent(MainActivity.this,easymath.first.activities.TeacherMenuActivity.class);
											MainActivity.this.startActivity(myIntent);
										}
									}
								});
								AlertDialog alertDialog = alertDialogBuilder.create();
								alertDialog.show();
							}

						}
						// User doesn't exist
						else {
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
							alertDialogBuilder.setTitle("משתמש לא קיים");
							alertDialogBuilder.setMessage("שם משתמש לא קיים, אנא נסה שוב").setCancelable(false).setPositiveButton("אישור",new DialogInterface.OnClickListener() {
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
		});
		
		// Clicked New User
		newUser.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Connect to DB with connection string
				Firebase databaseReference = new Firebase(FirebaseDBUtils.getDatabaseURL());
				final Firebase objectRef ;
				
				// Get user and password input
				final TextView userName = (TextView) findViewById(R.id.tfuserName);
				final TextView password = (TextView) findViewById(R.id.tfpassword);
				final TextView verifyPassword = (TextView) findViewById(R.id.tfverifypassword);
				
				// Get user type from spinner (teacher or student)
				final Spinner spinner = (Spinner) findViewById(R.id.spinnerStudTeacher);
				final String userKind = spinner.getSelectedItem().toString();
				if(userKind.equals("מורה")) {
					objectRef = databaseReference.child("users/teachers");
				}
				else
				{
					objectRef = databaseReference.child("users/students");
				}

				// Receive events
				objectRef.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot snapshot) {
						
						// Register the user
						final String userNameString = userName.getText().toString().trim();
						final String passwordString = password.getText().toString();
						final String verifyPasswordString = verifyPassword.getText().toString();

						// If this username is already used
						if(snapshot.child(userNameString).exists()){
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
							alertDialogBuilder.setTitle("משתמש קיים");
							alertDialogBuilder.setMessage("שם המשתמש כבר קיים במערכת, אנא נסה שם אחר").setCancelable(false).setPositiveButton("אישור",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									dialog.cancel();
								}
							});
							AlertDialog alertDialog = alertDialogBuilder.create();
							alertDialog.show();
						}
						// Username isn't used so we can register it
						else {
							// Validate passowrd
							if (passwordString.length()==0) {
								AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
										context);
								alertDialogBuilder.setTitle("סיסמא לא חוקית");
								alertDialogBuilder.setMessage("סיסמא לא יכולה להיות ריקה, אנא הוסף סיסמא").setCancelable(false).setPositiveButton("אישור",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										dialog.cancel();
									}
								});
								AlertDialog alertDialog = alertDialogBuilder.create();
								alertDialog.show();
							}
							// Check that the passowrd equals verifyPassword
							else if(!passwordString.equals(verifyPasswordString)) {
								AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
										context);
								alertDialogBuilder.setTitle("אימות סיסמא שגוי");
								alertDialogBuilder.setMessage("הסיסמא ואימות הסיסמא לא זהים").setCancelable(false)
								.setPositiveButton("אישור",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										dialog.cancel();
									}
								});
								AlertDialog alertDialog = alertDialogBuilder.create();
								alertDialog.show();
							}
							// Everything is okay, register the new user
							else {
								// Create a DB user object
								Firebase newRef = objectRef.child(userNameString);
								// Set username and password
								newRef.child("userName").setValue(userNameString);
								newRef.child("password").setValue(passwordString);
								
								// Extra information for students
								if(userKind.equals("תלמיד")) {
									newRef.child("teacherName").setValue("-1");
									newRef.child("add").setValue("0");
									newRef.child("sub").setValue("0");
									newRef.child("numoftests").setValue("0");
									newRef.child("avgtime").setValue("0");
								}

								// Alert
								AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
								alertDialogBuilder.setTitle("התחברות");
								alertDialogBuilder.setMessage("המשתמש התווסף בהצלחה, התחברת למערכת").setCancelable(false).setPositiveButton("אישור",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										// Keep the connected user in the User stucture
										User.userName = userNameString;
										User.password = passwordString;
										if (userKind.equals("תלמיד")){
											// Redirect to MenuActivity for students
											Intent myIntent = null;
											MainActivity.this.finish();
											myIntent = new Intent(MainActivity.this,easymath.first.activities.MenuActivity.class);
											MainActivity.this.startActivity(myIntent); }
										else {
											// Redirect to TeacherMenuActivity for teachers
											Intent myIntent = null;
											MainActivity.this.finish();
											myIntent = new Intent(MainActivity.this,easymath.first.activities.TeacherMenuActivity.class);
											MainActivity.this.startActivity(myIntent);
										}
									}
								});
								AlertDialog alertDialog = alertDialogBuilder.create();
								alertDialog.show();
							}
						}
					}
					@Override
					public void onCancelled(FirebaseError arg0) {
					}
				});
			}
		});
	}
}