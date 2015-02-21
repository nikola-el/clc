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
		Float restore = 1f;
		String un = "";
		JSONObject json = new JSONObject();
		try
		{
			json = new JSONObject(extra);
		}
		catch (JSONException e)
		{
			un = "un";
			restore = 0f;
		}
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
		
		sharedPref.putString("history", getString(json, "history"));
		
		sharedPref.putFloat("restore", restore);
		sharedPref.commit();
		Toast.makeText(p1, "Restore " + un + "successful!", Toast.LENGTH_LONG).show();

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
	
	private String getString(JSONObject p1, String p2)
	{
		try
		{
			return p1.getString(p2);
		}
		catch (JSONException e)
		{}
		return "";
	}
}
