package com.exnoke.battery.cycle;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.os.*;
import java.text.*;
import java.util.*;

public final class Cycle
{
	protected static final Long time()
	{
		Long time = System.currentTimeMillis();
		time = time - time % AlarmManager.INTERVAL_DAY + 18 * AlarmManager.INTERVAL_HOUR + 480000;
		time = Cycle.fix(time);
		if (TimeZone.getDefault().inDaylightTime(new Date(time)))
		{
			time -= AlarmManager.INTERVAL_HOUR;
		}
		time = Cycle.fix(time);
		return time;
	}

	private static final Long fix(Long l)
	{
		return (l < System.currentTimeMillis()) ?(l + 86400000): l;
	}

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
		return sharedPref.getLong(p2, System.currentTimeMillis() + 60000);
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
		return new DecimalFormat("0.00").format(f);
	}

	protected static final String getCycle(Context p1)
	{
		return format(get(p1, "cycle"));
	}

	protected static final String getValue(Context p1, String p2)
	{
		return format(get(p1, p2));
	}

	protected static final String getWeek(Context p1)
	{
		return format(get(p1, "week")) + " - " + format(get(p1, "diff"));
	}

	protected static final void setCycle(Context p1, boolean p2)
	{
		Float cycle = get(p1, "cycle");
		Float lastLevel = get(p1, "level");
		Float currLevel = getLevel(p1);

		cycle += (Math.abs(lastLevel - currLevel)) / 200;
		set(p1, "cycle", cycle);
		set(p1, "level", currLevel);

		if (wakelockInstalled(p1) & p2)
		{
			Intent wakelock = new Intent("com.exnoke.battery.cycle.BACKUP_STATS");
			wakelock.setPackage("com.exnoke.wakelock");
			wakelock.putExtra("cycle", getJson(p1));
			p1.sendBroadcast(wakelock);
		}
	}

	protected static final void setDiff(Context p1)
	{
		Float currCycle = get(p1, "cycle");
		Float diff = currCycle - get(p1, "week");
		Float elapsed = new Float((System.currentTimeMillis() - getLong(p1, "start"))) / AlarmManager.INTERVAL_DAY;
		Float average = (currCycle - get(p1, "initial")) / elapsed;

		set(p1, "diff", diff);
		set(p1, "week", currCycle);
		set(p1, "average", average);
		set(p1, "min", Math.min(get(p1, "min"), diff));
		set(p1, "max", Math.max(get(p1, "max"), diff));
	}

	protected static final String getJson(Context p1)
	{
		SharedPreferences sharedPref = p1.getSharedPreferences(p1.getString(R.string.settings), Context.MODE_PRIVATE);
		return sharedPref.getAll().toString();
	}

	protected static final boolean wakelockInstalled(Context p1)
	{
		try
		{
			p1.getPackageManager().getPackageInfo("com.exnoke.wakelock", 0);
		}
		catch (PackageManager.NameNotFoundException e)
		{
			return false;
		}
		return true;
	}

	protected static final boolean mustRestore(Context p1)
	{
		if (get(p1, "restore") == 0 & wakelockInstalled(p1))
		{
			return true;
		}
		return false;
	}

	protected static final void setCycleOrRestore(Context p1)
	{
		if (mustRestore(p1))
		{
			restoreBackup(p1);
		}
		else
		{
			setCycle(p1, true);
		}
	}

	private static final void restoreBackup(Context p1)
	{
		Intent restore = new Intent("com.exnoke.battery.cycle.RESTORE_STATS");
		restore.setPackage("com.exnoke.wakelock");
		p1.sendBroadcast(restore);
	}

	protected static final boolean my(Context p1)
	{
		return get(p1, "my") != 0;
	}
}
