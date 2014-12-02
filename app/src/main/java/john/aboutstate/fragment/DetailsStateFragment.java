package john.aboutstate.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.kevinsawicki.http.HttpRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import john.aboutstate.R;

/**
 * Created by john on 28.11.14.
 */
public class DetailsStateFragment extends Fragment {
    View rootView;
    LayoutInflater inflator;
    ActionBar actionBar;
    Activity mActivity;
    View v;
    String name_state,code_state,latitude_state,longtitude_state, capital_state, region_state, area_state, callingCode_state;
    TextView  titleApp_txv,latitude_txv,longtitude_txv, capital_txv, region_txv, area_txv, callingCode_txv;
    ImageView flag_state_imgv;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity=activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
        Bundle bundle = getArguments();
        code_state = bundle.getString("code");
        name_state = bundle.getString("name_state");
        Log.d("QAZ", name_state);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.details_state,container,false);

        latitude_txv = (TextView) rootView.findViewById(R.id.latitude_state);
        longtitude_txv = (TextView) rootView.findViewById(R.id.longitude_state);
        capital_txv = (TextView) rootView.findViewById(R.id.capital_state);
        region_txv = (TextView) rootView.findViewById(R.id.region_state);
        area_txv = (TextView) rootView.findViewById(R.id.area_state);
        callingCode_txv = (TextView) rootView.findViewById(R.id.callingCode_state);
        flag_state_imgv = (ImageView) rootView.findViewById(R.id.flag_state);
        new AsyncJSON().execute();

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
        actionBar = getActivity().getActionBar();

        inflator = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflator.inflate(R.layout.select_state_ab, null);
        actionBar.setCustomView(v);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        titleApp_txv = (TextView) getActivity().findViewById(R.id.state_title);
        titleApp_txv.setText(name_state);
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
}


