package com.education.android.mathcounts;

import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TestDatabaseHelper extends SQLiteOpenHelper {
	private static SQLiteDatabase mDatabase;
	
	private static final String DB_NAME = "tests.sqlite";
	private static final int VERSION = 2;

	//columns representing attributes of Test object
	private static final String TABLE_TEST = "test";
	private static final String COLUMN_TEST_ID = "_id";
	private static final String COLUMN_USER = "user";
	private static final String COLUMN_TEST_DATE = "test_date";
	private static final String COLUMN_OPERATION = "operation";
	private static final String COLUMN_DIFFICULTY = "difficulty";
	private static final String COLUMN_TIME_ALLOTTED = "time_allotted";
	private static final String COLUMN_TIME_REMAINING = "time_remaining";
	private static final String COLUMN_NUMBER_QUESTIONS = "num_questions";
	private static final String COLUMN_NUMBER_CORRECT = "num_correct";
	private static final String COLUMN_QUIT_TEST = "quit_test";
	
	//columns representing attributes of Question object, which is presented to user as a Card
	private static final String TABLE_QUESTION = "question";
	private static final String COLUMN_QUESTION_NUMBER = "question_num";
	private static final String COLUMN_FIRST_NUMBER = "first_num";
	private static final String COLUMN_SECOND_NUMBER = "second_num";
	private static final String COLUMN_ANSWER = "answer";
	private static final String COLUMN_STUDENT_ANSWER = "student_answer";
	private static final String COLUMN_ANSWER_CORRECT = "answer_correct";
	private static final String COLUMN_QUESTION_TEST_ID = "_id";
	
	private static final String TABLE_HIGH_SCORE = "high_score";
	private static final String COLUMN_TEST_MODE = "mode";
	private static final String COLUMN_SCORE = "score";
	private static final String COLUMN_NAME = "name";
	private static final String COLUMN_HIGH_SCORE_ID = "_id";
	
	public TestDatabaseHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
		mDatabase = getWritableDatabase();
	}

	//create table with given columns
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table test (" + 
				" _id integer primary key autoincrement, user varchar(100)," +
				" test_date integer, operation varchar(100), difficulty integer," +
				" time_allotted varchar(100), time_remaining varchar(100)," +
				" num_questions integer, num_correct integer, quit_test integer)");
		db.execSQL("create table question (" +
				" _id integer references test(_id), question_num integer, first_num integer, second_num integer," +
				" answer integer, student_answer integer, answer_correct integer)");
		db.execSQL("create table high_score (" +
				" _id integer primary key autoincrement, mode integer, score integer, name varchar(100))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if(oldVersion == 1) {
			db.execSQL("create table high_score (" +
					" _id integer primary key autoincrement, mode integer, score integer, name varchar(100))");
		}
	}
	
	//delete all tests and their corresponding questions from database
	public void deleteAllTests() {
		mDatabase.delete(TABLE_TEST, null, null);
		mDatabase.delete(TABLE_QUESTION, null, null);
	}
	
	//delete single test with given id
	public void deleteTest(long id) {
		mDatabase.delete(TABLE_TEST, COLUMN_TEST_ID + " = ?", new String[] { String.valueOf(id) });
		mDatabase.delete(TABLE_QUESTION, COLUMN_QUESTION_TEST_ID + " = ?", new String[] { String.valueOf(id) });
	}
	
	//delete tests for given user
	public void deleteTestByName(String name) {
		mDatabase.delete(TABLE_TEST, COLUMN_USER + " = ?", new String[] { name });
	}
	
	//insert questions for test with given id from list of Cards
	public long insertQuestions(long testId,  ArrayList<Card> cardList) {
		ContentValues cv = new ContentValues();
		
		for(Card card : cardList) {
			cv.put(COLUMN_QUESTION_NUMBER, card.getQuestionNum());
			cv.put(COLUMN_FIRST_NUMBER, card.getFirstNum());
			cv.put(COLUMN_SECOND_NUMBER, card.getSecondNum());
			cv.put(COLUMN_ANSWER, card.getAnswer());
			cv.put(COLUMN_STUDENT_ANSWER, card.getStudentAnswer().equals("") ? 
					"" : card.getStudentAnswer());
			cv.put(COLUMN_ANSWER, card.getAnswer());
			
			int correct = (String.valueOf(card.getAnswer()).equals(card.getStudentAnswer())) ? 
					1 : 0;
			cv.put(COLUMN_ANSWER_CORRECT, correct);
			
			cv.put(COLUMN_QUESTION_TEST_ID, testId);
			
			mDatabase.insert(TABLE_QUESTION, null, cv);
		}
		return testId;
	}
	
	public boolean shouldInsertScore(int mode, long score) {
		Cursor cursor = getReadableDatabase().query(TABLE_HIGH_SCORE,
				null,
				COLUMN_TEST_MODE + " = ?",
				new String[] { String.valueOf(mode) },
				null,
				null,
				COLUMN_SCORE + " asc");

		if(cursor.getCount() < 3)
			return true;
		
		cursor.moveToFirst();
		long highScore = cursor.getInt(cursor.getColumnIndex(COLUMN_SCORE));
		
		if((score > highScore)) {
			return true;
		}
		
		return false;
	}
	
	public long insertHighScore(int mode, long score, String name) {
		ContentValues cv = new ContentValues();
		Cursor cursor = getReadableDatabase().query(TABLE_HIGH_SCORE,
				null,
				COLUMN_TEST_MODE + " = ?",
				new String[] { String.valueOf(mode) },
				null,
				null,
				COLUMN_SCORE + " asc");
		
		if(cursor.getCount() >= 3) {
			cursor.moveToFirst();
			
			long id = cursor.getInt(cursor.getColumnIndex(COLUMN_HIGH_SCORE_ID));
			
			getWritableDatabase().delete(TABLE_HIGH_SCORE,
					COLUMN_HIGH_SCORE_ID + " = ?",
					new String[] { String.valueOf(id) });
		}
		
		cv.put(COLUMN_TEST_MODE, mode);
		cv.put(COLUMN_SCORE, score);
		cv.put(COLUMN_NAME, name);
		return getWritableDatabase().insert(TABLE_HIGH_SCORE, null, cv);
	}
	
	public Cursor getHighScores(int mode) {
		return getReadableDatabase().query(TABLE_HIGH_SCORE,
				null,
				COLUMN_TEST_MODE + " = ?",
				new String[] { String.valueOf(mode) },
				null,
				null,
				COLUMN_SCORE + " desc");
	}
	
	public void deleteAllHighScores() {
		mDatabase.delete(TABLE_HIGH_SCORE, null, null);
	}
	
	//insert single test and return its auto-generated id in table
	public long insertTest(long date, String user, int numQuestions, 
			int numCorrect, String operation, int difficulty,
			String timeAllotted, String timeRemaining, int quitTest) {
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_TEST_DATE, date);
		cv.put(COLUMN_USER, user);
		cv.put(COLUMN_OPERATION, operation);
		cv.put(COLUMN_DIFFICULTY, difficulty);
		cv.put(COLUMN_TIME_ALLOTTED, timeAllotted);
		cv.put(COLUMN_TIME_REMAINING, timeRemaining);
		cv.put(COLUMN_NUMBER_QUESTIONS, numQuestions);
		cv.put(COLUMN_NUMBER_CORRECT, numCorrect);
		cv.put(COLUMN_QUIT_TEST, quitTest);
		return getWritableDatabase().insert(TABLE_TEST, null, cv);
	}
	
	//return TestCursor containing a single test with the given id
	public TestCursor queryTest(long id) {
		Cursor wrapped = getReadableDatabase().query(TABLE_TEST,
				null,
				COLUMN_TEST_ID + " = ?",
				new String[] { String.valueOf(id) },
				null,
				null,
				null,
				"1");
		return new TestCursor(wrapped);
	}
	
	//return TestCursor containing all tests in database, sorted by date
	public TestCursor queryTestsByDate() {
		Cursor wrapped = getReadableDatabase().query(TABLE_TEST,
				null,
				COLUMN_QUIT_TEST + " = ?",
				new String[] { "0" },
				null,
				null,
				COLUMN_TEST_DATE + " desc");
		return new TestCursor(wrapped);
	}
	
	//return TestCursor containing all tests for given user
	public TestCursor queryTestsByName(String name) {
		Cursor wrapped = getReadableDatabase().query(TABLE_TEST,
				null,
				COLUMN_USER + " = ? AND " + COLUMN_QUIT_TEST + " = ?",
				new String[] { name, "0" },
				null,
				null,
				COLUMN_TEST_DATE + " desc");
		return new TestCursor(wrapped);
	}
	
	//return CardCursor containing questions from test with given id
	public CardCursor queryQuestionsForTest(long testId) {
		Cursor wrapped = getReadableDatabase().query(TABLE_QUESTION,
				null,
				COLUMN_QUESTION_TEST_ID + " = ?",
				new String[] { String.valueOf(testId) },
				null,
				null,
				COLUMN_QUESTION_NUMBER + " asc");
		return new CardCursor(wrapped);
	}
	
	//contains Tests matching query parameters
	public static class TestCursor extends CursorWrapper {
		public TestCursor(Cursor cursor) {
			super(cursor);
		}
		
		//creates new Test object with attributes from table
		public Test getTest() {
			if(isBeforeFirst() || isAfterLast()) return null;
			
			Test test = new Test();
			long testId = getLong(getColumnIndex(COLUMN_TEST_ID));
			test.setTestId(testId);
			long testDate = getLong(getColumnIndex(COLUMN_TEST_DATE));
			test.setTestDate(new Date(testDate));
			String operation = getString(getColumnIndex(COLUMN_OPERATION));
			test.setOperation(operation);
			int difficulty = getInt(getColumnIndex(COLUMN_DIFFICULTY));
			test.setDifficulty(difficulty);
			String timeAllotted = getString(getColumnIndex(COLUMN_TIME_ALLOTTED));
			test.setTimeAllotted(timeAllotted);
			String timeRemaining = getString(getColumnIndex(COLUMN_TIME_REMAINING));
			test.setTimeRemaining(timeRemaining);
			String user = getString(getColumnIndex(COLUMN_USER));
			test.setUser(user);
			int numQuestions = getInt(getColumnIndex(COLUMN_NUMBER_QUESTIONS));
			test.setNumQuestions(numQuestions);
			int numCorrect = getInt(getColumnIndex(COLUMN_NUMBER_CORRECT));
			test.setNumCorrect(numCorrect);
			int quitTest = getInt(getColumnIndex(COLUMN_QUIT_TEST));
			test.setQuitTest(quitTest == 1 ? true : false);
			return test;
		}
	}
	
	//contains Cards (questions) with query parameters
	public static class CardCursor extends CursorWrapper {
		public CardCursor(Cursor c) {
			 	super(c);
		}
		
		//returns new Card object with attributes from table
		public Card getCard() {
			if(isBeforeFirst() || isAfterLast()) return null;
			
			Card card = new Card();
			int firstNum = getInt(getColumnIndex(COLUMN_FIRST_NUMBER));
			card.setFirstNum(firstNum);
			int secondNum = getInt(getColumnIndex(COLUMN_SECOND_NUMBER));
			card.setSecondNum(secondNum);
			int answer = getInt(getColumnIndex(COLUMN_ANSWER));
			card.setAnswer(answer);
			int questionNum = getInt(getColumnIndex(COLUMN_QUESTION_NUMBER));
			card.setQuestionNum(questionNum);
			String studentAnswer = getString(getColumnIndex(COLUMN_STUDENT_ANSWER));
			card.setStudentAnswer(studentAnswer);
			return card;
		}
	}
}
