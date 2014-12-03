package john.aboutstate.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.kevinsawicki.http.HttpRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

import john.aboutstate.R;
import john.aboutstate.support.DatabaseHelper;

/**
 * Created by john on 28.11.14.
 */
public class DetailsStateFragment extends Fragment {
    DatabaseHelper dbHelper = null;
    SQLiteDatabase db = null;
    View rootView;
    LayoutInflater inflator;
    ActionBar actionBar;
    Activity mActivity;
    View v;
    String name_state,saved_name_state,code_state,latitude_state,longtitude_state, capital_state, region_state, area_state, callingCode_state;
    TextView  titleApp_txv,latitude_txv,longtitude_txv, capital_txv, region_txv, area_txv, callingCode_txv;
    ImageView flag_state_imgv, save_state_imgv;
    Bundle bundle;
    ImageView backButton,slideMenu;
    String root;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity=activity;
        mActivity.findViewById(R.id.save_state_imgv);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
        root = Environment.getExternalStorageDirectory().toString();

        backButton = (ImageView) getActivity().findViewById(R.id.back_button);
        slideMenu  = (ImageView) getActivity().findViewById(R.id.slide_menu);
        save_state_imgv = (ImageView) getActivity().findViewById(R.id.save_state_imgv);
        backButton.setVisibility(View.VISIBLE);
        save_state_imgv.setVisibility(View.VISIBLE);
        slideMenu.setVisibility(View.INVISIBLE);

        bundle = getArguments();
        code_state = bundle.getString("code");
        name_state = bundle.getString("name_state");
        saved_name_state = bundle.getString("saved_name_state");
        //Log.d("QAZ", saved_name_state);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.details_state,container,false);

        dbHelper = new DatabaseHelper(getActivity());
        db = dbHelper.getWritableDatabase();

        latitude_txv = (TextView) rootView.findViewById(R.id.latitude_state);
        longtitude_txv = (TextView) rootView.findViewById(R.id.longitude_state);
        capital_txv = (TextView) rootView.findViewById(R.id.capital_state);
        region_txv = (TextView) rootView.findViewById(R.id.region_state);
        area_txv = (TextView) rootView.findViewById(R.id.area_state);
        callingCode_txv = (TextView) rootView.findViewById(R.id.callingCode_state);
        flag_state_imgv = (ImageView) rootView.findViewById(R.id.flag_state);
        titleApp_txv = (TextView) getActivity().findViewById(R.id.state_title);
        titleApp_txv.setText(name_state);

//        actionBar = getActivity().getActionBar();
//        inflator = (LayoutInflater) getActivity()
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        v = inflator.inflate(R.layout.select_state_ab, null);
//        actionBar.setCustomView(v);
//        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);







        if (bundle.getString("name_state")!=null){
            new AsyncJSON().execute();
        }else{
            getDataDB();}





     //   new AsyncJSON().execute();

//        save_state_imgv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ContentValues cv = new ContentValues();
//                cv.put(DatabaseHelper.LAT_STATE, latitude_state);
//                cv.put(DatabaseHelper.LNG_STATE, longtitude_state);
//                cv.put(DatabaseHelper.CAPITAL_STATE, capital_state);
//                cv.put(DatabaseHelper.AREA_STATE, area_state);
//                cv.put(DatabaseHelper.CALL_CODE_STATE, callingCode_state);
//                cv.put(DatabaseHelper.REGION_STATE, region_state);
//                db.insert(DatabaseHelper.TABLE_STATE, null, cv);
//            }
//        });


