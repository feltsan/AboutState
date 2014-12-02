package john.aboutstate.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import john.aboutstate.R;
import john.aboutstate.support.ListViewAdapter;


/**
 * Created by john on 28.11.14.
 */
public class SavedStateFragment extends Fragment {
    View rootView;
    HashMap<String, String> item = new HashMap<String, String>();
    private ListView listView;
    private ArrayList<HashMap<String, String>> arrayList;
    private ListViewAdapter adapter;

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
}
