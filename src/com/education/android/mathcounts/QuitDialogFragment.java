package com.education.android.mathcounts;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class QuitDialogFragment extends DialogFragment {
	public static final String EXTRA_CHOICE = "com.getmoney.mathcounts.choice";
	public static final int FROM_TEST_ACTIVITY = 0;
	public static final int FROM_CHALLENGE = 1;
	
	private int key;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		builder.setTitle(getResources().getString(R.string.QuitQuestion))
			.setPositiveButton(getResources().getString(R.string.Quit), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(key == FROM_TEST_ACTIVITY)
						((TestActivity) getTargetFragment().getActivity()).quitTest();
					if(key == FROM_CHALLENGE)
						((ChallengeTestActivity) getTargetFragment().getActivity()).quitTest();
				}
			}).setNegativeButton(getResources().getString(R.string.Cancel), new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			});
		return builder.create();
	}
	
	public void setKey(int key) {
		this.key = key;
	}
}
