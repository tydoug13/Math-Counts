package com.education.android.mathcounts;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DeleteDialogFragment extends DialogFragment {
	private void sendResult(int resultCode) {
		if(getTargetFragment() == null)
			return;
		
		getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, new Intent());
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		String title = (getTargetRequestCode() == SavedTestsFragment.DELETE_ALL) ? 
				getResources().getString(R.string.DeleteAllTestsQuestion) : getResources().getString(R.string.DeleteTabQuestion);
		String positivePrompt = (getTargetRequestCode() == SavedTestsFragment.DELETE_ALL) ?
				getResources().getString(R.string.DeleteAllTests) : getResources().getString(R.string.DeleteThisTab);
		
		return new AlertDialog.Builder(getActivity())
			.setTitle(title)
			.setPositiveButton(positivePrompt, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					sendResult(Activity.RESULT_OK);
				}
			})
			.setNegativeButton(getResources().getString(R.string.Cancel), null)
			.create();
	}
	
	public static DeleteDialogFragment newInstance() {
		DeleteDialogFragment fragment = new DeleteDialogFragment();
		return fragment;
	}
}
