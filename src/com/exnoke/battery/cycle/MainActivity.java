package com.exnoke.battery.cycle;

import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;

public class MainActivity extends Activity
{
	private boolean api = Build.VERSION.SDK_INT >= 11;
	
	@Override
	protected void onStart()
	{
		super.onStart();
		boolean alarm = getIntent().getBooleanExtra("alarm", false);

		Cycle.setCycleOrRestore(this);

		if (alarm)
		{
			Cycle.setDiff(this);
			long time = Cycle.time();
			Cycle.setLong(this, "alarm", time);
			Cycle.setAlarm(this, time);
			if (api)
			{
				notifyShow(Cycle.get(this, "diff") >= 1, Cycle.isSnapshotDay());
			}
		}
		else if (api)
		{
			notifyShow(false, Cycle.isSnapshotDay());
		}
		finish();
	}

	private void notifyShow(boolean show, boolean snap)
	{
		PendingIntent notifyPIntent = PendingIntent.getActivity(this, 1, new Intent(this, Cycle.isSnapshotDay() ? HistoryActivity.class: StatsActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK), PendingIntent.FLAG_UPDATE_CURRENT);
		String contentText ="Cycle: " + Cycle.getCycle(this) + ", last day: " + Cycle.getValue(this, "diff");
		String contextTitle = snap ?"Snapshot": "Cycle";
		Notification noti;

		if (Build.VERSION.SDK_INT >= 16)
		{
			noti = new Notification.Builder(this)
				.setContentTitle(contextTitle)
				.setContentText(contentText)
				.setTicker(contentText)
				.setContentIntent(notifyPIntent)
				.setSmallIcon(snap ? R.drawable.ic_menu_quickmemo : R.drawable.ic_launcher)
				.setPriority((snap || show) ?Notification.PRIORITY_DEFAULT: Notification.PRIORITY_MIN)
				.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + (snap ?R.raw.schedule: R.raw.crystal)))
				.setDefaults(Notification.DEFAULT_VIBRATE)
				.build();
		}
		else
		{
			noti = new Notification.Builder(this)
				.setContentTitle(contextTitle)
				.setContentText(contentText)
				.setTicker(contentText)
				.setContentIntent(notifyPIntent)
				.setSmallIcon(snap ? R.drawable.ic_menu_quickmemo : R.drawable.ic_launcher)
				.setPriority((snap || show) ?Notification.PRIORITY_DEFAULT: Notification.PRIORITY_MIN)
				.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + (snap ?R.raw.schedule: R.raw.crystal)))
				.setDefaults(Notification.DEFAULT_VIBRATE)
				.getNotification();
		}

		noti.flags = snap ?48: 16;

		NotificationManager note = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		note.notify("cycle", 0, noti);
	}
}
