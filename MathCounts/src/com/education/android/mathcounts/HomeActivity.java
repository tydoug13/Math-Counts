package com.education.android.mathcounts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class HomeActivity extends FragmentActivity {
	
	private HomePager mViewPager;
	private HomeFragment mHomeFragment;
	private EnterNameFragment mEnterNameFragment;
	private ChooseDifficultyFragment mChooseDifficultyFragment;
	private ChooseTestLengthFragment mChooseTestLengthFragment;
	private EditText mEditText;
	
	private boolean isKeyboard = false;
	private boolean hasMenu = true;
	
	private String mOperation;
	private String mStudentName;
	private String mNumQuestions;
	private String mTime;
	private String mLevel;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DropboxUploader.newInstance(getApplicationContext());
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_main));
		
		setContentView(R.layout.viewpagertest);

		FragmentManager fm = getSupportFragmentManager();
		
		mViewPager = (HomePager) findViewById(R.id.viewPagerTest);
		mViewPager.setId(R.id.viewPagerTest);
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
			@Override
			public Fragment getItem(int pos) {
				if(pos == 0) {
					mHomeFragment = new HomeFragment();
					return mHomeFragment;
				} else if(pos == 1) {
					mEnterNameFragment = (EnterNameFragment) EnterNameFragment.newInstance();
					return mEnterNameFragment;
				} else if(pos == 2) {
					mChooseDifficultyFragment = new ChooseDifficultyFragment();
					return mChooseDifficultyFragment;
				} else if(pos == 3) {
					mChooseTestLengthFragment = new ChooseTestLengthFragment();
					return mChooseTestLengthFragment;
				}
				
				return new Fragment();
			}

			@Override
			public void setPrimaryItem(ViewGroup container, int position,
					Object object) {
				
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				if(position != 1) {
					if(isKeyboard) {
						imm.hideSoftInputFromWindow(container.getWindowToken(), 0);
						isKeyboard = false;
					}
				}
				
				if(position == 0) 
					getActionBar().setDisplayHomeAsUpEnabled(false);
				
				super.setPrimaryItem(container, position, object);
			}

			@Override
			public int getCount() {
				return 4;
			}
			
		});
		
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				if(arg0 == ViewPager.SCROLL_STATE_DRAGGING) {
					hasMenu = false;
					invalidateOptionsMenu();
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}

			@Override
			public void onPageSelected(int arg0) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				mHomeFragment.resetImageViews();
				
				if(arg0 != 1 && isKeyboard) {
					imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
					isKeyboard = false;
				}
				
				if(arg0 == 0) {
					hasMenu = true;
					invalidateOptionsMenu();
				} else {
					hasMenu = false;
					invalidateOptionsMenu();
				}
				
				if(arg0 == 0) {
					mHomeFragment.setSelection(HomeFragment.NONE_PRESSED);
					getActionBar().setTitle(" " + getResources().getString(R.string.app_name));
					getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_main));
					mViewPager.setPagingEnabled(false);
				} else if(arg0 == 1) {
					if(!isKeyboard) {
						imm.toggleSoftInputFromWindow(mEditText.getWindowToken(), 0, 0);
						mEditText.requestFocus();
						isKeyboard = true;
					}
				}
			}
			
		});
		mViewPager.setPagingEnabled(false);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		invalidateOptionsMenu();
	} 

	 @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		if(hasMenu) {
			getMenuInflater().inflate(R.menu.menu_home, menu);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.menu_previous_tests:
				Intent i = new Intent(this, SavedTestsActivity.class);
				startActivity(i);
				finish();
				return true;
			case R.id.menu_settings:
				boolean isProtected = PreferenceManager
					.getDefaultSharedPreferences(getApplicationContext())
					.getBoolean(SettingsActivity.SETTINGS_PASSWORD_TOGGLE, false);
				
				if(isProtected) {
					FragmentManager fm = getSupportFragmentManager();
					DialogFragment dialog = EnterPasswordDialogFragment.newInstance();
					dialog.setTargetFragment(null, 0);
					dialog.show(fm, getResources().getString(R.string.PasswordDialog));
				} else {
					Intent j = new Intent(this, SettingsActivity.class);
					startActivity(j);
					finish();
				}
			    return true;
			
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void setEditText(EditText et) {
		mEditText = et;
	}

	@Override
	public void onBackPressed() {
		if(mViewPager.getCurrentItem() != 0) {
			mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
			if(mViewPager.getCurrentItem() == 0) {
				mHomeFragment.setSelection(HomeFragment.NONE_PRESSED);
				getActionBar().setTitle(" " + getResources().getString(R.string.MathCounts));
				getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_main));
			}
		} else {
			super.onBackPressed();
		}
	}
	
	public HomePager getHomePager() {
		return mViewPager;
	}
	
	public void beginTest() {
		if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean(SettingsActivity.FIRST_TIME, true) &&
				!PreferenceManager.getDefaultSharedPreferences(this).getBoolean(SettingsActivity.DROPBOX_STORAGE, false)) {
			DialogFragment dialog = StorageSetupDialog.newInstance();
			dialog.setTargetFragment(mHomeFragment, StorageSetupDialog.CONFIGURE_DROPBOX);
			dialog.show(getSupportFragmentManager(), "");

			PreferenceManager
				.getDefaultSharedPreferences(this)
				.edit()
				.putBoolean(SettingsActivity.FIRST_TIME, false)
				.commit();
		} else {
			int level;
			if(mLevel.equals(getResources().getString(R.string.easy))) {
				level = 1;
			} else if(mLevel.equals(getResources().getString(R.string.medium))) {
				level = 2;
			} else if(mLevel.equals(getResources().getString(R.string.hard))) {
				level = 3;
			} else if(mLevel.equals(getResources().getString(R.string.expert))) {
				level = 4;
			} else if(mLevel.equals(getResources().getString(R.string.all))) {
				level = 13;
			} else {
				level = Integer.parseInt(mLevel);
			}
			
			Intent intent = new Intent(this, TestActivity.class);
				intent.putExtra(TestManager.OPERATION, mOperation);
				intent.putExtra(TestManager.QUESTIONS, Integer.parseInt(mNumQuestions));
				intent.putExtra(TestManager.DIFFICULTY, level);
				intent.putExtra(TestManager.TIME, mTime);
				intent.putExtra(TestManager.NAME, mStudentName);
				startActivity(intent);
				finish();
		}
	}
	
	public String getOperation() {
		return mOperation;
	}

	public void setOperation(String operation) {
		mOperation = operation;
	}

	public String getStudentName() {
		return mStudentName;
	}

	public void setStudentName(String studentName) {
		mStudentName = studentName;
	}

	public String getNumQuestions() {
		return mNumQuestions;
	}

	public void setNumQuestions(String numQuestions) {
		mNumQuestions = numQuestions;
	}

	public String getLevel() {
		return mLevel;
	}

	public void setLevel(String level) {
		mLevel = level;
	}

	public String getTime() {
		return mTime;
	}

	public void setTime(String time) {
		mTime = time;
	}

	public HomePager getViewPager() {
		return mViewPager;
	}
	
	public void updateButton() {
		mEnterNameFragment.populate();
	}
}
