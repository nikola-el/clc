package com.exnoke.battery.cycle;

import android.content.*;
import android.widget.*;
import org.json.*;

public class RestoreReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context p1, Intent p2)
	{
		String extra = p2.getStringExtra("cycle");
		JSONObject json = new JSONObject();
		try
		{
			json = new JSONObject(extra);
		}
		catch (JSONException e)
		{}
		SharedPreferences.Editor sharedPref = p1.getSharedPreferences(p1.getString(R.string.settings), Context.MODE_PRIVATE).edit();
		sharedPref.putFloat("cycle", getFloat(json, "cycle"));
		
		sharedPref.putLong("alarm", getLong(json, "alarm"));
		sharedPref.putFloat("level", getFloat(json, "level"));
		sharedPref.putFloat("diff", getFloat(json, "diff"));
		sharedPref.putFloat("week", getFloat(json, "week"));
		
		sharedPref.putFloat("min", getFloat(json, "min"));
		sharedPref.putFloat("max", getFloat(json, "max"));
		sharedPref.putFloat("average", getFloat(json, "average"));
		sharedPref.putLong("start", getLong(json, "start"));
		sharedPref.putFloat("initial", getFloat(json, "initial"));
		
		sharedPref.putFloat("restore", 1f);
		sharedPref.commit();
		Toast.makeText(p1, "Restore successful!", Toast.LENGTH_LONG).show();

		Cycle.setCycle(p1, true);
	}

	private float getFloat(JSONObject p1, String p2)
	{
		try
		{
			return new Float(p1.getDouble(p2));
		}
		catch (JSONException e)
		{}
		return 0f;
	}
	
	private long getLong(JSONObject p1, String p2)
	{
		try
		{
			return p1.getLong(p2);
		}
		catch (JSONException e)
		{}
		return Cycle.time();
	}
}
