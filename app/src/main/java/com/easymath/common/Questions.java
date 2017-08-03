package com.easymath.common;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;

/**
 * Represents a set of questions and one displayed question
 * @author liadpc
 */
public class Questions {

	/**
	 * Currently displayed question
	 */
	public static String question = "a";
	
	/**
	 * Displayed Answer 1
	 */
	public static String ans1;
	
	/**
	 * Displayed Answer 2
	 */
	public static String ans2;
	
	/**
	 * Displayed Answer 3
	 */
	public static String ans3;
	
	/**
	 * Displayed Answer 4
	 */
	public static String ans4;
	
	/**
	 * True answer
	 */
	public static int ans;
	
	/**
	 * Points for displayed question
	 */
	public static int points;
	
	/**
	 * Holds 5 questions for training
	 */
	public static String [] questionNumbers  = new String [5];
	
	/**
	 * Holds the true answers for test questions
	 */
	public static int [] answersForTests = new int [10];
	
	/**
	 * Hold answer times for test questions
	 */
	public static long [] answersTimesInTest = new long [10];
	
	/**
	 * Holds TRUE for correct answers and FALSE for wrong answers
	 */
	public static boolean [] correctAnswersInTest = new boolean [10];
	
	/**
	 * Holds each question's coefficient for the level calculation
	 */
	public static double [] coefficient = new double [10];
	
	/**
	 * Questions reference holder for test
	 */
	public static DataSnapshot [] QuestionsRefFortest = new DataSnapshot [10];
	
	/**
	 * DB access
	 */
	public static Firebase ref;
	
	/**
	 * Subject of displayed question
	 */
	public static String subject;
	
	/**
	 * Number of displayed question
	 */
	public static int numOfQuestion;
	
	/**
	 * Holds add level of the tested user
	 */
	public static int addLevel;
	
	/**
	 * Holds sub level of the tested user
	 */
	public static int subLevel;
	
	/**
	 * Holds the amount of dragged items to display in visualized games (explanations)
	 */
	public static int questionExplanationVar;
	
	/**
	 * Holds number of tries
	 */
	public static int numOfTry;
	
}
