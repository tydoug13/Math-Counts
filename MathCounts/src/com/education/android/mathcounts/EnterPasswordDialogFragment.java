package com.education.android.mathcounts;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EnterPasswordDialogFragment extends DialogFragment {
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		View v = getActivity().getLayoutInflater().inflate(R.layout.enter_password_dialog_layout, null);
		
		final EditText field1 = (EditText) v.findViewById(R.id.password_field1);
		
		final AlertDialog dialog = new AlertDialog.Builder(getActivity())
			.setView(v)
			.setTitle(getResources().getString(R.string.EnterPassword))
			.setPositiveButton(android.R.string.ok, null)
			.setNegativeButton(getResources().getString(R.string.Cancel), new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {}
			})
			.create();
		
		dialog.show();
		
		Button positive = (Button) dialog.getButton(DialogInterface.BUTTON_POSITIVE);
		positive.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String text1 = field1.getText().toString();
				String passwordText = PreferenceManager.getDefaultSharedPreferences(getActivity())
						.getString(SettingsActivity.SETTINGS_PASSWORD, "");
				
				if(!text1.equals("") && text1.equals(passwordText)) {
					Intent j = new Intent(getActivity(), SettingsActivity.class);
					startActivity(j);
					dialog.dismiss();
				} else {
					Toast.makeText(getActivity(), getResources().getString(R.string.PasswordIncorrect), Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		return dialog;
	}
	
	public static DialogFragment newInstance() {
		Bundle args = new Bundle();
		DialogFragment frag = new EnterPasswordDialogFragment();
		frag.setArguments(args);
		return frag;
	}
}
