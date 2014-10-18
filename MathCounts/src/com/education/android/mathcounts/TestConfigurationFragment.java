package com.education.android.mathcounts;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

public class TestConfigurationFragment extends Fragment {
	
	public static final String OPERATION =  "operation";
	
	private String mOperation;
	private String mStudentName;
	private String mTime;
	private int mNumQuestions;
	private int mLevel;
	
	private Button mStartTestButton;
	private NumberPicker mLevelPicker;
	private EditText mEditText;
	private TextView mLevelTextView;
	
	
	public static Fragment newInstance(String operation) {
		Bundle args = new Bundle();
		args.putString(OPERATION, operation);
		Fragment fragment = new TestConfigurationFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setRetainInstance(true);
		
		View v = inflater.inflate(R.layout.fragment_test_configuration, container, false);
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if(NavUtils.getParentActivityName(getActivity()) != null) {
				getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
				setHasOptionsMenu(true);
			}
		}
		
		final EditText name = (EditText) v.findViewById(R.id.etname);
		mEditText = name;
		((HomeActivity) getActivity()).setEditText(mEditText);
		name.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
		
		mLevelTextView = (TextView) v.findViewById(R.id.tvlevel);
		
		final NumberPicker timePicker = (NumberPicker) v.findViewById(R.id.timePickerTime);
		timePicker.setMinValue(0);
		timePicker.setMaxValue(7);
		timePicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		
		mLevelPicker = (NumberPicker) v.findViewById(R.id.timePickerLevel);
		mLevelPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		
		final NumberPicker numQuestionsPicker = (NumberPicker) v.findViewById(R.id.timePickerNumQuestions);
		numQuestionsPicker.setMinValue(0);
		numQuestionsPicker.setMaxValue(6);
		numQuestionsPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		
		final Fragment frag = this;
		
		mStartTestButton = (Button) v.findViewById(R.id.startTestButton);
		mStartTestButton.setText(getResources().getString(R.string.start_test));
		mStartTestButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				mNumQuestions = Integer.parseInt(getResources().getStringArray(R.array.question_numbers)[numQuestionsPicker.getValue()]);
				String difficulty = "";
				if(mOperation.equals(TestManager.ADDITION)) {
					difficulty = getResources().getStringArray(R.array.addition_difficulty)[mLevelPicker.getValue()];
				}
				if(mOperation.equals(TestManager.SUBTRACTION)) {
					difficulty = getResources().getStringArray(R.array.subtraction_difficulty)[mLevelPicker.getValue()];
				}
				if(mOperation.equals(TestManager.MULTIPLICATION)) {
					difficulty = getResources().getStringArray(R.array.multiplication_difficulty)[mLevelPicker.getValue()];
				}
				if(mOperation.equals(TestManager.DIVISION)) {
					difficulty = getResources().getStringArray(R.array.division_difficulty)[mLevelPicker.getValue()];
				}
				
				if(difficulty.equals(getResources().getString(R.string.easy))) {
					mLevel = 1;
				} else if(difficulty.equals(getResources().getString(R.string.medium))) {
					mLevel = 2;
				} else if(difficulty.equals(getResources().getString(R.string.hard))) {
					mLevel = 3;
				} else if(difficulty.equals(getResources().getString(R.string.expert))) {
					mLevel = 4;
				} else if(difficulty.equals(getResources().getString(R.string.all))) {
					mLevel = 13;
				} else {
					mLevel = Integer.parseInt(difficulty);
				}
				
				mTime = getResources().getStringArray(R.array.timer_values)[timePicker.getValue()];
				if(mTime.equals(getResources().getString(R.string.noTime))) {
					mTime = "-";
				} 
				
				mStudentName = name.getText().toString();
				
				boolean isFirstTime = PreferenceManager
						.getDefaultSharedPreferences(getActivity())
						.getBoolean(SettingsActivity.FIRST_TIME, true);
				
