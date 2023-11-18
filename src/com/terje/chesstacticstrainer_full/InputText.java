package com.terje.chesstacticstrainer_full;

import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class InputText extends ButtonSprite implements OnClickListener {

	private final String	mTitle;
	private final String	mMessage;
	private final Text		mText;
	private boolean			mIsPassword;
	private String			mValue;
	private Activity 		mContext;
	private EntryListener 	caller;
	private int 			mTextOffsetY;
	private int mTextOffsetX;

	public InputText(float pX, float pY, final String title, final String message, ITextureRegion texture,
			Font font, int textOffsetX, int textOffsetY, VertexBufferObjectManager vbo, Activity context, EntryListener caller) {
		super(pX, pY, texture, vbo, null);

		mMessage = message;
		mTitle = title;
		mContext =  context;
		this.caller = caller;
		mTextOffsetY = textOffsetY;
		mTextOffsetX = textOffsetX;
		mText = new Text(0, 0, font, "                            ",vbo);
		mText.setColor(Color.BLACK);
		attachChild(mText);
		setOnClickListener(this);
	}

	public String getText() {
		return mValue;
	}

	public boolean isPassword() {
		return mIsPassword;
	}

	@Override
	public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
		showTextInput();
	}

	public void setPassword(final boolean isPassword) {
		mIsPassword = isPassword;
	}

	public void setText(String text) {
		mValue = text;
		if (isPassword() && text.length() > 0)
			text = String.format("%0" + text.length() + "d", 0).replace("0", "*");
		mText.setText(text);
		mText.setPosition(mTextOffsetX+mText.getWidth()/2,mTextOffsetY);
	}

	public void showTextInput() {
		mContext.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				final AlertDialog.Builder alert = new AlertDialog.Builder(mContext);

				alert.setTitle(InputText.this.mTitle);
				alert.setMessage(InputText.this.mMessage);
				

				final EditText editText = new EditText(mContext);
				editText.setTextSize(25f);
				editText.setText(InputText.this.mValue);
				editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
				editText.setGravity(Gravity.CENTER_HORIZONTAL);
				editText.setLines(1);
				if (isPassword())
					editText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME | InputType.TYPE_TEXT_VARIATION_PASSWORD);

				alert.setView(editText);

				alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						setText(editText.getText().toString());
						caller.okPressed();
					}
				});

				alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						caller.cancelPressed();
					}
				});

				final AlertDialog dialog = alert.create();
				dialog.setOnShowListener(new OnShowListener() {
					@Override
					public void onShow(DialogInterface dialog) {
						editText.requestFocus();
						final InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
					}
				});
				dialog.show();
			}
		});
	}

}