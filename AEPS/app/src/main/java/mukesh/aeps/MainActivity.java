package mukesh.aeps;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import mukesh.aeps.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    public String secret_ke="aHR0cHM6Ly93d3cuYWNwbC5pbi5uZXQvRk0yMjBfUkVHSVNUUkFUSU9OX1NFUlZJQ0UvTWdtdEluZm9QYWdlLmFzbXg";
    public void doSomething(){
        int AEPS_REQUEST_CODE = 10923;
// Set a value to AEPS_REQUEST_CODE

        Intent intent = new Intent(this, EkoPayActivity.class);
        Bundle bundle = new Bundle();

//Initialize all unknown variables and replace all dummy values
        bundle.putString("environment", "uat");
        bundle.putString("product","aeps");
        bundle.putString("secret_key_timestamp", secret_key_timestamp);
        bundle.putString("secret_key", secret_ke);
        bundle.putString("developer_key", "becbbce45f79c6f5109f848acd540567");
        bundle.putString("initiator_id", "9962981729");
        bundle.putString("callback_url", "http://accesscomputech.com/FM220REG/");
//        bundle.putString("callback_url_custom_headers", callback_url_custom_headers); //optional
//        bundle.putString("callback_url_custom_params", callback_url_custom_params); //optional
        bundle.putString("user_code", "20810200");
        bundle.putString("initiator_logo_url", "http://www.accesscomputech.com/assets/img/acpl_logo_new.png");
        bundle.putString("partner_name" , "PARTNER Name INC");
        bundle.putString("language" , "en");

        intent.putExtras(bundle);
        startActivityForResult(intent, AEPS_REQUEST_CODE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}