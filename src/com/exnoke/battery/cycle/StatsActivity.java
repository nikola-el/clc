package com.exnoke.battery.cycle;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.util.*;

public class StatsActivity extends Activity
{

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Cycle.setCycleOrRestore(this);
		getActionBar().setDisplayHomeAsUpEnabled(Cycle.wakelockInstalled(this));
		TextView text = (TextView)findViewById(R.id.mainTextView1);
		text.setText(Cycle.getJson(this));
		
		Cycle.set(this, "initial", 132.86f);
		Cycle.set(this, "average", 0.76f);
		Cycle.set(this, "min", 0.33f);
		Cycle.set(this, "max", 1.28f);
		Cycle.setLong(this, "start", 1421258880000l);
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
