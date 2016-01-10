package com.exnoke.battery.cycle;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class StatsActivity extends Activity
{
	protected static boolean theme = false;
	protected static boolean parent = false;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		if (Build.VERSION.SDK_INT >= 11)
		{
			Cycle.setTheme(this, theme = getIntent().getBooleanExtra("theme", false));
			getActionBar().setDisplayHomeAsUpEnabled(Cycle.wakelockInstalled(this) & (parent = getIntent().getBooleanExtra("parent", false)));
		}

		setContentView(R.layout.stats);
		Cycle.setCycleOrRestore(this);

		//startService(new Intent(this, com.exnoke.battery.notification.NotificationService.class));
		//Cycle.setHistory(this, Cycle.stringElementRemove(Cycle.getHistory(this), 1));

		//Cycle.setHistory(this, "27.09.2015_357.75_0.39#26.09.2015_357.36_1.21#25.09.2015_356.15_0.85#24.09.2015_355.30_0.76#23.09.2015_354.54_0.45#22.09.2015_354.09_0.94#21.09.2015_353.15_0.47#20.09.2015_352.68_0.72#19.09.2015_351.96_0.63");
		//Cycle.set(this, "cycle", 357.88f);
		//Cycle.set(this, "week", 357.75f);
		//Cycle.set(this, "diff", 0.39f);
		//Cycle.set(this, "min", 0.26f);

		Cycle.setAlarm(this, Cycle.getLong(this, "alarm"));
	}

	@Override
	protected void onResume()
	{
		TextView currInfo = (TextView)findViewById(R.id.currInfo);
		currInfo.setText(Cycle.getCycle(this));
		TextView averageInfo = (TextView)findViewById(R.id.averageInfo);
		averageInfo.setText(Cycle.getValue(this, "average"));
		averageInfo.setOnLongClickListener(new LocalListener());
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
		if (!Cycle.getHistory(this).isEmpty())
		{
			Intent history = new Intent(this, HistoryActivity.class);
			startActivity(history);
		}
		else
		{
			Toast.makeText(this, getString(R.string.history_unavailable) , 1).show();
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
	private void fixAverage()
	{
		Float average = Cycle.get(this, "diff");
		Long start = Cycle.getLong(this, "alarm") - 2 * 86400000;
		Float initial = Cycle.get(this, "week");
		String history = Cycle.getHistory(this);
		Integer length = history.split("#").length;
		String[][] historyArray = Cycle.layoutArrayfromString(history);
		Float min = 2f;

		for (int i=0; i < length; i++)
		{
			min = Math.min(min, Float.valueOf(historyArray[i][2]));
		}

		Cycle.set(this, "min", min);
		Cycle.set(this, "initial", initial);
		Cycle.set(this, "average", average);
		Cycle.setLong(this, "start", start);

		Toast.makeText(this, getString(R.string.fixed_average), 0).show();
		onResume();
	}

	private class LocalListener implements View.OnLongClickListener
	{
		@Override
		public boolean onLongClick(View p1)
		{
			fixAverage();
			return true;
		}
	}
}
