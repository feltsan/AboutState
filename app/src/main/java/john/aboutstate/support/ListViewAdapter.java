package john.aboutstate.support;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import john.aboutstate.R;

/**
 * Created by john on 30.11.14.
 */
public class ListViewAdapter extends BaseAdapter {

    Context context;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<String, String>();
    LayoutInflater inflater;
    ViewHolder holder;

    public ListViewAdapter(Context context, ArrayList<HashMap<String, String>> arrayList) {
        this.context = context;
        data = arrayList;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public HashMap<String, String> getItem(int positions) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {


        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_list_view_layout, null);
            holder = new ViewHolder();

            holder.stateName = (TextView) convertView.findViewById(R.id.txv_state_name);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        resultp = data.get(position);
        holder.stateName.setText(resultp.get("name_state"));

        return convertView;
    }

    static class ViewHolder {
        TextView stateName;

    }
}
