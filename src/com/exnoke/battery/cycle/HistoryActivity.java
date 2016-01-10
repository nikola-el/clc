package com.exnoke.battery.cycle;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class HistoryActivity extends Activity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		if (Build.VERSION.SDK_INT >= 11)
		{
			Cycle.setTheme(this, StatsActivity.theme);
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}

		setContentView(R.layout.history);
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
			{R.id.historyTextView50,R.id.historyTextView51,R.id.historyTextView52},
			{R.id.historyTextView60,R.id.historyTextView61,R.id.historyTextView62},
			{R.id.historyTextView70,R.id.historyTextView71,R.id.historyTextView72},
			{R.id.historyTextView80,R.id.historyTextView81,R.id.historyTextView82}
		};

		String[][] data = Cycle.layoutArrayfromString(Cycle.getHistory(this));

		for (int i=0;i < Math.min(views.length, data.length);i++)
		{
			for (int j=0;j < 3;j++)
			{
				TextView view=(TextView)findViewById(views[i][j]);
				view.setText(data[i][j]);
			}
		}

		TextView fixView = (TextView) findViewById(R.id.historyTextView00);
		fixView.setOnLongClickListener(new LocalListener());
	}

	private void fix()
	{
		fix(findViewById(R.id.historyTextView00));
	}

	public void fix(View v)
	{
		TextView v1 = (TextView) v;
		TextView v2 = (TextView) findViewById(R.id.historyTextView10);
		if (v1.getText().equals(v2.getText()) || Cycle.get(this, "diff") < 0.3)
		{
			String[][] data = Cycle.layoutArrayfromString(Cycle.getHistory(this));
			Cycle.set(this, "week", Float.valueOf(data[1][1]));
			Cycle.set(this, "diff", Float.valueOf(data[1][2]));
			Cycle.setHistory(this, Cycle.stringElementRemove(Cycle.getHistory(this), 1));
			Toast.makeText(this, getString(R.string.fixed_dups), 1).show();

			finish();
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

	private class LocalListener implements View.OnLongClickListener
	{
		@Override
		public boolean onLongClick(View p1)
		{
			fix();
			return true;
		}
	}
}
