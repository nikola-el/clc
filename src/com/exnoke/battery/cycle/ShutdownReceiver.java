package com.exnoke.battery.cycle;

import android.content.*;

public class ShutdownReceiver extends BroadcastReceiver
{

	@Override
	public void onReceive(Context p1, Intent p2)
	{
		if (!Cycle.mustRestore(p1))
		{
			Cycle.setCycle(p1, false);
		}
	}

}
