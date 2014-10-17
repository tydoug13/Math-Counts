package com.education.android.mathcounts;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class StorageSetupDialog extends DialogFragment {
	public static final int CONFIGURE_DROPBOX = 1;
	
	private void sendResult(int resultCode) {
		if(getTargetFragment() == null)
			return;
		
		Intent i = new Intent();
		getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new AlertDialog.Builder(getActivity())
		.setTitle(getString(R.string.storage_prompt))
		.setPositiveButton(getResources().getString(R.string.useDropbox), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				DropboxUploader.newInstance(getActivity().getApplicationContext());
				sendResult(CONFIGURE_DROPBOX);
			}
		})
		.setNegativeButton(getResources().getString(R.string.noThanks), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				sendResult(Activity.RESULT_CANCELED);
			}
		})
		.create();
	}
	
	public static StorageSetupDialog newInstance() {
		StorageSetupDialog fragment = new StorageSetupDialog();
		return fragment;
	}
}
