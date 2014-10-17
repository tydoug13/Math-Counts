package com.education.android.mathcounts;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.education.android.mathcounts.TestDatabaseHelper.TestCursor;

public class SavedTestsFragment extends ListFragment implements LoaderCallbacks<Cursor> {
	public static final int DELETE_ALL = 0;
	public static final int DELETE_TAB = 1;
	
	public static final String USER_NAME = "name";
	
	private static String mName;
	private TestCursor mTestCursor;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		
		Bundle args = getArguments();
		if(args != null) {
			mName = args.getString(USER_NAME);
		} else {
			mName = null;
		}
		
		getLoaderManager().initLoader(0, null, this);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		getListView().setMultiChoiceModeListener(new MultiChoiceModeListener() {
			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				switch(item.getItemId()) {
					case R.id.menu_item_delete_test:
						TestCursorAdapter adapter = (TestCursorAdapter) getListAdapter();
						
						for(int i = adapter.getCount() - 1; i >= 0; i--) {
							if(getListView().isItemChecked(i)) {
								TestManager.deleteTestById(((TestCursor) adapter.getItem(i)).getTest().getTestId(), getActivity());
							}
						}
						
						mTestCursor.requery();
						adapter.notifyDataSetChanged();
						((SavedTestsActivity) getActivity()).updateTabs();
						return true;
					default:
						return false;
				}
			}

			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				MenuInflater inflater = mode.getMenuInflater();
				inflater.inflate(R.menu.test_list_item_context, menu);
				return true;
			}

			@Override
			public void onDestroyActionMode(ActionMode mode) {}

			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				return false;
			}

			@Override
			public void onItemCheckedStateChanged(ActionMode mode,
					int position, long id, boolean checked) {
			}
		});
	
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Intent i = new Intent(getActivity(), TestResultActivity.class);
		i.putExtra(TestResultFragment.TEST_ID, id);
		i.putExtra(TestResultFragment.IS_FROM_TEST, false);
		startActivity(i);
		getActivity().finish();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		if(mName != null)
			inflater.inflate(R.menu.fragment_saved_tests, menu);
		else
			inflater.inflate(R.menu.fragment_all_saved_tests, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		FragmentManager fm = getActivity().getSupportFragmentManager();
		DeleteDialogFragment dialog = DeleteDialogFragment.newInstance();
		switch(item.getItemId()) {
			case R.id.menu_item_delete_tab:
				dialog.setTargetFragment(SavedTestsFragment.this, DELETE_TAB);
				dialog.show(fm, null);
				return true;
			case R.id.menu_item_delete_all:
				dialog.setTargetFragment(SavedTestsFragment.this, DELETE_ALL);
				dialog.show(fm, null);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	public static Fragment newInstance(String name) {
		Bundle args = new Bundle();
		args.putString(USER_NAME, name);
		
		SavedTestsFragment frag = new SavedTestsFragment();
		frag.setArguments(args);
		return frag;
	}
	
	public static Fragment newInstance() {
		return new SavedTestsFragment();
	}
	
	private static class TestListCursorLoader extends SQLiteCursorLoader {
		private Context mContext;
		
		public TestListCursorLoader(Context context) {
			super(context);
			mContext = context;
		}

		@Override
		protected Cursor loadCursor() {
			if(mName != null) {
				return TestManager.queryTestsByName(mName, mContext);
			} else {
				return TestManager.queryTestsByDate(mContext);
			}
		}
	}
	
	private static class TestCursorAdapter extends CursorAdapter {
		private TestCursor mTestCursor;
		
		public TestCursorAdapter(Context context, TestCursor c) {
			super(context, c, 0);
			mTestCursor = c;
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			return inflater
					.inflate(R.layout.previous_tests_list_item, parent, false);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			Test test = mTestCursor.getTest();
			
			Date date = test.getTestDate();
			String formattedDate = new SimpleDateFormat("M/d/yyyy" + "    " + "h:mm a").format(date);
			
			TextView leftBracket = (TextView) view.findViewById(R.id.tvLeftBracket);
			TextView rightBracket =  (TextView) view.findViewById(R.id.tvRightBracket);
			TextView operationText = (TextView) view.findViewById(R.id.tvOperation);
			TextView scoreText = (TextView) view.findViewById(R.id.tvScore);
			TextView dateText = (TextView) view.findViewById(R.id.tvDate);
			TextView nameText = (TextView) view.findViewById(R.id.tvName);
			
			formatTextViews(leftBracket, rightBracket, operationText, test.getOperation());

			scoreText.setText(test.getNumCorrect() + "/" + test.getNumQuestions());
			dateText.setText(formattedDate);
			nameText.setText(test.getUser());
		}
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		return new TestListCursorLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		mTestCursor = (TestCursor) cursor;
		TestCursorAdapter adapter = 
				new TestCursorAdapter(getActivity(), (TestCursor) cursor);
		setListAdapter(adapter);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {}
	
	private static void formatTextViews(TextView leftBracket, TextView rightBracket,
			TextView operationText, String operation) {
		if(operation.equals(TestManager.ADDITION)) {
			int textColor = Color.rgb(181, 81, 81);
			operationText.setText("+");
			leftBracket.setTextColor(textColor);
			rightBracket.setTextColor(textColor);
			operationText.setTextColor(textColor);
		} else if(operation.equals(TestManager.SUBTRACTION)) {
			int textColor = Color.rgb(0, 155, 221);
			operationText.setText("-");
			leftBracket.setTextColor(textColor);
			rightBracket.setTextColor(textColor);
			operationText.setTextColor(textColor);
		} else if(operation.equals(TestManager.MULTIPLICATION)) {
			int textColor = Color.rgb(34, 177, 76);
			operationText.setText("x");
			leftBracket.setTextColor(textColor);
			rightBracket.setTextColor(textColor);
			operationText.setTextColor(textColor);
		} else if(operation.equals(TestManager.DIVISION)) {
			int textColor = Color.rgb(163, 73, 164);
			operationText.setText("÷");
			leftBracket.setTextColor(textColor);
			rightBracket.setTextColor(textColor);
			operationText.setTextColor(textColor);
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode != Activity.RESULT_OK) return;
		
		if(requestCode == DELETE_TAB) {
			Toast.makeText(getActivity(), getResources().getString(R.string.tabDeleted), Toast.LENGTH_LONG).show();
			TestManager.deleteTestByName(mName, getActivity());
			((TabManager) getActivity()).updateTabs();
		} else if(requestCode == DELETE_ALL) {
			Toast.makeText(getActivity(), getResources().getString(R.string.allTestsDeleted), Toast.LENGTH_LONG).show();
			TestManager.deleteAllTests(getActivity());
			((TabManager) getActivity()).updateTabs();
		}
	}

	public interface TabManager {
		void updateTabs();
	}
}
