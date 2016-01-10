package com.exnoke.battery.cycle;

import android.app.*;
import android.appwidget.*;
import android.content.*;
import android.widget.*;

public class CycleWidget extends AppWidgetProvider
{
	public void onReceive(Context context, Intent intent)
	{
		String action = intent.getAction();
		if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action))
		{

			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.cycle_layout);

			Intent cycleIntent = new Intent(context, StatsActivity.class);
			cycleIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

			if (cycleIntent != null)
			{
				PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, cycleIntent, 0);
				views.setOnClickPendingIntent(R.id.CycleWidget, pendingIntent);
			}

			AppWidgetManager
				.getInstance(context)
				.updateAppWidget(intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS), views);
		}
	}
}
