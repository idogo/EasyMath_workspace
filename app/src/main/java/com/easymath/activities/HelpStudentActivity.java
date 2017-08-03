package com.easymath.activities;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Help student screen
 * @author liadpc
 */
public class HelpStudentActivity extends Activity {


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		TextView  tv=new TextView(this);
		tv.setTextSize(25);
		tv.setMovementMethod(new ScrollingMovementMethod());
		tv.setGravity(Gravity.CENTER_VERTICAL);
		tv.setText("ברוכים הבאים לאפליקציית לימוד חשבון עבור תלמידי כיתות א'. כאן לומדים חושבים בכיף. " +
				"איך עושים את זה?\n"+
				"1. למידה - תוכלו לצפות בסרטונים שיעזרו לכם ללמוד חיבור וחיסור ברמות שונות.\n"  +
				"2. תרגול - כאן תענו על מספר שאלות וגם אם אתם טועים לא נורא, תוכלו לנסות שוב או לעשות משחק שיעזור לכם להבין את התשובה הנכונה\n" +
				"3. מבחן - לא להיבהל. המבחן הוא בשבילכם. תוכלו לבדוק את עצמכם. במה אתם טובים יותר, ובמה צריכים לתרגל עוד.\n"+ "ככל שתבחנו את עצמכם יותר, כך תעלו את הרמה שלכם " );

		setContentView(tv);

	}
}
