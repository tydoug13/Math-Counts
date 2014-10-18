package com.education.android.mathcounts;

import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;

import com.education.android.mathcounts.TestDatabaseHelper.CardCursor;
import com.education.android.mathcounts.TestDatabaseHelper.TestCursor;

public class TestManager {
	
	private static TestManager mTestManager;
	private ArrayList<Card> mCardList = new ArrayList<Card>();
	
	private Context mContext;
	
	private int mNumQuestions;
	private int mDifficulty;
	private boolean mTestFinished;
	private String mOperation;
	private String mTime;
	private String mTimeLeft;
	private String mStudentName;

	public static final String ADDITION = "Addition";
	public static final String SUBTRACTION = "Subtraction";
	public static final String MULTIPLICATION = "Multiplication";
	public static final String DIVISION = "Division";
	public static final String CHALLENGE = "Challenge";
	
	public static final String OPERATION = "operation";
	public static final String QUESTIONS = "questions";
	public static final String DIFFICULTY = "difficulty";
	public static final String TIME = "time";
	public static final String NAME = "name";
	
	private TestManager(String operation, int numQuestions, int difficulty, Context c) {
		mContext = c;
		
		mOperation = operation;
		mNumQuestions = numQuestions;
		mDifficulty = difficulty;
		mTestFinished = false;
	}
	
	public static TestManager newInstance(String operation, int numQuestions,
			int difficulty, Context context) {
		if(mTestManager == null) {
			mTestManager = new TestManager(operation, numQuestions, difficulty, context);
		}
		return mTestManager;
	}
	
	public static TestManager getInstance() {
		return mTestManager;
	}
	
	public void release() {
		mTestManager = null;
	}
	
	public void generateQuestions() {
		int[][] questions = null;
			
		if(mOperation.equals(TestManager.ADDITION)) {
			questions = NumberGenerator.getAdditionRandy(mDifficulty, mNumQuestions);
		}
		if(mOperation.equals(TestManager.SUBTRACTION)) {
			questions = NumberGenerator.getSubtractionRandy(mDifficulty, mNumQuestions);
		}
		if(mOperation.equals(TestManager.MULTIPLICATION)) {
			questions = NumberGenerator.getMultiplicationRandy(mDifficulty, mNumQuestions);
		}
		if(mOperation.equals(TestManager.DIVISION)) {
			questions = NumberGenerator.getDivisionRandy(mDifficulty, mNumQuestions);
		}
		if(mOperation.equals(TestManager.CHALLENGE)) {
		}
		
		if(questions == null) return;
		
		for(int i = 0; i < mNumQuestions; i++) {
			Card card = new Card();
			card.setFirstNum(questions[i][0]);
			card.setSecondNum(questions[i][1]);
			card.setAnswer(questions[i][2]);
			card.setQuestionNum(i + 1);
			mCardList.add(card);
		}
	}
	
	public static int[] getSampleQuestion(String operation, int difficulty) {
		int[][] questions = null;
			
		if(operation.equals(TestManager.ADDITION)) {
			questions = NumberGenerator.getAdditionRandy(difficulty, 1);
		}
		if(operation.equals(TestManager.SUBTRACTION)) {
			questions = NumberGenerator.getSubtractionRandy(difficulty, 1);
		}
		if(operation.equals(TestManager.MULTIPLICATION)) {
			do {
				questions = NumberGenerator.getMultiplicationRandy(difficulty, 1);
			} while(difficulty != 13 && questions[0][0] != difficulty);
		}
		if(operation.equals(TestManager.DIVISION)) {
			questions = NumberGenerator.getDivisionRandy(difficulty, 1);
		}
		return new int[] {questions[0][0], questions[0][1]};
	}
	
	public ArrayList<Card> getCards() {
		return mCardList;
	}
	
	public Card getCard(int cardIndex) {
		for(Card c : mCardList) {
			if(c.getQuestionNum() == cardIndex)
				return c;
		}
		return null;
	}
	
	public long saveResults(boolean quitTest) {
		int numCorrect = 0;
		for(Card c : mCardList) {
			if(String.valueOf(c.getAnswer()).equals(c.getStudentAnswer())) 
				numCorrect++;
		}
		
		TestDatabaseHelper db = new TestDatabaseHelper(mContext);
		
		long id = db.insertTest(new Date().getTime(), mStudentName, 
				mNumQuestions, numCorrect, mOperation, mDifficulty, mTime, mTimeLeft, quitTest ? 1 : 0);	//If the user quits, returns 1
		
		db.insertQuestions(id, mCardList);
		
		return id;
	}

	public static Test getTest(long id, Context c) {
		TestDatabaseHelper db = new TestDatabaseHelper(c);
		
		Test test = null;
		TestCursor cursor = db.queryTest(id);
		cursor.moveToFirst();
		if(!cursor.isAfterLast()) {
			test = cursor.getTest();
		}
		cursor.close();
		return test;
	}
	
	public static void deleteAllTests(Context c) {
		TestDatabaseHelper db = new TestDatabaseHelper(c);
		db.deleteAllTests();
	}
	
	public static void deleteTestById(long id, Context c) {
		TestDatabaseHelper db = new TestDatabaseHelper(c);
		db.deleteTest(id);
	}
	
	public static void deleteTestByName(String name, Context c) {
		TestDatabaseHelper db = new TestDatabaseHelper(c);
		db.deleteTestByName(name);
	}
	
	public static TestCursor queryTestsByName(String name, Context c) {
		TestDatabaseHelper db = new TestDatabaseHelper(c);
		TestCursor cursor = db.queryTestsByName(name);
		return cursor;
	}
	
	public static TestCursor queryTestsByDate(Context c) {
		TestDatabaseHelper db = new TestDatabaseHelper(c);
		TestCursor cursor = db.queryTestsByDate();
		return cursor;
	}
	
	public static CardCursor queryCardsForTest(long id, Context c) {
		TestDatabaseHelper db = new TestDatabaseHelper(c);
		CardCursor cursor = db.queryQuestionsForTest(id);
		return cursor;
	}
	
	public static void insertHighScore(int mode, long score, String name, Context c) {
		TestDatabaseHelper db = new TestDatabaseHelper(c);
		db.insertHighScore(mode, score, name);
	}
	
	public static void deleteHighScores(Context c) {
		TestDatabaseHelper db = new TestDatabaseHelper(c);
		db.deleteAllHighScores();
	}
	
	public static Cursor getHighScores(int mode, Context c) {
		TestDatabaseHelper db = new TestDatabaseHelper(c);
		return db.getHighScores(mode);
	}
	
	public static boolean shouldInsertScore(int mode, int score, Context c) {
		TestDatabaseHelper db = new TestDatabaseHelper(c);
		return db.shouldInsertScore(mode, score);
	}
	
	public int getDifficulty() {
		return mDifficulty;
	}
	
	public String getOperation() {
		return mOperation;
	}

	public String getTimeLeft() {
		return mTimeLeft;
	}

	public void setTimeLeft(String timeLeft) {
		mTimeLeft = timeLeft;
	}

	public String getTime() {
		return mTime;
	}

	public void setTime(String time) {
		mTime = time;
	}

	public String getStudentName() {
		return mStudentName;
	}

	public void setStudentName(String studentName) {
		mStudentName = studentName;
	}
	
	public void setTestFinished(boolean isFinished) {
		mTestFinished = isFinished;
	}
	
	public boolean isTestFinished() {
		return mTestFinished;
	}
}
