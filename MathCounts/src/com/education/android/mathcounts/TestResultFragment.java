package com.education.android.mathcounts;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.education.android.mathcounts.TestDatabaseHelper.CardCursor;

public class TestResultFragment extends Fragment implements LoaderCallbacks<Cursor> {
	public static final String TEST_ID = "test_id";
	public static final String IS_FROM_TEST = "from_test";
	
	private static String mOperation;
	private static Test mTest;
	private ListView mCardList;
	private boolean isFromTest;
	
	public Test realTest;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		Bundle args = getArguments();
		if(args.getBoolean(IS_FROM_TEST)) {
			isFromTest = true;
		}

		if(args != null && args.getLong(TEST_ID) != -1)
			mTest = TestManager.getTest(args.getLong(TEST_ID), getActivity());
		else
			getActivity().finish();
		
		ActionBar mActionBar = getActivity().getActionBar();
		mActionBar.setDisplayShowTitleEnabled(false);
		mActionBar.setDisplayShowHomeEnabled(false);
		
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		
		View menu;
		TextView nameTextView;
		menu = inflater.inflate(R.layout.result_actionbar, null);
		nameTextView = (TextView) menu.findViewById(R.id.menu_name);
		nameTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isFromTest)
					((TestResultActivity) getActivity()).goBack();
				else
					getActivity().onBackPressed();
			}
		});
		
		if(isFromTest)
			nameTextView.setText(getResources().getString(R.string.home));
		else
			nameTextView.setText(getResources().getString(R.string.back_button_label));
		
		
		nameTextView.setTextSize(20);
		
		TextView dateTextView = (TextView) menu.findViewById(R.id.menu_date);
		dateTextView.setText(mTest.getTestDateString());
		dateTextView.setTextSize(20);
		
		mActionBar.setCustomView(menu);
		mActionBar.setDisplayShowCustomEnabled(true);
		
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v;
		
		if(isFromTest) {
			v = inflater.inflate(R.layout.fragment_test_result, container, false);
		} else
			v = inflater.inflate(R.layout.fragment_test_result_no_retake, container, false);
		
		mCardList = (ListView) v.findViewById(R.id.result_fragment_resultList);
		
		TextView scoreTextView = (TextView) v.findViewById(R.id.tvScoreResult);
		scoreTextView.setText(mTest.getNumCorrect() + "/" + mTest.getNumQuestions());
		
		TextView timeTextView = (TextView) v.findViewById(R.id.tvTimeResult);
		if(!mTest.getTimeAllotted().equals("-")) {
			long timeUsed = (Timer.getMilliSeconds(mTest.getTimeAllotted()) - Timer.getMilliSeconds(mTest.getTimeRemaining()));
			timeTextView.setText(Timer.millisecondsToString(timeUsed) + "/" + mTest.getTimeAllotted());
		} else {
			timeTextView.setText("-/-");
		}
		
		TextView levelTextView = (TextView) v.findViewById(R.id.tvLevelResult);
		
		if(mTest.getOperation().equals(TestManager.ADDITION) || mTest.getOperation().equals(TestManager.SUBTRACTION)) {
			switch(mTest.getDifficulty()) {
			case 1:
				levelTextView.setText(getResources().getString(R.string.easy));
				break;
			case 2: 
				levelTextView.setText(getResources().getString(R.string.medium));
				break;
			case 3:
				levelTextView.setText(getResources().getString(R.string.hard));
				break;
			case 4:
				levelTextView.setText(getResources().getString(R.string.expert));
				break;
			default:
				levelTextView.setText("");
			}
		} else if(mTest.getOperation().equals(TestManager.MULTIPLICATION) || mTest.getOperation().equals(TestManager.DIVISION)) {
			if(mTest.getDifficulty() == 13) 
				levelTextView.setText(getResources().getString(R.string.all));
			else 
				levelTextView.setText(Integer.toString(mTest.getDifficulty()));
		}
		
															
		
		Button retakeButton = (Button) v.findViewById(R.id.result_fragment_retakeButton);
		if(retakeButton != null) {
			retakeButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent(getActivity(), TestActivity.class);
					i.putExtra(TestManager.OPERATION, mTest.getOperation());
					i.putExtra(TestManager.QUESTIONS, mTest.getNumQuestions());
					i.putExtra(TestManager.DIFFICULTY, mTest.getDifficulty());
					i.putExtra(TestManager.TIME, mTest.getTimeAllotted());
					i.putExtra(TestManager.NAME, mTest.getUser());
					startActivity(i);
					getActivity().finish();
				}
			});	
		}
		
		if(mTest.getOperation().equals(TestManager.ADDITION)) {
			mOperation = "+";
			getActivity().getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_red));
			
			if(retakeButton != null)
				retakeButton.setBackgroundResource(R.drawable.button_shape_start_test_red);
		} else if(mTest.getOperation().equals(TestManager.SUBTRACTION)) {
			mOperation = "-";
			getActivity().getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_blue));
			
			if(retakeButton != null)
				retakeButton.setBackgroundResource(R.drawable.button_shape_start_test_blue);
		} else if(mTest.getOperation().equals(TestManager.MULTIPLICATION)) {
			mOperation = "x";
			getActivity().getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_green));
			
			if(retakeButton != null)
				retakeButton.setBackgroundResource(R.drawable.button_shape_start_test_green);
		} else if(mTest.getOperation().equals(TestManager.DIVISION)) {
			mOperation = "/";
			getActivity().getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_purple));
			
			if(retakeButton != null)
				retakeButton.setBackgroundResource(R.drawable.button_shape_start_test_purple);
		}
		
		return v;
	}
	
	public static Fragment newInstance(long id, boolean isFromTest) {
		Bundle args = new Bundle();
		args.putLong(TEST_ID, id);
		args.putBoolean(IS_FROM_TEST, isFromTest);
		
		Fragment frag = new TestResultFragment();
		frag.setArguments(args);
		
		return frag;
	}
	
	private static class CardListCursorLoader extends SQLiteCursorLoader {
		Context mContext;
		
		public CardListCursorLoader(Context context) {
			super(context);
			mContext = context;
		}

		@Override
		protected Cursor loadCursor() {
			return TestManager.queryCardsForTest(mTest.getTestId(), mContext);
		}
	}
	
	private static class CardCursorAdapter extends CursorAdapter {
		private CardCursor mCardCursor;
		
		public CardCursorAdapter(Context context, CardCursor c) {
			super(context, c, 0);
			mCardCursor = c;
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			return inflater
					.inflate(R.layout.list_item_question, parent, false);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			Card card = mCardCursor.getCard();
			
			TextView questionNum = 
					(TextView) view.findViewById(R.id.result_fragment_questionNum);
			TextView problemText =
					(TextView) view.findViewById(R.id.result_fragment_problemText);
			ImageView correctImage =
					(ImageView) view.findViewById(R.id.result_fragment_correctImage);
			TextView correctAnswer =
					(TextView) view.findViewById(R.id.result_fragment_correctAnswer);
			
			questionNum.setText("#" + card.getQuestionNum());
			if((mOperation.equals("+") || mOperation.equals("-")) && mTest.getDifficulty() == 4) {
				Display display = ((Activity) mContext).getWindowManager().getDefaultDisplay();
				Point size = new Point();
				display.getSize(size);
				if(size.x >= 1500) {
					problemText.setTextSize(35);
					correctAnswer.setTextSize(35);
				} else {
					problemText.setTextSize(20);
					correctAnswer.setTextSize(20);
				}
			}
			problemText.setText(card.getFirstNum() + " " + mOperation + " " +
					card.getSecondNum() + " = " + card.getStudentAnswer());
			
			if(String.valueOf(card.getAnswer()).equals(card.getStudentAnswer())) {
				correctImage.setImageResource(R.drawable.btn_check_buttonless_on);
				correctAnswer.setText("");
			} else {
				correctImage.setImageResource(android.R.drawable.ic_delete);
				correctAnswer.setText("(" + card.getAnswer() + ")");
			}
		}

		@Override
		public boolean isEnabled(int position) {
			return false;
		}

	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		return new CardListCursorLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		CardCursorAdapter adapter = 
				new CardCursorAdapter(getActivity(), (CardCursor) cursor);
		mCardList.setAdapter(adapter);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mTest = null;
	}
	
	
}
