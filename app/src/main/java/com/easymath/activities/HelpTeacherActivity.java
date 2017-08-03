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
		tv.setText("����� �� ���� ��������:\n" +
				"1.���� ���� ������� ��� ����� ����� ������ ����� (����� ��� ���� ��� ������)\n" +
				"2.����� ������� - ���� ���� ������ ����� ��� ���� ����� ���� ����� �� ���� ����� ���� ������� ���\n" +
				"3. ����� ������� - ������ ������ ������� �� ����� ������ ������ ������ �������� �������� ����� \n"+
				"������ �� �����, ������ ������ ������ ���� ����� ����� ���� ���� ���� �� ��� 1 � 5 ���� 1 �� ���� ��� �5 ��� ���, �� ������� ���� 0 �� ���� ������� ��� �� ����.");

		setContentView(tv);

	}
}
