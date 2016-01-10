package echo.echo.echo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Devipriya on 09-Jan-16.
 */
public class AlarmAdapter extends ArrayAdapter<AlarmItems> {
    static class ViewHolder{
        public  TextView detailText,remindText;
        public Switch check;
    }


    public AlarmAdapter(Context context, ArrayList<AlarmItems> alarmRow) {
        super(context, 0, alarmRow);
    }

    @Override
    public View getView(int position, View customView, ViewGroup parent) {
        // Get the data item for this position
        AlarmItems alarmRow = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (customView == null) {
            customView = LayoutInflater.from(getContext()).inflate(R.layout.alarm_item, parent, false);
        }
        ViewHolder holder=new ViewHolder();
        // Lookup view for data population
        /*holder.idText = (TextView) customView.findViewById(R.id.idText);*/
        holder.detailText = (TextView) customView.findViewById(R.id.detailText);
        holder.remindText = (TextView) customView.findViewById(R.id.remindText);
        holder.check = (Switch) customView.findViewById(R.id.checkSwitch);
        // Populate the data into the template view using the data object
        /*holder.check=(ToggleButton)customView.findViewById(R.id.toggleButton);*/
        holder.detailText.setText(alarmRow.getDetail());
        Log.d("detail",alarmRow.getDetail());
        holder.remindText.setText(alarmRow.getRemind());
        /*holder.idText.setText(String.valueOf(alarmRow.getId()));*/
        /*check.setChecked(alarmRow.isCheck()==1);*/
        holder.check.setChecked(alarmRow.isCheck()==1);
        holder.check.setFocusable(false);
        // Return the completed view to render on screen
        customView.setTag(holder);

        return customView;
    }
}
