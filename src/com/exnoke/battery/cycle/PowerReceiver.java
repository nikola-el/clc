package com.exnoke.battery.cycle;

import android.content.*;

public class PowerReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context p1, Intent p2)
	{
		Cycle.setCycle(p1);
	}
}
