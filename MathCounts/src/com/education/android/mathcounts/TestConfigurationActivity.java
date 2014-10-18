package com.education.android.mathcounts;

import android.support.v4.app.Fragment;

public class TestConfigurationActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return new TestConfigurationFragment();
	}

}
