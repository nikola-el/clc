package com.exnoke.battery.cycle;

import android.content.*;

public class PowerReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context p1, Intent p2)
	{
		Cycle.setCycleOrRestore(p1.getApplicationContext());
		Cycle.setAlarm(p1, Cycle.getLong(p1, "alarm"));
	}
}
