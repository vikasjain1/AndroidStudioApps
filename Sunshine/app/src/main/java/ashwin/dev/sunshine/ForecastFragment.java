package ashwin.dev.sunshine;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by vikasjain on 2/16/15.
 */
public class ForecastFragment extends Fragment
{
  private final String LOG = ForecastFragment.class.getSimpleName();

  public ForecastFragment()
  {

  }

  @Override
  public void onCreate(Bundle inSavedInstanceState)
  {
    super.onCreate(inSavedInstanceState);
    setHasOptionsMenu(true);
  }

  @Override
  public void onCreateOptionsMenu(Menu inMenu, MenuInflater inflater)
  {
    inflater.inflate(R.menu.forecast_fragment, inMenu);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup inContainer,
      Bundle inSavedInstanceState)
  {
    View rootView = inflater.inflate(R.layout.fragment_main, inContainer, false);

    ArrayList<String> weekForecastFake = new ArrayList<String>();

    weekForecastFake.add("Today, February 9th: 51");
    weekForecastFake.add("Tuesday, February 10th: 29");
    weekForecastFake.add("Wednesday, February 11th: 34");
    weekForecastFake.add("Thursday, February 12th: 72");
    weekForecastFake.add("Friday, February 13th: 17");

    ArrayAdapter<String> forecastAdapter =
        new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast,
            R.id.list_item_forecast_textview, weekForecastFake);

    // Get a reference to listView and setting an adapter to it
    ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
    listView.setAdapter(forecastAdapter);

    Log.i("Ashwin Testing Sunshine", "onCreateView...");

    return rootView;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem inItem)
  {
    int id = inItem.getItemId();
    if (id == R.id.action_refresh)
    {
      Log.v(LOG, "Refresh Clicked - About to execute FetchWeather Async Task...");
      FetchWeatherTask weatherTask = new FetchWeatherTask();
      weatherTask.execute("94043");
      return true;
    }
    return super.onOptionsItemSelected(inItem);
  }

  public class FetchWeatherTask extends AsyncTask<String, Void, Void>
  {
    private final String LOG = FetchWeatherTask.class.getSimpleName();

    @Override
    protected Void doInBackground(String... inParams)
    {
      Log.i("Ashwin Testing Sunshine", "doInBackground...");

      HttpURLConnection urlConnection = null;
      BufferedReader reader = null;

      // Will contain the raw JSON response as a string.
      String forecastJsonStr = null;

      try
      {
        // Construct the URL for the OpenWeatherMap query
        // Possible parameters are available at OWM's forecast API page, at
        // http://openweathermap.org/API#forecast
        String FORECAST_BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
        String QUERY_PARAM = "q";
        String FORMAT_PARAM = "mode";
        String UNITS_PARAM = "units";
        String DAYS_PARAM = "cnt";
        Uri builtUri =
            Uri.parse(FORECAST_BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM, inParams[0])
                .appendQueryParameter(FORMAT_PARAM, "json")
                .appendQueryParameter(UNITS_PARAM, "metric")
                .appendQueryParameter(DAYS_PARAM, Integer.toString(7)).build();

        // Create the request to OpenWeatherMap, and open the connection
        URL url = new URL(builtUri.toString());

        // make sure it worked properly by adding log tag
        Log.v(LOG, "Built URL: " + builtUri.toString());

        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();

        // Read the input stream into a String
        InputStream inputStream = urlConnection.getInputStream();
        StringBuffer buffer = new StringBuffer();
        if (inputStream == null)
        {
          // Nothing to do.
          forecastJsonStr = null;
        }
        reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = reader.readLine()) != null)
        {
          // Since it's JSON, adding a newline isn't necessary (it won't affect
          // parsing)
          // But it does make debugging a *lot* easier if you print out the
          // completed buffer for debugging.
          buffer.append(line + "\n");
        }

        if (buffer.length() == 0)
        {
          // Stream was empty. No point in parsing.
          forecastJsonStr = null;
        }
        forecastJsonStr = buffer.toString();

        Log.v(LOG, "Forecast JSON String: " + forecastJsonStr);
      }
      catch (IOException e)
      {
        Log.e("PlaceholderFragment", "Error ", e);
        // If the code didn't successfully get the weather data, there's no
        // point in attempting
        // to parse it.
        forecastJsonStr = null;
      }
      finally
      {
        if (urlConnection != null)
        {
          urlConnection.disconnect();
        }
        if (reader != null)
        {
          try
          {
            reader.close();
          }
          catch (final IOException e)
          {
            Log.e("PlaceholderFragment", "Error closing stream", e);
          }
        }
      }
      return null;
    }
  }
}
