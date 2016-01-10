package com.exnoke.battery.cycle;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.os.*;
import android.text.*;
import java.text.*;
import java.util.*;

public final class Cycle
{
	protected static final Long time()
	{
		Long time = System.currentTimeMillis();
		time = time - time % AlarmManager.INTERVAL_DAY + 18 * AlarmManager.INTERVAL_HOUR + 480000;
		time = Cycle.fix(time);
		if (TimeZone.getDefault().inDaylightTime(new Date(time + AlarmManager.INTERVAL_DAY)))
		{
			time -= AlarmManager.INTERVAL_HOUR;
		}
		time = Cycle.fix(time);
		return time;
	}

	public static final void setAlarm(Context p1, long time)
	{
		AlarmManager am = (AlarmManager)p1.getSystemService(p1.ALARM_SERVICE);
		Intent alarmIntent = new Intent(p1, MainActivity.class);
		alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		alarmIntent.putExtra("alarm", true);
		PendingIntent pInt = PendingIntent.getActivity(p1, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		try
		{
			am.cancel(pInt);
		}
		catch (Exception e)
		{}

		if (Build.VERSION.SDK_INT >= 19)
		{
			am.setExact(AlarmManager.RTC_WAKEUP, time, pInt);
		}
		else
		{
			am.set(AlarmManager.RTC_WAKEUP, time, pInt);
		}
	}

	protected static final boolean isSnapshotDay()
	{
		Calendar cal = Calendar.getInstance();
		int ciso = cal.get(cal.DAY_OF_YEAR) + 5 * cal.get(cal.YEAR);
		return (ciso % 9 == 7);
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

	public static final Long getLong(Context p1, String p2)
	{
		SharedPreferences sharedPref = p1.getSharedPreferences(p1.getString(R.string.settings), Context.MODE_PRIVATE);
		return sharedPref.getLong(p2, time());
	}

	protected static final void setLong(Context p1, String p2, Long l)
	{
		SharedPreferences.Editor sharedPref = p1.getSharedPreferences(p1.getString(R.string.settings), Context.MODE_PRIVATE).edit();
		sharedPref.putLong(p2, l);
		sharedPref.commit();
	}

	private static final Long getStart(Context p1)
	{
		Long current = System.currentTimeMillis();
		SharedPreferences sharedPref = p1.getSharedPreferences(p1.getString(R.string.settings), Context.MODE_PRIVATE);
		if (sharedPref.contains("start"))
		{
			return sharedPref.getLong("start", current);
		}
		else
		{
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.putLong("start", current);
			editor.commit();
			return current;
		}
	}

	protected static final String getHistory(Context p1)
	{
		SharedPreferences sharedPref = p1.getSharedPreferences(p1.getString(R.string.settings), Context.MODE_PRIVATE);
		return sharedPref.getString("history", "");
	}

	protected static final void setHistory(Context p1, String p2)
	{
		SharedPreferences.Editor sharedPref = p1.getSharedPreferences(p1.getString(R.string.settings), Context.MODE_PRIVATE).edit();
		sharedPref.putString("history", p2);
		sharedPref.commit();
	}

	public static final Float getLevel(Context p1)
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

	protected static final void setCycle(Context p1, boolean p2)
	{
		getStart(p1);
		Float cycle = get(p1, "cycle");
		Float lastLevel = get(p1, "level");
		Float currLevel = getLevel(p1);

		cycle += (Math.abs(lastLevel - currLevel)) / 200;
		set(p1, "cycle", cycle);
		set(p1, "level", currLevel);

		if (wakelockInstalled(p1) & p2)
		{
			Intent wakelock = new Intent(p1.getPackageName() + ".BACKUP_STATS");
			wakelock.setPackage("com.exnoke.wakelock");
			wakelock.putExtra("cycle", cycle);
			wakelock.putExtra("min", get(p1, "min"));
			wakelock.putExtra("max", get(p1, "max"));
			wakelock.putExtra("average", get(p1, "average"));
			wakelock.putExtra("week", get(p1, "week"));
			wakelock.putExtra("diff", get(p1, "diff"));
			wakelock.putExtra("history", getHistory(p1));
			wakelock.putExtra("start", getStart(p1));
			wakelock.putExtra("initial", get(p1, "initial"));
			wakelock.putExtra("my", get(p1, "my"));
			p1.sendBroadcast(wakelock);
		}
	}

	protected static final void setDiff(Context p1)
	{
		Float currCycle = get(p1, "cycle");
		Float diff = currCycle - get(p1, "week");
		Float elapsed = new Float(System.currentTimeMillis() - getStart(p1)) / AlarmManager.INTERVAL_DAY;
		Float average = (currCycle - get(p1, "initial")) / elapsed;

		set(p1, "diff", diff);
		set(p1, "week", currCycle);
		set(p1, "average", average);
		if (get(p1, "min") > 0)
		{
			set(p1, "min", Math.min(get(p1, "min"), diff));
		}
		else
		{
			set(p1, "min", 1f);
		}
		set(p1, "max", Math.max(get(p1, "max"), diff));

		String history = getHistory(p1);
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		String newElement = sdf.format(Calendar.getInstance().getTime()) + "_" + getValue(p1, "week") + "_" + getValue(p1, "diff");

		setHistory(p1, stringElementChange(history, newElement));
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
		Intent restore = new Intent(p1.getPackageName() + ".RESTORE_STATS");
		restore.setPackage("com.exnoke.wakelock");
		p1.sendBroadcast(restore);
	}

	protected static final void setTheme(Context p1, boolean theme)
	{
		p1.setTheme(theme ?R.style.DarkTheme: R.style.LightTheme);
	}

	protected static final String[][] layoutArrayfromString(String initial)
	{
		String[] array = initial.split("#");
		String[][] global=new String[array.length][];
		for (int i=0; i < array.length; i++)
		{
			global[i] = array[i].split("_");
		}
		return global;
	}

	protected static final String stringElementChange(String initial, String element)
	{
		String[] global = initial.split("#");
		String[] edited = new String[Math.min(global.length + 1, 20)];
		edited[0] = element;
		System.arraycopy(global, 0, edited, 1, Math.min(global.length, 19));
		return TextUtils.join("#", edited);
	}

	protected static final String stringElementRemove(String initial, int num)
	{
		String[] global = initial.split("#");
		String[] edited = new String[global.length - num];
		System.arraycopy(global, num, edited, 0, edited.length);
		return TextUtils.join("#", edited);
	}
}
