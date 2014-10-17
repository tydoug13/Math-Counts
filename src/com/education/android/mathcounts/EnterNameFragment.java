package com.education.android.mathcounts;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

public class EnterNameFragment extends Fragment {
	
	public static final String SAVED_NAME = "saved_name";
	
	private HomeActivity mActivity;
	private String mName;
	private String mOperation;
	
	private Button mNextButton;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mActivity = (HomeActivity) getActivity();
		mOperation = mActivity.getOperation();
		mName = "";
		
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		setHasOptionsMenu(true);
		
		View v = inflater.inflate(R.layout.fragment_enter_name, null);
		
		String previousName = PreferenceManager.getDefaultSharedPreferences(mActivity.getApplicationContext()).getString(SAVED_NAME, null);
		
		final CheckBox rememberCheck = (CheckBox) v.findViewById(R.id.rememberMe);
		rememberCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {	
				if(!isChecked) {
					PreferenceManager
						.getDefaultSharedPreferences(mActivity.getApplicationContext())
						.edit()
						.putString(SAVED_NAME, null)
						.commit();
				}
			}
		});
		
		final EditText editName = (EditText) v.findViewById(R.id.etName);
		editName.setText(mName);
		mActivity.setStudentName(mName);
		
		if(previousName != null) {
			editName.setText(previousName);
			rememberCheck.setChecked(true);
		}
		editName.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mName = editName.getText().toString();
				mActivity.setStudentName(mName);
			}
		});
		
		mNextButton = (Button) v.findViewById(R.id.next);
		mNextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {	
				if(rememberCheck.isChecked()) {
					PreferenceManager
						.getDefaultSharedPreferences(mActivity.getApplicationContext())
						.edit()
						.putString(SAVED_NAME, mName)
						.apply();
				}
				
				mActivity.getViewPager().setCurrentItem(2);
			}
		});
		
		populate();
		mActivity.setEditText(editName);
		
		return v;
	}
	
	public static Fragment newInstance() {
		return new EnterNameFragment();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home:
			mActivity.getViewPager().setCurrentItem(0);
			return true;
		}
		return false;
	}
	
	public void populate() {
		mOperation = mActivity.getOperation();
		if(mOperation == null) return;
		if(mOperation.equals(TestManager.ADDITION)) {
			getActivity().getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_red));
			getActivity().getActionBar().setTitle(" " + getResources().getString(R.string.button_addition));
			mNextButton.setBackgroundResource(R.drawable.button_shape_start_test_red);
		}
		else if(mOperation.equals(TestManager.SUBTRACTION)) {
			getActivity().getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_blue));
			getActivity().getActionBar().setTitle(" " + getResources().getString(R.string.button_subtraction));
			mNextButton.setBackgroundResource(R.drawable.button_shape_start_test_blue);
		}
		else if(mOperation.equals(TestManager.MULTIPLICATION)) {
			getActivity().getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_green));
			getActivity().getActionBar().setTitle(" " + getResources().getString(R.string.button_multiplication));
			//mLevelTextView.setText(getResources().getString(R.string.multiplier));
			mNextButton.setBackgroundResource(R.drawable.button_shape_start_test_green);
		}
		else if(mOperation.equals(TestManager.DIVISION)) {
			getActivity().getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_purple));
			getActivity().getActionBar().setTitle(" " + getResources().getString(R.string.button_division));
			//mLevelTextView.setText(getResources().getString(R.string.divisor));
			mNextButton.setBackgroundResource(R.drawable.button_shape_start_test_purple);
		}
	}
	
}
