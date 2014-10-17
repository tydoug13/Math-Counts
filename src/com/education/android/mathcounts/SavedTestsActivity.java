package com.education.android.mathcounts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.education.android.mathcounts.TestDatabaseHelper.TestCursor;

public class SavedTestsActivity extends FragmentActivity implements LoaderCallbacks<Cursor>,
	SavedTestsFragment.TabManager {
	private ArrayList<Test> mTestList;
	private ArrayList<String> mNameList;
	private TestCursor mTestCursor;
	private Handler mHandler;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_fragment);
	    mHandler = new Handler();
	    mTestList = new ArrayList<Test>();
	    mNameList = new ArrayList<String>();
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
		getActionBar().setDisplayShowTitleEnabled(true);
		getActionBar().setTitle(getResources().getString(R.string.testHistory));
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_main));
	    LoaderManager lm = getSupportLoaderManager();
	    lm.initLoader(0, null, this);
	}
	
	private void getNewFragment(Fragment fragment) {
		FragmentManager fm = getSupportFragmentManager();
		fm.beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home:
			startActivity(new Intent(this, HomeActivity.class));
			finish();
			return true;
		}
		return false;
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new TestListCursorLoader(this);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		mTestCursor = (TestCursor) cursor;
		mHandler.post(new Runnable() {
			@Override
			public void run() {
			    updateUI();
			}	
		});
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		
	}
	
	private void updateUI() {
		if(mTestCursor == null) return;
		
		getActionBar().removeAllTabs();
		mNameList = new ArrayList<String>();
		mTestList = new ArrayList<Test>();

	    mTestCursor.moveToFirst();
	    while(!mTestCursor.isAfterLast()) {
	    	Test newTest = mTestCursor.getTest();
	    	String name = newTest.getUser();
	    	
	    	if(mTestCursor.isFirst()) {
	    		mNameList.add(name);
	    		mTestList.add(newTest);
	    		mTestCursor.moveToNext();
	    		continue;
	    	}
	    	
	    	int i = 0;
	    	boolean shouldCreate;
	    	do {
	    		shouldCreate = !newTest.getUser().equals(mTestList.get(i).getUser());
	    		i++;
	    	} while(shouldCreate && i < mTestList.size());
	    	
	    	if(shouldCreate) {
	    		mNameList.add(newTest.getUser());
	    	}
	        
	    	mTestList.add(newTest);
	        mTestCursor.moveToNext();
	    }
	    
	    makeTabs();
	}
	
	private void makeTabs() {
		final ActionBar actionBar = getActionBar();
	    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	    
	    TabListener primaryTabListener = new TabListener() {
			@Override
			public void onTabSelected(Tab tab, FragmentTransaction ft) {
				getNewFragment(SavedTestsFragment.newInstance());
				
				onWindowStartingActionMode(new ActionMode.Callback() {
					
					@Override
					public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
						return false;
					}
					
					@Override
					public void onDestroyActionMode(ActionMode mode) {
						
					}
					
					@Override
					public boolean onCreateActionMode(ActionMode mode, Menu menu) {
						return false;
					}
					
					@Override
					public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
						return false;
					}
				});
			}

			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) {}

			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction ft) {}
	    };
	    
	    TabListener secondaryTabListener = new TabListener() {
			@Override
			public void onTabSelected(Tab tab, FragmentTransaction ft) {
				String name = mNameList.get(tab.getPosition() - 1);
				getNewFragment(SavedTestsFragment.newInstance(name));
				
				onWindowStartingActionMode(new ActionMode.Callback() {
					
					@Override
					public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
						return false;
					}
					
					@Override
					public void onDestroyActionMode(ActionMode mode) {
						
					}
					
					@Override
					public boolean onCreateActionMode(ActionMode mode, Menu menu) {
						return false;
					}
					
					@Override
					public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
						return false;
					}
				});
			}

			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) {}

			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction ft) {}
			
	    };
	    
	    Tab primaryTab = actionBar.newTab();
    	primaryTab.setText(getResources().getString(R.string.allTests));
		actionBar.addTab(primaryTab.setTabListener(primaryTabListener)); 
	    
	    Collections.sort(mNameList, new Comparator<String>() {
			@Override
			public int compare(String s1, String s2) {
				return s1.compareToIgnoreCase(s2);
			}
	    	
	    });
	    
	    for(String name : mNameList) {
	    	Tab secondaryTab = actionBar.newTab();
	    	
	    	if(name.equals(""))
	    		name = getResources().getString(R.string.noName);
	    	
	    	secondaryTab.setText(name);
			actionBar.addTab(secondaryTab.setTabListener(secondaryTabListener));                
	    }
	}

	@Override
	public void updateTabs() {
		mTestCursor.requery();
		updateUI();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		startActivity(new Intent(this, HomeActivity.class));
		finish();
	}
}
