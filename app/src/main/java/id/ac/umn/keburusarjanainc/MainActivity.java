package id.ac.umn.keburusarjanainc;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import id.ac.umn.keburusarjanainc.adapter.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager; //isi page tiap tab
    private TabLayout tabLayout; //tab kategori

    //Adapter yang fungsinya untuk mengatur isi page sesuai tab atau page yang dipilih
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = findViewById(R.id.appbar);
        setSupportActionBar(toolbar);

        //ViewPager merupakan holder dari Fragment tiap halaman Kategori
        viewPager = findViewById(R.id.viewPager);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

//        ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        };
//
//        viewPager.addOnPageChangeListener(onPageChangeListener);

        viewPager.setAdapter(viewPagerAdapter);

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //Create Option Menu di TopBar / Action Bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.qr_code_button:
                Intent intent  = new Intent(MainActivity.this, QRCodeScanner.class);
                startActivity(intent);
                break;

            case R.id.about_menu:
                Intent intent1 = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent1);
                break;

            case R.id.fokus_option:
                Intent skipIntent = new Intent(getApplicationContext(), FokusWebActivity.class);
                skipIntent.putExtra(QRCodeScanner.KEY_URL, "http://fokus.ultimagz.com/");
                startActivity(skipIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
