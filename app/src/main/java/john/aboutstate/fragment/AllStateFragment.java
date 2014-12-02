package john.aboutstate.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.github.kevinsawicki.http.HttpRequest;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import john.aboutstate.R;
import john.aboutstate.support.ListViewAdapter;

/**
 * Created by john on 28.11.14.
 */
public class AllStateFragment extends Fragment {
    View rootView;
    LayoutInflater inflator;
    ActionBar actionBar;
    View v;
    HashMap<String, String> item = new HashMap<String, String>();
    private ListView listView;
    private ArrayList<HashMap<String, String>> arrayList;
    private ListViewAdapter adapter;
    LinearLayout linearLayout;
    Activity mActivity;
    ImageView backButton, saveButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.listview_main,container,false);

        backButton = (ImageView) getActivity().findViewById(R.id.back_button);
        saveButton = (ImageView) getActivity().findViewById(R.id.save_state_imgv);
        backButton.setVisibility(View.INVISIBLE);
        saveButton.setVisibility(View.INVISIBLE);

        linearLayout = (LinearLayout) getActivity().findViewById(R.id.all_state_item);
        listView = (ListView) rootView.findViewById(R.id.listview);
        new DownloadJson().execute();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent("SendCode");
                intent.putExtra("code", arrayList.get(position).get("code"));
                intent.putExtra("name_state", arrayList.get(position).get("name_state"));

                getActivity().sendBroadcast(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

//        actionBar = getActivity().getActionBar();
//        inflator = (LayoutInflater) getActivity()
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        v = inflator.inflate(R.layout.all_state_action_bar, null);
//        actionBar.setCustomView(v);
//        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);


//        linearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    @Override
    public void onPause() {
        super.onPause();
        backButton.setVisibility(View.VISIBLE);
        saveButton.setVisibility(View.VISIBLE);
    }

    private class DownloadJson extends AsyncTask<Object, Long, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected JSONObject doInBackground(Object[] params) {
            arrayList = new ArrayList<HashMap<String, String>>();
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray;
            HttpRequest request = HttpRequest.get("https://api.theprintful.com/countries");
            if (request.code() == 200) {
                String response = request.body();
                try {
                    jsonObject = new JSONObject(response);
                    jsonArray = jsonObject.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        jsonObject = jsonArray.getJSONObject(i);
                        map.put("code", jsonObject.getString("code"));
                        map.put("name_state", jsonObject.getString("name"));
                        arrayList.add(map);

//                        ContentValues cv = new ContentValues();
//                        cv.put(DatabaseHelper.TEXT, jsonObject.getString("text"));
//                        cv.put(DatabaseHelper.CASE_ID, jsonObject.getString("caseId"));
//                        db.insert(DatabaseHelper.TABLE_SCENARIO, null, cv);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return jsonObject;

        }

        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);

            adapter = new ListViewAdapter(rootView.getContext(), arrayList);
            listView.setAdapter(adapter);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
