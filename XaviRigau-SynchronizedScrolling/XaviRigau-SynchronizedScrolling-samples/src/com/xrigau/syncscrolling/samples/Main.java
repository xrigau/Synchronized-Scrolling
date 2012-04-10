package com.xrigau.syncscrolling.samples;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class Main extends Activity implements OnClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		findViewById(R.id.simple).setOnClickListener(this);
		findViewById(R.id.complex).setOnClickListener(this);
		findViewById(R.id.longer).setOnClickListener(this);
		findViewById(R.id.two).setOnClickListener(this);
		findViewById(R.id.gravity).setOnClickListener(this);
		findViewById(R.id.gmail).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.simple:
			startActivity(new Intent(this, SimpleSyncScrollActivity.class));
			break;
		case R.id.complex:
			startActivity(new Intent(this, ComplexSyncScrollActivity.class));
			break;
		case R.id.longer:
			startActivity(new Intent(this, LongerSyncScrollActivity.class));
			break;
		case R.id.two:
			startActivity(new Intent(this, TwoSyncScrollActivity.class));
			break;
		case R.id.gravity:
			startActivity(new Intent(this, GravitySyncScrollActivity.class));
			break;
		case R.id.gmail:
			startActivity(new Intent(this, GmailSyncScrollActivity.class));
			break;
		}
	}
}