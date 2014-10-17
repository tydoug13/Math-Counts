package com.education.android.mathcounts;

import android.content.Intent;
import android.support.v4.app.Fragment;

public class TestResultActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		Intent intent = getIntent();
		return TestResultFragment.newInstance(intent.getLongExtra(TestResultFragment.TEST_ID, -1), 
				intent.getBooleanExtra(TestResultFragment.IS_FROM_TEST, false));
	}

	@Override
	public void onBackPressed() {
		Intent intent = getIntent();
		boolean isFromTest = intent.getBooleanExtra(TestResultFragment.IS_FROM_TEST, false);
		if(isFromTest)
			goBack();
		else startActivity(new Intent(this, SavedTestsActivity.class));
		
		finish();
		super.onBackPressed();			
	}
	
	public void goBack() {
		Intent i = new Intent(this, HomeActivity.class);
		startActivity(i);
		finish();
	}
}
