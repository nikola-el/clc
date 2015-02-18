package com.exnoke.battery.cycle;

import android.app.*;
import android.content.*;

public class BootReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context p1, Intent p2)
	{
		Cycle.setCycleOrRestore(p1);

		AlarmManager am = (AlarmManager)p1.getSystemService(p1.ALARM_SERVICE);
		Intent alarmIntent = new Intent(p1, MainActivity.class);
		alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		alarmIntent.putExtra("alarm", true);
		PendingIntent pInt = PendingIntent.getActivity(p1, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		am.setExact(AlarmManager.RTC_WAKEUP, Cycle.getLong(p1, "alarm"), pInt);
	}
}