        Button goToMap = (Button) rootView.findViewById(R.id.btn_gotomap_df);
        goToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("SendLatLng");
                intent.putExtra("lat", latitude_state);
                intent.putExtra("lng", longtitude_state);
                intent.putExtra("area", area_state);
                getActivity().sendBroadcast(intent);

            }
        });


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        backButton.setVisibility(View.VISIBLE);
        save_state_imgv.setVisibility(View.VISIBLE);
        slideMenu.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private class AsyncJSON extends AsyncTask<Object, Long, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected JSONObject doInBackground(Object[] params) {
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
//            ContentValues cv = new ContentValues();
            HttpRequest request = HttpRequest.get(" http://restcountries.eu/rest/v1/alpha/" + code_state.toLowerCase());
            if (request.code() == 200) {
                String response = request.body();
                try {
                    jsonObject = new JSONObject(response);
                    capital_state = jsonObject.getString("capital");
                    region_state = jsonObject.getString("region");
                    area_state = jsonObject.getString("area");
                    jsonArray = jsonObject.getJSONArray("callingCodes");
                    callingCode_state = jsonArray.get(0).toString();
                    jsonArray = jsonObject.getJSONArray("latlng");
                    latitude_state= jsonArray.get(0).toString();
                    longtitude_state= jsonArray.get(1).toString();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return jsonObject;
        }

        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);

            Picasso.with(mActivity)
                    .load("http://www.geognos.com/api/en/countries/flag/"+ code_state +".png")
                    .into(flag_state_imgv);
            latitude_txv.setText(getResources().getString(R.string.latitude) + latitude_state);
            longtitude_txv.setText(getResources().getString(R.string.longitude) + longtitude_state);
            capital_txv.setText(getResources().getString(R.string.capital) + capital_state);
            region_txv.setText(getResources().getString(R.string.region) + region_state);
            area_txv.setText(getResources().getString(R.string.area) + area_state);
            callingCode_txv.setText(getResources().getString(R.string.callingCode) + callingCode_state);
//            Cursor c = db.query(TaskDataBase.TABLE_NAME, new String[]{TaskDataBase.UID, TaskDataBase.NAME,
//                    TaskDataBase.TEXT, TaskDataBase.SMALL_IMAGE, TaskDataBase.BIG_IMAGE}, "name = ?", new String[]{name}, null, null, null);
//
//            c.moveToFirst();

//            tvText.setText(c.getString(c.getColumnIndex(TaskDataBase.TEXT)));
//            tvFName.setText(c.getString(c.getColumnIndex(TaskDataBase.NAME)));
//            imageLoader.DisplayImage(c.getString(c.getColumnIndex(TaskDataBase.BIG_IMAGE)), img);

//            c.close();

        }
    }

    public void getDataDB(){
        Picasso.with(mActivity)
                .load(new File(root + "/saved_images/"+ saved_name_state+ ".jpg"))
                .into(flag_state_imgv);

        Cursor c = db.query(DatabaseHelper.TABLE_STATE, new String[]{DatabaseHelper.NAME_STATE,DatabaseHelper.LAT_STATE, DatabaseHelper.LNG_STATE,
                DatabaseHelper.CAPITAL_STATE, DatabaseHelper.REGION_STATE, DatabaseHelper.AREA_STATE,
                DatabaseHelper.CALL_CODE_STATE},"nameState = ?", new String[]{saved_name_state}, null, null, null);

        c.moveToPosition(0);


        capital_state = c.getString(c.getColumnIndex(DatabaseHelper.CAPITAL_STATE));
        region_state = c.getString(c.getColumnIndex(DatabaseHelper.REGION_STATE));
        area_state = c.getString(c.getColumnIndex(DatabaseHelper.AREA_STATE));
        callingCode_state = c.getString(c.getColumnIndex(DatabaseHelper.CALL_CODE_STATE));
        latitude_state= c.getString(c.getColumnIndex(DatabaseHelper.LAT_STATE));;
        longtitude_state= c.getString(c.getColumnIndex(DatabaseHelper.LNG_STATE));;

        latitude_txv.setText(getResources().getString(R.string.latitude) + latitude_state);
        longtitude_txv.setText(getResources().getString(R.string.longitude) + longtitude_state);
        capital_txv.setText(getResources().getString(R.string.capital) + capital_state);
        region_txv.setText(getResources().getString(R.string.region) + region_state);
        area_txv.setText(getResources().getString(R.string.area) + area_state);
        callingCode_txv.setText(getResources().getString(R.string.callingCode) + callingCode_state);

    }

    public void saveState(){

        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.NAME_STATE, name_state);
        cv.put(DatabaseHelper.LAT_STATE, latitude_state);
        cv.put(DatabaseHelper.LNG_STATE, longtitude_state);
        cv.put(DatabaseHelper.CAPITAL_STATE, capital_state);
        cv.put(DatabaseHelper.AREA_STATE, area_state);
        cv.put(DatabaseHelper.CALL_CODE_STATE, callingCode_state);
        cv.put(DatabaseHelper.REGION_STATE, region_state);
        db.insert(DatabaseHelper.TABLE_STATE, null, cv);
        saveImagetoSD();
        Toast.makeText(getActivity(),"Saved",Toast.LENGTH_SHORT).show();
    }

    public void saveImagetoSD() {
        View content =  getActivity().findViewById(R.id.flag_state);
        content.setDrawingCacheEnabled(true);
        Bitmap bmap = content.getDrawingCache();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();

        String fname = name_state +".jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

//        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE||newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
//            Fragment fragment = new DetailsStateFragment();
//            fragment.setArguments(bundle);
//            FragmentTransaction ft = getFragmentManager().beginTransaction();
//            ft.addToBackStack(null);
//            ft.replace(R.id.container, fragment);
//                        ft.commitAllowingStateLoss();
//        }

    }
}