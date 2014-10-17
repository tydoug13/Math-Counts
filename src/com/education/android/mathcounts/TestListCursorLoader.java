package com.education.android.mathcounts;

import android.content.Context;
import android.database.Cursor;

public class TestListCursorLoader extends SQLiteCursorLoader {

	public TestListCursorLoader(Context context) {
		super(context);
	}

	@Override
	protected Cursor loadCursor() {
		Cursor cursor = TestManager.queryTestsByDate(getContext());
		return cursor;
	}
}
