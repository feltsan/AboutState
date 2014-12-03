package john.aboutstate.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import john.aboutstate.R;
import john.aboutstate.support.DatabaseHelper;
import john.aboutstate.support.ListViewAdapter;


/**
 * Created by john on 28.11.14.
 */
public class SavedStateFragment extends Fragment {
    DatabaseHelper dbHelper = null;
    SQLiteDatabase db = null;
    View rootView;
    HashMap<String, String> item;
    private ListView listView;
    private ArrayList<HashMap<String, String>> arrayList;
    private ListViewAdapter adapter;
    Cursor c;
    ImageView backButton, saveButton;
    TextView titleApp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.listview_main,container,false);
        listView = (ListView) rootView.findViewById(R.id.listview);
        backButton = (ImageView) getActivity().findViewById(R.id.back_button);
        saveButton = (ImageView) getActivity().findViewById(R.id.save_state_imgv);
        titleApp = (TextView)getActivity().findViewById(R.id.state_title);

        backButton.setVisibility(View.INVISIBLE);
        saveButton.setVisibility(View.INVISIBLE);
        titleApp.setText(R.string.app_name);

        dbHelper = new DatabaseHelper(getActivity());
        db = dbHelper.getWritableDatabase();

        arrayList = new ArrayList<HashMap<String, String>>();

         c = db.query(DatabaseHelper.TABLE_STATE, new String[]{DatabaseHelper.UID,DatabaseHelper.LAT_STATE,
                DatabaseHelper.LNG_STATE, DatabaseHelper.NAME_STATE,
                DatabaseHelper.CAPITAL_STATE, DatabaseHelper.REGION_STATE,DatabaseHelper.AREA_STATE,
                DatabaseHelper.CALL_CODE_STATE}, null, null, null, null, null);

        if (c.moveToFirst()) {
            do {
                item = new HashMap<String, String>();
                item.put("name_state", c.getString(c.getColumnIndex(DatabaseHelper.NAME_STATE)));
                arrayList.add(item);

                Log.d("myLogs", "ID = " + c.getString(c.getColumnIndex(DatabaseHelper.UID)) + ", name = "
                        + c.getString(c.getColumnIndex(DatabaseHelper.NAME_STATE)) );

            } while (c.moveToNext());

        }

        adapter = new ListViewAdapter(rootView.getContext(), arrayList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("saved_name_state", arrayList.get(position).get("name_state"));

                Fragment fragment = new DetailsStateFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container, fragment);
                ft.addToBackStack(null);
                fragment.setArguments(bundle);
                ft.commitAllowingStateLoss();
            }
        });

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent("SendCode");
//                intent.putExtra("code", arrayList.get(position).get("code"));
//                intent.putExtra("name_state", arrayList.get(position).get("name_state"));
//
//                getActivity().sendBroadcast(intent);
//            }
//        });


        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        backButton.setVisibility(View.VISIBLE);
        saveButton.setVisibility(View.VISIBLE);
    }
}
