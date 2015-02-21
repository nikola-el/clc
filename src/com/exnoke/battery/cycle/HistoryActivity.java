package com.exnoke.battery.cycle;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import java.text.*;

public class HistoryActivity extends Activity
{

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Cycle.setTheme(this, StatsActivity.theme);
		setContentView(R.layout.history);
		if (Build.VERSION.SDK_INT >= 11)
		{
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		setView();
	}

	private void setView()
	{
		Integer[][] views = 
		{
			{R.id.historyTextView00,R.id.historyTextView01,R.id.historyTextView02},
			{R.id.historyTextView10,R.id.historyTextView11,R.id.historyTextView12},
			{R.id.historyTextView20,R.id.historyTextView21,R.id.historyTextView22},
			{R.id.historyTextView30,R.id.historyTextView31,R.id.historyTextView32},
			{R.id.historyTextView40,R.id.historyTextView41,R.id.historyTextView42},
			{R.id.historyTextView50,R.id.historyTextView51,R.id.historyTextView52}
		};


		String[][] data = Cycle.layoutArrayfromString(Cycle.getHistory(this));

		for (int i=0;i < Math.min(views.length,data.length);i++)
		{
			for (int j=0;j < 3;j++)
			{
				TextView view=(TextView)findViewById(views[i][j]);
				view.setText(data[i][j]);
			}
		}
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