				DropboxUploader uploader = DropboxUploader.getInstance();
				if(!uploader.hasLinkedAccount() && isFirstTime) {
					FragmentManager fm = getActivity().getSupportFragmentManager();
					StorageSetupDialog dialog = StorageSetupDialog.newInstance();
					dialog.setTargetFragment(frag, Activity.RESULT_OK);
					dialog.show(fm, null);
				} else {
					beginTest();
				}
			}
		});
		
		populate();
		
		timePicker.setDisplayedValues(getResources().getStringArray(R.array.timer_values));
		numQuestionsPicker.setDisplayedValues(getResources().getStringArray(R.array.question_numbers));
		
		return v;
	}

	public void populate() {
		if(mOperation == null) return;
		if(mOperation.equals(TestManager.ADDITION)) {
			getActivity().getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_red));
			getActivity().getActionBar().setTitle(" " + getResources().getString(R.string.button_addition));
			mStartTestButton.setBackgroundResource(R.drawable.button_shape_start_test_red);
			mLevelPicker.setMinValue(0);
			mLevelPicker.setMaxValue(7);
			mLevelPicker.setDisplayedValues(getResources().getStringArray(R.array.addition_difficulty));
		}
		else if(mOperation.equals(TestManager.SUBTRACTION)) {
			getActivity().getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_blue));
			getActivity().getActionBar().setTitle(" " + getResources().getString(R.string.button_subtraction));
			mStartTestButton.setBackgroundResource(R.drawable.button_shape_start_test_blue);
			mLevelPicker.setMinValue(0);
			mLevelPicker.setMaxValue(7);
			mLevelPicker.setDisplayedValues(getResources().getStringArray(R.array.subtraction_difficulty));
		}
		else if(mOperation.equals(TestManager.MULTIPLICATION)) {
			getActivity().getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_green));
			getActivity().getActionBar().setTitle(" " + getResources().getString(R.string.button_multiplication));
			mLevelTextView.setText(getResources().getString(R.string.multiplier));
			mStartTestButton.setBackgroundResource(R.drawable.button_shape_start_test_green);
			mLevelPicker.setMinValue(0);
			mLevelPicker.setMaxValue(12);
			mLevelPicker.setDisplayedValues(getResources().getStringArray(R.array.multiplication_difficulty));
		}
		else if(mOperation.equals(TestManager.DIVISION)) {
			getActivity().getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_purple));
			getActivity().getActionBar().setTitle(" " + getResources().getString(R.string.button_division));
			mLevelTextView.setText(getResources().getString(R.string.divisor));
			mStartTestButton.setBackgroundResource(R.drawable.button_shape_start_test_purple);
			mLevelPicker.setMinValue(0);
			mLevelPicker.setMaxValue(12);
			mLevelPicker.setDisplayedValues(getResources().getStringArray(R.array.division_difficulty));
		}
	}
	
	public void setOperation(String operation) {
		mOperation = operation;
		getView().invalidate();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home:
			if(NavUtils.getParentActivityName(getActivity()) != null) {
				NavUtils.navigateUpFromSameTask(getActivity());
			}
			return true;
		}
		return false;
	}
	
	public void beginTest() {
		PreferenceManager
			.getDefaultSharedPreferences(getActivity())
			.edit()
			.putBoolean(SettingsActivity.FIRST_TIME, false)
			.commit();
		
		Intent intent = new Intent(getActivity(), TestActivity.class);
			intent.putExtra(TestManager.OPERATION, mOperation);
			intent.putExtra(TestManager.QUESTIONS, mNumQuestions);
			intent.putExtra(TestManager.DIFFICULTY, mLevel);
			intent.putExtra(TestManager.TIME, mTime);
			intent.putExtra(TestManager.NAME, mStudentName);
			startActivity(intent);
			getActivity().finish();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == StorageSetupDialog.CONFIGURE_DROPBOX) {
			PreferenceManager.getDefaultSharedPreferences(getActivity())
				.edit()
				.putBoolean(SettingsActivity.DROPBOX_STORAGE, true)
				.commit();
			DropboxUploader uploader = DropboxUploader.getInstance();
			uploader.startLink(getActivity(), requestCode);
		} else {
			beginTest();
		}
	}
	
}
