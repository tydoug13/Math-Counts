package com.education.android.mathcounts;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

//load Cursors on separate thread
public abstract class SQLiteCursorLoader extends AsyncTaskLoader<Cursor> {
	private Cursor mCursor;

	public SQLiteCursorLoader(Context context) {
		super(context);
	}
	
	protected abstract Cursor loadCursor();

	@Override
	public Cursor loadInBackground() {
		Cursor cursor = loadCursor();
		if(cursor != null) {
			cursor.getCount();
		}
		return cursor;
	}

	@Override
	public void deliverResult(Cursor data) {
		Cursor oldCursor = mCursor;
		mCursor = data;
		
		if(isStarted()) {
			super.deliverResult(data);
		}
		
		if(oldCursor != null && oldCursor != data && !oldCursor.isClosed()) {
			oldCursor.close();
		}
	}

	@Override
	public void onCanceled(Cursor data) {
		if(data != null && !data.isClosed()) {
			data.close();
		}
	}

	@Override
	protected void onReset() {
		super.onReset();
		
		onStopLoading();
		
		if(mCursor != null && !mCursor.isClosed()) {
			mCursor.close();
		}
		mCursor = null;
	}

	@Override
	protected void onStartLoading() {
		if(mCursor != null ) {
			deliverResult(mCursor);
		}
		if(takeContentChanged() || mCursor == null) {
			forceLoad();
		}
	}

	@Override
	protected void onStopLoading() {
		cancelLoad();
	}
}
