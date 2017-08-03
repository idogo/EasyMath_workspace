package com.easymath.util;

import com.firebase.client.Firebase;

public class FirebaseDBUtils {

	private final static String CONNECTION_STRING = "https://brilliant-fire-7490.firebaseio.com/";
	public final static String TEACHERS_TBL = "users/teachers";
	public final static String STUDENTS_TBL = "users/students";

	public static String getDatabaseURL(){
		return CONNECTION_STRING;
	}

	public static Firebase getTable(Firebase databaseReference, String table){
		return databaseReference.child(table);
	}
}
