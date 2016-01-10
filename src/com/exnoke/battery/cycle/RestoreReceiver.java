package com.exnoke.battery.cycle;

import android.content.*;
import android.widget.*;
import org.json.*;
import android.text.*;

public class RestoreReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context p1, Intent p2)
	{
		SharedPreferences.Editor sharedPref = p1.getSharedPreferences(p1.getString(R.string.settings), Context.MODE_PRIVATE).edit();
		sharedPref.putFloat("cycle", p2.getFloatExtra("cycle", 0f));
		sharedPref.putFloat("min", p2.getFloatExtra("min", 0f));
		sharedPref.putFloat("max", p2.getFloatExtra("max", 0f));
		sharedPref.putFloat("average", p2.getFloatExtra("average", 0f));
		sharedPref.putFloat("week", p2.getFloatExtra("week", 0f));
		sharedPref.putFloat("diff", p2.getFloatExtra("diff", 0f));
		sharedPref.putString("history", p2.getStringExtra("history"));
		sharedPref.putLong("start", p2.getLongExtra("start", 0l));
		sharedPref.putFloat("initial", p2.getFloatExtra("initial", 0f));
		sharedPref.putFloat("my", p2.getFloatExtra("my", 0f));
		
		sharedPref.putFloat("restore", 1);
		sharedPref.commit();
		Toast.makeText(p1, "Restore successful!", Toast.LENGTH_LONG).show();
		Cycle.setCycle(p1, true);
	}
}
