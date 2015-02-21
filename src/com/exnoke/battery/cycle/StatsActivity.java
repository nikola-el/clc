package com.exnoke.battery.cycle;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import android.text.*;
import android.content.*;

public class StatsActivity extends Activity
{
	protected static boolean theme = false;
	protected static boolean parent = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Cycle.setTheme(this, theme = getIntent().getBooleanExtra("theme", false));
		setContentView(R.layout.stats);
		Cycle.setCycleOrRestore(this);
		if(Build.VERSION.SDK_INT >= 11)
		{
			getActionBar().setDisplayHomeAsUpEnabled(Cycle.wakelockInstalled(this) & (parent = getIntent().getBooleanExtra("parent", false)));
		}
	}

	@Override
	protected void onResume()
	{
		TextView currInfo = (TextView)findViewById(R.id.currInfo);
		currInfo.setText(Cycle.getCycle(this));
		TextView averageInfo = (TextView)findViewById(R.id.averageInfo);
		averageInfo.setText(Cycle.getValue(this, "average"));
		TextView lastInfo = (TextView)findViewById(R.id.lastInfo);
		lastInfo.setText(Cycle.getValue(this, "week"));
		TextView diffInfo = (TextView)findViewById(R.id.diffInfo);
		diffInfo.setText(Cycle.getValue(this, "diff"));
		try
		{
			TextView minInfo = (TextView)findViewById(R.id.minInfo);
			minInfo.setText(Cycle.getValue(this, "min"));
			TextView maxInfo = (TextView)findViewById(R.id.maxInfo);
			maxInfo.setText(Cycle.getValue(this, "max"));
		}
		catch (Exception e)
		{}

		super.onResume();
	}

	public void showHistory(View v)
	{
		Intent history = new Intent(this, HistoryActivity.class);
		startActivity(history);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
