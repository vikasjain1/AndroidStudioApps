package ashwin.dev.sunshine;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity
{
  private final String LOG = MainActivity.class.getSimpleName();

  @Override
  protected void onCreate(Bundle inSavedInstanceState)
  {
    super.onCreate(inSavedInstanceState);

    setContentView(R.layout.activity_main);

    if (inSavedInstanceState == null)
    {
      getSupportFragmentManager().beginTransaction()
          .add(R.id.container, new ForecastFragment()).commit();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu inMenu)
  {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, inMenu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem inItem)
  {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = inItem.getItemId();

    // noinspection SimplifiableIfStatement
    if (id == R.id.action_settings)
    {
      return true;
    }

    return super.onOptionsItemSelected(inItem);
  }
}
