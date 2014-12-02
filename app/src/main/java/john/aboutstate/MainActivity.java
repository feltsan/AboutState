package john.aboutstate;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import com.google.android.gms.internal.b;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import john.aboutstate.fragment.AllStateFragment;
import john.aboutstate.fragment.DetailsStateFragment;
import john.aboutstate.fragment.MapStateFragment;


public class MainActivity extends Activity {

    SlidingMenu slide_menu;
    LayoutInflater inflator;
    ActionBar actionBar;
    View v;
    LinearLayout linearLayout;
    LinearLayout linearLayout1;
    TextView titleState;
    Fragment fragment1;
    private FragmentMessBroadcastReceiver fragmentMessBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentMessBroadcastReceiver = new FragmentMessBroadcastReceiver();
        Fragment existingFragment = getFragmentManager().findFragmentById(R.id.container);

        actionBar = getActionBar();
        inflator = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


       if( existingFragment==null ||existingFragment.getClass().equals(AllStateFragment.class) ){
           v = inflator.inflate(R.layout.all_state_action_bar, null);

       }else{
           v = inflator.inflate(R.layout.select_state_ab, null);
       }
        actionBar.setCustomView(v);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        slide_menu = new SlidingMenu(this);
        slide_menu.setMode(SlidingMenu.LEFT);
        slide_menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        slide_menu.setShadowWidthRes(R.dimen.shadow_width);
        slide_menu.setShadowDrawable(R.drawable.shadow);
        slide_menu.setShadowWidth(15);
        slide_menu.setBehindWidth(200);
        slide_menu.setBehindWidthRes(R.dimen.slidingmenu_offset);
//        slide_menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        slide_menu.setFadeDegree(0.35f);
        slide_menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
        slide_menu.setMenu(R.layout.menu);

        linearLayout = (LinearLayout)findViewById(R.id.exit_button);
        linearLayout1 = (LinearLayout) findViewById(R.id.all_state_item);
    }

    public void toggleMenu(View view){
        slide_menu.toggle(true);

    }


    public void backButton(View view){
        super.onBackPressed();

    }


    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter filter = new IntentFilter();
        filter.addAction("SendCode");
        filter.addAction("SendLatLng");
        registerReceiver(fragmentMessBroadcastReceiver, filter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        linearLayout1.setOnClickListener(new View.OnClickListener() {
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

        linearLayout.setOnClickListener(new View.OnClickListener() {
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
                titleState = (TextView) findViewById(R.id.state_title);
                titleState.setText(R.string.map_state);
                ImageView imageView =(ImageView)findViewById(R.id.save_state);
                imageView.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(fragmentMessBroadcastReceiver);
    }
}
