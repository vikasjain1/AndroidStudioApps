package ashwin.dev.spike;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

public final class WeatherJSONParser
{
  private WeatherJSONParser()
  {

  }

  public static double getMaxTemperatureForDay(String inWeatherJsonStr, int inDayIndex)
      throws JSONException
  {
    JSONObject parser = new JSONObject(inWeatherJsonStr);

    JSONArray list = parser.getJSONArray("list");
    JSONObject dayObj = list.getJSONObject(inDayIndex);
    JSONObject tempObj = dayObj.getJSONObject("temp");
    double maxTemp = tempObj.getDouble("max");

    return maxTemp;
  }

  public static void geocoding(String inAddr) throws Exception
  {
    // build a URL
    String s = "http://maps.google.com/maps/api/geocode/json?sensor=false&address=";
    s += URLEncoder.encode(inAddr, "UTF-8");
    URL url = new URL(s);

    // read from the URL
    Scanner scan = new Scanner(url.openStream());
    String str = new String();
    while (scan.hasNext())
    {
      str += scan.nextLine();
    }
    scan.close();

    // build a JSON object
    JSONObject obj = new JSONObject(str);
    if (!obj.getString("status").equals("OK"))
    {
      return;
    }

    // get the first result
    JSONObject res = obj.getJSONArray("results").getJSONObject(0);
    System.out.println(res.getString("formatted_address"));

    JSONObject loc = res.getJSONObject("geometry").getJSONObject("location");
    System.out.println("lat: " + loc.getDouble("lat") + ", lng: " + loc.getDouble("lng"));
  }
}
