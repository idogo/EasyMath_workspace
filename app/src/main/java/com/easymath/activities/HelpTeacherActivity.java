package com.easymath.activities;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.widget.TextView;

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
		tv.setText("למורה יש מספר אפשרויות:\n" +
				"1.לחצן הוסף תרגילים אשר יוסיף תרגיל שהמורה יכתוב (בתנאי שלא קיים כבר במערכת)\n" +
				"2.הוספת תלמידים - מורה יכול להוסיף תלמיד אשר יהיה מקושר אליו ובדרך זו יוכל לעקוב אחרי הפרופיל שלו\n" +
				"3. ניהול תלמידים - אפשרות לצפייה בפרופיל של תלמיד ומחיקת התלמיד מרשימת התלמידים המקושרים למורה \n"+
				"לתשומת לב המורה, פרופיל התלמיד מתעדכן כאשר תלמיד מסיים מבחן והוא מציג מד בין 1 ל 5 כאשר 1 זה פחות טוב ו5 הכי טוב, אם הפרופיל מראה 0 זה אומר שהתלמיד עוד לא נבחן.");

		setContentView(tv);

	}
}
