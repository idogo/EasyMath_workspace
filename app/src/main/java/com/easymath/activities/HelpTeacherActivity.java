package com.easymath.activities;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.widget.TextView;

import com.easymath.util.PropertiesUtil;

import java.io.IOException;

/**
 * Help teacher screen
 * @author liadpc
 */
public class HelpTeacherActivity extends Activity {


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		TextView  tv=new TextView(this);
		tv.setTextSize(25);
		tv.setMovementMethod(new ScrollingMovementMethod());
		tv.setGravity(Gravity.CENTER_VERTICAL);
		try {
			tv.setText(PropertiesUtil.getTeacherHelp());
		} catch (IOException e) {
			e.printStackTrace();
		}
		setContentView(tv);

	}
}
