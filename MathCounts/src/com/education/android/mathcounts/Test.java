package com.education.android.mathcounts;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author Tynan
 *
 * Test objects are created when data about a previously saved test is queried from the database.
 *
 */

public class Test {
	private long mTestId;
	private String mUser;
	private Date mTestDate;
	private String mOperation;
	private int mDifficulty;
	private String mTimeAllotted;
	private String mTimeRemaining;
	private int mNumQuestions;
	private int mNumCorrect;
	private boolean quitTest;
	
	public long getTestId() {
		return mTestId;
	}
	public void setTestId(long testId) {
		mTestId = testId;
	}
	public String getUser() {
		return mUser;
	}
	public void setUser(String user) {
		mUser = user;
	}
	public Date getTestDate() {
		return mTestDate;
	}
	public void setTestDate(Date testDate) {
		mTestDate = testDate;
	}
	public int getNumQuestions() {
		return mNumQuestions;
	}
	public void setNumQuestions(int numQuestions) {
		mNumQuestions = numQuestions;
	}
	public int getNumCorrect() {
		return mNumCorrect;
	}
	public void setNumCorrect(int numCorrect) {
		mNumCorrect = numCorrect;
	}
	public String getOperation() {
		return mOperation;
	}
	public void setOperation(String operation) {
		mOperation = operation;
	}
	public int getDifficulty() {
		return mDifficulty;
	}
	public void setDifficulty(int difficulty) {
		mDifficulty = difficulty;
	}
	public String getTimeAllotted() {
		return mTimeAllotted;
	}
	public void setTimeAllotted(String timeAllotted) {
		mTimeAllotted = timeAllotted;
	}
	public String getTimeRemaining() {
		return mTimeRemaining;
	}
	public void setTimeRemaining(String timeRemaining) {
		mTimeRemaining = timeRemaining;
	}
	public String getTestDateString() {
		return new SimpleDateFormat("MM/dd/yyyy").format(mTestDate);
	}
	public boolean didQuitTest() {
		return quitTest;
	}
	public void setQuitTest(boolean quitTest) {
		this.quitTest = quitTest;
	}
}
