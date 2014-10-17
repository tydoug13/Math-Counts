package com.education.android.mathcounts;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class SettingsFragment extends PreferenceFragment {

	@Override
	public View onCreateView(LayoutInflater paramLayoutInflater,
			ViewGroup paramViewGroup, Bundle paramBundle) {
		final PreferenceFragment pref = this;
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			setHasOptionsMenu(true);
		}
		getActivity().getActionBar().setDisplayShowTitleEnabled(true);
		getActivity().getActionBar().setTitle(" Settings");
		
		addPreferencesFromResource(R.xml.settings);
		Preference dropboxStorage = getPreferenceManager().findPreference(SettingsActivity.DROPBOX_STORAGE);
		dropboxStorage.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				if((Boolean) newValue == true) {
					FragmentManager fm = getFragmentManager();
					StorageSetupDialog dialog = StorageSetupDialog.newInstance();
					dialog.setTargetFragment(pref, 0);
					dialog.show(fm, "StorageDialog");
					return false;
				}
				DropboxUploader.getInstance().unlinkAccounts();
				return true;
			}
		});
		
		Preference rateOption = getPreferenceManager().findPreference(SettingsActivity.RATE_OPTION);
		rateOption.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference arg0) {
				Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
			    Intent i = new Intent(Intent.ACTION_VIEW, uri);
		        startActivity(i);
				return true;
			}
		});
		
		Preference settingsPassword = getPreferenceManager().findPreference(SettingsActivity.SETTINGS_PASSWORD_TOGGLE);
		settingsPassword.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				if((Boolean) newValue == true) {
					FragmentManager fm = getFragmentManager();
					DialogFragment dialog = CreatePasswordDialogFragment.newInstance();
					dialog.setTargetFragment(pref, 0);
					dialog.show(fm, "PasswordDialog");
					return false;
				}
				return true;
			}
		});
		
		return super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home:
			getActivity().startActivity(new Intent(getActivity(), HomeActivity.class));
			getActivity().finish();
			return true;
		}
		return false;
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
		} else if(resultCode == CreatePasswordDialogFragment.PASSWORD_PROTECT) {
			PreferenceManager.getDefaultSharedPreferences(getActivity())
				.edit()
				.putBoolean(SettingsActivity.SETTINGS_PASSWORD_TOGGLE, true)
				.commit();
			((SettingsActivity) getActivity()).replaceFragment();
		}
	}
}
