package com.education.android.mathcounts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class ChallengeActivity extends FragmentActivity {
    
	public static final String CHALLENGE_CHOICE = "choice";
	public static final int SQUARES_CHALLENGE = 0;
	public static final int PERCENTAGES_CHALLENGE = 1;
	public static final int TEN_QUESTION_CHALLENGE = 2;
	public static final int MULTIPLICATION_MADNESS = 3;
	public static final int TWO_MINUTE_DRILL = 4;
	
	private Button mChallengeButton;
	private ViewPager mViewPager;
	private int mChoice;
	private int mPosition;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_challenge_page);
		
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_main));
		getActionBar().setTitle(getResources().getString(R.string.home));
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		FragmentManager fm = getSupportFragmentManager();
		mViewPager = (ViewPager) findViewById(R.id.viewPagerGames);
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
			@Override
			public Fragment getItem(int pos) {
				ChallengeFragment fragment = new ChallengeFragment();
				fragment.setPosition(pos);
				return fragment;
			}

			@Override
			public int getCount() {
				return 5;
			}
		});
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				mPosition = arg0;
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
		});
		
		mChallengeButton = (Button) findViewById(R.id.challengeStartButton);
		mChallengeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ChallengeActivity.this, ChallengeTestActivity.class);
				mChoice = mPosition;
				intent.putExtra(CHALLENGE_CHOICE, mChoice);
				startActivity(intent);
				finish();
			}
		});
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		mViewPager.setOffscreenPageLimit(2);
		mViewPager.setPageMargin((int) -(dm.widthPixels / 4.5));
		mViewPager.setCurrentItem(2);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home:
			Intent i = new Intent(this, HomeActivity.class);
			startActivity(i);
			finish();
			
			return true;
		}
		return false;
	}

	@Override
	public void onBackPressed() {
		startActivity(new Intent(this, HomeActivity.class));
		finish();
		
		super.onBackPressed();
	}
	
}
