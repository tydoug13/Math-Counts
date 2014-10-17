package com.education.android.mathcounts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HomeFragment extends Fragment {
	
	public static final int NONE_PRESSED = 0;
	public static final int ADDITION_PRESSED = 1;
	public static final int SUBTRACTION_PRESSED = 2;
	public static final int MULTIPLICATION_PRESSED = 3;
	public static final int DIVISION_PRESSED = 4;

	private ImageView mAdditionImageView;
	private ImageView mSubtractionImageView;
	private ImageView mMultiplicationImageView;
	private ImageView mDivisionImageView;
	private TextView mPrompt;
	
	private ImageView mChallengeButton;
	private ImageView mShadow;
	
	private int selection;
	
	private Activity mActivity;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mActivity = (HomeActivity) getActivity();
		
		setRetainInstance(true);
		View v = inflater.inflate(R.layout.fragment_home, container, false);
		
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		selection = NONE_PRESSED;
		
		mShadow = (ImageView) v.findViewById(R.id.challengeShadow);
		mChallengeButton = (ImageView) v.findViewById(R.id.challengeButton);
		mChallengeButton.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch(event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					mShadow.setVisibility(View.VISIBLE);
					enableButtons(false, false, false, false, true);
					return true;
				case MotionEvent.ACTION_UP:
					mShadow.setVisibility(View.INVISIBLE);
					enableButtons(true, true, true, true, true);
					if(isOutOfBounds(v, event)) {
						mShadow.setVisibility(View.INVISIBLE);
						return true;
					}
					Intent i = new Intent(getActivity(), ChallengeActivity.class);
					startActivity(i);
					getActivity().finish();
					return true;
					
				}
				return false;
			}
		});
		
		mAdditionImageView = (ImageView) v.findViewById(R.id.home_addition_button);
		mAdditionImageView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				HomePager pager = (HomePager) ((HomeActivity) getActivity()).getViewPager();
				
				switch(arg1.getActionMasked()) {
				case MotionEvent.ACTION_DOWN:
					resetImageViews();
					if(selection == ADDITION_PRESSED)
						mAdditionImageView.setImageResource(R.drawable.addition_button_pressed200dp_grey);
					else
						mAdditionImageView.setImageResource(R.drawable.addition_button_pressed200dp);
					
					enableButtons(true, false, false, false, false);
					return true;
				case MotionEvent.ACTION_UP:
					enableButtons(true, true, true, true, true);
					resetImageViews();
					if(selection == ADDITION_PRESSED) {
						if(isOutOfBounds(arg0, arg1)) {
							mAdditionImageView.setImageResource(R.drawable.ggshimmer200);
							return true;
						} else {
							getActivity().getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_main));
							getActivity().getActionBar().setTitle(" " + getResources().getString(R.string.MathCounts));
							mAdditionImageView.setImageResource(R.drawable.ggshimmer200);
							selection = NONE_PRESSED;
							pager.setPagingEnabled(false);
						}
					} else {
						if(isOutOfBounds(arg0, arg1)) {
							Log.d("Fuck", "aaa");
							mAdditionImageView.setImageResource(R.drawable.ggshimmer200);
							return true;
						} else {
							getActivity().getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_red));
							selection = ADDITION_PRESSED;
							mAdditionImageView.setImageResource(R.drawable.ggshimmer200);
							((HomeActivity) getActivity()).setOperation(TestManager.ADDITION);
							((HomeActivity) mActivity).updateButton();
							pager.setPagingEnabled(false);
							pager.setCurrentItem(1);
						}
					}
					return true;
				}
				return false;
			}
			
		});

		
		mSubtractionImageView = (ImageView) v.findViewById(R.id.home_subtraction_button);
		mSubtractionImageView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				HomePager pager = (HomePager) ((HomeActivity) getActivity()).getViewPager();
				
				switch(arg1.getAction()) {
				case MotionEvent.ACTION_DOWN:
					resetImageViews();
					if(selection != SUBTRACTION_PRESSED) {
						mSubtractionImageView.setImageResource(R.drawable.subtraction_button_pressed200dp);
					} else {
						mSubtractionImageView.setImageResource(R.drawable.subtraction_button_pressed200dp_grey);
					}
					enableButtons(false, true, false, false, false);
					return true;
				
				case MotionEvent.ACTION_UP:
					enableButtons(true, true, true, true, true);
					resetImageViews();
					if(selection == SUBTRACTION_PRESSED) {
						if(isOutOfBounds(arg0, arg1)) {
							mSubtractionImageView.setImageResource(R.drawable.ggsubshimmer200);
							return true;
						} else {
							getActivity().getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_main));
							getActivity().getActionBar().setTitle(" " + getResources().getString(R.string.MathCounts));
							mSubtractionImageView.setImageResource(R.drawable.ggsubshimmer200);
							selection = NONE_PRESSED;
							pager.setPagingEnabled(false);					}
					} else {
						if(isOutOfBounds(arg0, arg1)) {
							Log.d("Fuck", "aaa");
							mSubtractionImageView.setImageResource(R.drawable.ggsubshimmer200);
							return true;
						} else {
							getActivity().getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_blue));
							selection = SUBTRACTION_PRESSED;
							mSubtractionImageView.setImageResource(R.drawable.ggsubshimmer200);
							((HomeActivity) getActivity()).setOperation(TestManager.SUBTRACTION);
							((HomeActivity) mActivity).updateButton();
							pager.setPagingEnabled(false);
							pager.setCurrentItem(1);
						}
					}
					return true;
				}
				return false;
			}
			
		});
		
		mMultiplicationImageView = (ImageView) v.findViewById(R.id.home_multiplication_button);
		mMultiplicationImageView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				HomePager pager = (HomePager) ((HomeActivity) getActivity()).getViewPager();
				
				switch(arg1.getAction()) {
				case MotionEvent.ACTION_DOWN:
					resetImageViews();
					pager.setCurrentItem(0);
					if(selection != MULTIPLICATION_PRESSED)	 {
						mMultiplicationImageView.setImageResource(R.drawable.multiplication_button_pressed200dp);
					} else {
						mMultiplicationImageView.setImageResource(R.drawable.multiplication_button_pressed200dp_grey);
					}

					enableButtons(false, false, true, false, false);
					return true;
				case MotionEvent.ACTION_UP:
					enableButtons(true, true, true, true, true);
					resetImageViews();
					if(selection == MULTIPLICATION_PRESSED) {
						if(isOutOfBounds(arg0, arg1)) {
							mMultiplicationImageView.setImageResource(R.drawable.ggshimmermultiplicaiton200);
							return true;
						} else {
							getActivity().getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_main));
							getActivity().getActionBar().setTitle(" " + getResources().getString(R.string.MathCounts));
							mMultiplicationImageView.setImageResource(R.drawable.ggshimmermultiplicaiton200);
							selection = NONE_PRESSED;
							pager.setPagingEnabled(false);
						}
					} else {
						if(isOutOfBounds(arg0, arg1)) {
							Log.d("Fuck", "aaa");
							mMultiplicationImageView.setImageResource(R.drawable.ggshimmermultiplicaiton200);
							return true;
						} else {
							getActivity().getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_green));
							selection = MULTIPLICATION_PRESSED;
							mMultiplicationImageView.setImageResource(R.drawable.ggshimmermultiplicaiton200);
							((HomeActivity) getActivity()).setOperation(TestManager.MULTIPLICATION);
							((HomeActivity) mActivity).updateButton();
							pager.setPagingEnabled(false);
							pager.setCurrentItem(1);
						}
					}
					return true;
				}
				return false;
			}
			
		});
		
		mDivisionImageView = (ImageView) v.findViewById(R.id.home_division_button);
		mDivisionImageView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				HomePager pager = (HomePager) ((HomeActivity) getActivity()).getViewPager();
				
				switch(arg1.getAction()) {
				case MotionEvent.ACTION_DOWN:
					resetImageViewsCheck();
					if(selection != DIVISION_PRESSED) {
						mDivisionImageView.setImageResource(R.drawable.division_button_pressed200dp);
					} else {
						mDivisionImageView.setImageResource(R.drawable.division_button_pressed200dp_grey);
					}

					enableButtons(false, false, false, true, false);
					return true;
				case MotionEvent.ACTION_UP:
					enableButtons(true, true, true, true, true);
					resetImageViews();
					if(selection == DIVISION_PRESSED) {
						if(isOutOfBounds(arg0, arg1)) {
							mDivisionImageView.setImageResource(R.drawable.ggdivshimmer200);
							return true;
						} else {
							getActivity().getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_main));
							getActivity().getActionBar().setTitle(" " + getResources().getString(R.string.MathCounts));
							mDivisionImageView.setImageResource(R.drawable.ggdivshimmer200);
							selection = NONE_PRESSED;
							pager.setPagingEnabled(false);
						}
					} else {
						if(isOutOfBounds(arg0, arg1)) {
							Log.d("Fuck", "aaa");
							mDivisionImageView.setImageResource(R.drawable.ggdivshimmer200);
							return true;
						} else {
							getActivity().getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_purple));
							selection = DIVISION_PRESSED;
							mDivisionImageView.setImageResource(R.drawable.ggdivshimmer200);
							((HomeActivity) getActivity()).setOperation(TestManager.DIVISION);
							((HomeActivity) mActivity).updateButton();
							pager.setPagingEnabled(false);
							pager.setCurrentItem(1);
						}
					}
					return true;
				}
				return false;
			}
		});
		
		return v;
	}

	
	public void resetImageViews()	 {
		mAdditionImageView.setImageResource(R.drawable.ggshimmer200);
		mSubtractionImageView.setImageResource(R.drawable.ggsubshimmer200);
		mMultiplicationImageView.setImageResource(R.drawable.ggshimmermultiplicaiton200);
		mDivisionImageView.setImageResource(R.drawable.ggdivshimmer200);
	}
	
	public void resetImageViewsCheck() {
		if(selection == ADDITION_PRESSED) {
			mSubtractionImageView.setImageResource(R.drawable.ggsubshimmer200);
			mMultiplicationImageView.setImageResource(R.drawable.ggshimmermultiplicaiton200);
			mDivisionImageView.setImageResource(R.drawable.ggdivshimmer200);
		} else if(selection == SUBTRACTION_PRESSED) {
			mAdditionImageView.setImageResource(R.drawable.ggshimmer200);
			mMultiplicationImageView.setImageResource(R.drawable.ggshimmermultiplicaiton200);
			mDivisionImageView.setImageResource(R.drawable.ggdivshimmer200);
		} else if(selection == MULTIPLICATION_PRESSED) {
			mAdditionImageView.setImageResource(R.drawable.ggshimmer200);
			mSubtractionImageView.setImageResource(R.drawable.ggsubshimmer200);
			mDivisionImageView.setImageResource(R.drawable.ggdivshimmer200);
		} else if(selection == DIVISION_PRESSED) {
			mAdditionImageView.setImageResource(R.drawable.ggshimmer200);
			mSubtractionImageView.setImageResource(R.drawable.ggsubshimmer200);
			mMultiplicationImageView.setImageResource(R.drawable.ggshimmermultiplicaiton200);
		}
	}
	

	public void disableImageViews() {
		mAdditionImageView.setClickable(false);
		mSubtractionImageView.setClickable(false);
		mMultiplicationImageView.setClickable(false);
		mDivisionImageView.setClickable(false);
	}
	
	public void enableImageViews() {
		mAdditionImageView.setClickable(true);
		mSubtractionImageView.setClickable(true);
		mMultiplicationImageView.setClickable(true);
		mDivisionImageView.setClickable(true);
	}
	
	public void setSelection(int selection) {
		this.selection = selection;
	}
	
	/**
	 * To enable and disable the four operation ImageViews
	 * @param isAdditionEnabled
	 * @param isSubtractionEnabled
	 * @param isMultiplicationEnabled
	 * @param isDivisionEnabled
	 */
	private void enableButtons(boolean isAdditionEnabled, boolean isSubtractionEnabled, boolean isMultiplicationEnabled, boolean isDivisionEnabled, boolean isChallengeEnabled) {
		mAdditionImageView.setEnabled(isAdditionEnabled);
		mSubtractionImageView.setEnabled(isSubtractionEnabled);
		mMultiplicationImageView.setEnabled(isMultiplicationEnabled);
		mDivisionImageView.setEnabled(isDivisionEnabled);
		mChallengeButton.setEnabled(isChallengeEnabled);
	}
	
	private boolean isOutOfBounds(View v, MotionEvent evt) {
		int[] position = new int[2];
		v.getLocationInWindow(position);
		
		int width = v.getWidth();
		int height = v.getHeight();
		
		float xCoord = evt.getRawX();
		float yCoord = evt.getRawY();
	
		if(xCoord > position[0] + width || xCoord < position[0])
			return true;
		if(yCoord > position[1] + height || yCoord < position[1])
			return true;
		
		return false;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == StorageSetupDialog.CONFIGURE_DROPBOX) {
			DropboxUploader uploader = DropboxUploader.getInstance();
			uploader.startLink(mActivity, requestCode);
			
			PreferenceManager.getDefaultSharedPreferences(mActivity)
				.edit()
				.putBoolean(SettingsActivity.DROPBOX_STORAGE, true)
				.commit();
		}
	}
}
