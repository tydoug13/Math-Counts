package com.education.android.mathcounts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class SettingsActivity extends FragmentActivity {
	public static final String FIRST_TIME = "first_time";
	public static final String DROPBOX_STORAGE = "pref_dropbox";
	public static final String SETTINGS_PASSWORD = "settings_password";
	public static final String SETTINGS_PASSWORD_TOGGLE = "settings_password_toggle";
	public static final String RATE_OPTION = "rate_option"; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fragment);
		FragmentManager fm = getSupportFragmentManager();
		Fragment fragment = createFragment();
		fm.beginTransaction().add(android.R.id.content, fragment).commit();
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_main));
	}
	
	protected Fragment createFragment() {
		return new SettingsFragment();
	}

	@Override
	protected void onResume() {
		super.onResume();
		replaceFragment();
	}
	
	public void replaceFragment() {
		FragmentManager fm = getSupportFragmentManager();
		Fragment fragment = createFragment();
		fm.beginTransaction().replace(android.R.id.content, fragment).commit();
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		startActivity(new Intent(this, HomeActivity.class));
		finish();
	}
}
