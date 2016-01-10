package com.exnoke.battery.cycle;

import android.content.*;

public class ShutdownReceiver extends BroadcastReceiver
{

	@Override
	public void onReceive(Context p1, Intent p2)
	{
		Context p3 = p1.getApplicationContext();
		
		if (!Cycle.mustRestore(p3))
		{
			Cycle.setCycle(p3, false);
		}
	}

}
