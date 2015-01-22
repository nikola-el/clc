package com.exnoke.battery.cycle;

import android.app.*;
import android.content.*;
import android.os.*;
import android.widget.*;

public class MainActivity extends Activity
{
	@Override
	protected void onStart()
	{
		super.onStart();
		Intent i = getIntent();
		boolean alarm = false;

		try
		{
			alarm = i.getBooleanExtra("alarm", false);
		}
		catch (Exception e)
		{}

		Cycle.setCycle(this);

		if (alarm)
		{
			Cycle.setDiff(this);
			setAlarm();
			notifyShow(Cycle.get(this, "diff") > 0.7);
		}
		else
		{
			Toast.makeText(this, Cycle.getCycle(this), Toast.LENGTH_LONG).show();
			Toast.makeText(this, Cycle.getWeek(this), Toast.LENGTH_LONG).show();
		}
		finish();
	}

	private void setAlarm()
	{
		AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
		Intent alarmIntent = new Intent(this, MainActivity.class);
		alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		alarmIntent.putExtra("alarm", true);
		PendingIntent pInt = PendingIntent.getActivity(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		try
		{
			am.cancel(pInt);
		}
		catch (Exception e)
		{}

		Long time = System.currentTimeMillis() + AlarmManager.INTERVAL_DAY;
		Cycle.setLong(this, "alarm", time);
		if (Build.VERSION.SDK_INT >= 19)
		{
			am.setExact(AlarmManager.RTC_WAKEUP, time, pInt);
		}
		else
		{
			am.set(AlarmManager.RTC_WAKEUP, time, pInt);
		}
	}

	private void notifyShow(boolean show)
	{
		PendingIntent notifyPIntent = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), 0);

		Notification noti = new Notification();
		String contentText ="Cycle: " + Cycle.getCycle(this) + ", last day: " + Cycle.getDiff(this);
		noti.icon = R.drawable.ic_launcher;
		noti.priority = show ?Notification.PRIORITY_DEFAULT: Notification.PRIORITY_MIN;
		noti.flags = Notification.FLAG_AUTO_CANCEL;
		noti.defaults = Notification.DEFAULT_ALL;
		noti.setLatestEventInfo(this, "Cycle", contentText, notifyPIntent);

		NotificationManager note = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		note.cancelAll();
		note.notify("cycle", 0, noti);
	}
}
