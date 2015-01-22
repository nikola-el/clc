package com.exnoke.battery.cycle;

import android.app.*;
import android.content.*;
import android.os.*;
import java.text.*;

public final class Cycle
{
	protected static final Float get(Context p1, String p2)
	{
		SharedPreferences sharedPref = p1.getSharedPreferences(p1.getString(R.string.settings), Context.MODE_PRIVATE);
		return sharedPref.getFloat(p2, 0);
	}

	protected static final void set(Context p1, String p2, Float val)
	{
		SharedPreferences.Editor sharedPref = p1.getSharedPreferences(p1.getString(R.string.settings), Context.MODE_PRIVATE).edit();
		sharedPref.putFloat(p2, val);
		sharedPref.commit();
	}

	protected static final Long getLong(Context p1, String p2)
	{
		SharedPreferences sharedPref = p1.getSharedPreferences(p1.getString(R.string.settings), Context.MODE_PRIVATE);
		return sharedPref.getLong(p2, System.currentTimeMillis() + 6 * AlarmManager.INTERVAL_HOUR);
	}

	protected static final void setLong(Context p1, String p2, Long l)
	{
		SharedPreferences.Editor sharedPref = p1.getSharedPreferences(p1.getString(R.string.settings), Context.MODE_PRIVATE).edit();
		sharedPref.putLong(p2, l);
		sharedPref.commit();
	}

	private static final Float getLevel(Context p1)
	{
		Intent p2 = p1.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		Integer status = p2.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		return status.floatValue();
	}

	private static final String format(Float f)
	{
		return new DecimalFormat("#.##").format(f);
	}

	protected static final String getCycle(Context p1)
	{
		return format(get(p1, "cycle"));
	}

	protected static final String getDiff(Context p1)
	{
		return format(get(p1, "diff"));
	}

	protected static final String getWeek(Context p1)
	{
		return format(get(p1, "week")) + " - " + format(get(p1, "diff"));
	}

	protected static final void setCycle(Context p1)
	{
		Float cycle = get(p1, "cycle");
		Float lastLevel = get(p1, "level");
		Float currLevel = getLevel(p1);

		cycle += (Math.abs(lastLevel - currLevel)) / 200;
		set(p1, "cycle", cycle);
		set(p1, "level", currLevel);
	}

	protected static final void setDiff(Context p1)
	{
		Float currCycle = get(p1, "cycle");
		Float lastCycle = get(p1, "week");

		set(p1, "diff", currCycle - lastCycle);
		set(p1, "week", currCycle);
	}
}
