package john.aboutstate;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.internal.b;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import john.aboutstate.fragment.AllStateFragment;
import john.aboutstate.fragment.DetailsStateFragment;
import john.aboutstate.fragment.MapStateFragment;
import john.aboutstate.fragment.SavedStateFragment;


public class MainActivity extends Activity {

    SlidingMenu slide_menu;
    LayoutInflater inflator;
    ActionBar actionBar;
    View v;
    LinearLayout allStateLayout,savedStateLayout ,exitLayout ;
    TextView titleState;
    private FragmentMessBroadcastReceiver fragmentMessBroadcastReceiver;
    ImageView saveIcon;
    Calendar c = Calendar.getInstance();
    public static final String APP_SETTINGS = "app_settings";
    public static final String APP_CHECK = "app_check";
    public static final String APP_TIME = "app_time";
    SharedPreferences sp;
    private Editor e;
    private boolean is_check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentMessBroadcastReceiver = new FragmentMessBroadcastReceiver();

        sp = getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE);
        e = sp.edit();

        is_check = sp.getBoolean(MainActivity.APP_CHECK, false);

        visitor();
        includeActionBar();
        includeSlideMenu();

        ImageView backButton = (ImageView) findViewById(R.id.back_button);
        ImageView saveButton = (ImageView) findViewById(R.id.save_state_imgv);
        titleState = (TextView) findViewById(R.id.state_title);
        backButton.setVisibility(View.INVISIBLE);
        saveButton.setVisibility(View.INVISIBLE);
        titleState.setText(getResources().getString(R.string.app_name));



        exitLayout = (LinearLayout)findViewById(R.id.exit_button);
        allStateLayout = (LinearLayout) findViewById(R.id.all_state_item);
        savedStateLayout = (LinearLayout) findViewById(R.id.save_state_item);
        saveIcon =(ImageView)findViewById(R.id.save_state_imgv);
    }

    public void toggleMenu(View view){
        slide_menu.toggle(true);

    }


    public void backButton(View view){
        super.onBackPressed();
        Fragment existingFragment = getFragmentManager().findFragmentById(R.id.container);
    }


    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter filter = new IntentFilter();
        filter.addAction("SendCode");
        filter.addAction("SendLatLng");
        filter.addAction("SendSaved");
        registerReceiver(fragmentMessBroadcastReceiver, filter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        allStateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment1 = new AllStateFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container, fragment1);
                ft.addToBackStack(null);
                ft.commitAllowingStateLoss();
                slide_menu.toggle(true);
            }
        });

        savedStateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment3 = new SavedStateFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container, fragment3);
                ft.addToBackStack(null);
                ft.commitAllowingStateLoss();
                slide_menu.toggle(true);
            }
        });

        exitLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });
    }

    class FragmentMessBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("SendCode")) {
                Bundle bundle = new Bundle();
                bundle.putString("code", intent.getStringExtra("code"));
                bundle.putString("name_state", intent.getStringExtra("name_state"));

                Fragment fragment = new DetailsStateFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container, fragment);
                ft.addToBackStack(null);
                fragment.setArguments(bundle);
                ft.commitAllowingStateLoss();
            }else if(intent.getAction().equals("SendLatLng")){
                Bundle bundle = new Bundle();
                bundle.putString("lat", intent.getStringExtra("lat"));
                bundle.putString("lng", intent.getStringExtra("lng"));
                bundle.putString("area", intent.getStringExtra("area"));

                Fragment fragment2 = new MapStateFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container, fragment2);
                ft.addToBackStack(null);
                fragment2.setArguments(bundle);
                ft.commitAllowingStateLoss();
                titleState.setText(R.string.map_state);

            }
        }
    }

    public void saveState(View view){
        FragmentManager fm = getFragmentManager();
        DetailsStateFragment fragment = (DetailsStateFragment)fm.findFragmentById(R.id.container);
        fragment.saveState();

        Fragment fragment3 = new SavedStateFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.container, fragment3);
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
     //   saveIcon.setVisibility(View.INVISIBLE);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Date date =new Date();
        DateFormat formater = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        formater.setTimeZone(TimeZone.getTimeZone("EET"));

        e.putString(APP_TIME,formater.format(date).toString());
        e.apply();
        unregisterReceiver(fragmentMessBroadcastReceiver);
    }

    public void first() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Welcome!")
                .setMessage("Undestande the world with us!")
                .setIcon(R.drawable.earth)
                .setCancelable(false)
                .setNegativeButton("GO!",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void lastVisit() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Last visit:" )
                .setMessage("")
                .setCancelable(false)
                .setNegativeButton("GO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void includeSlideMenu(){
        slide_menu = new SlidingMenu(this);
        slide_menu.setMode(SlidingMenu.LEFT);
        slide_menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        slide_menu.setShadowWidthRes(R.dimen.shadow_width);
        slide_menu.setShadowDrawable(R.drawable.shadow);
        slide_menu.setShadowWidth(15);
        slide_menu.setBehindWidth(200);
        slide_menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
//        slide_menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        slide_menu.setFadeDegree(0.35f);
        slide_menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
        slide_menu.setMenu(R.layout.menu);
    }
    private void includeActionBar(){
        actionBar = getActionBar();
        inflator = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflator.inflate (R.layout.select_state_ab, null);

        actionBar.setCustomView(v);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
    }
    private void visitor(){
        if(is_check){
            lastVisit();
        }else{
            first();
            e.putBoolean(APP_CHECK,true);
            e.apply();
        }
    }

}
