package game.raminduweeraman.colour.memory.com.colourgame;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Ramindu.WEERAMAN on 3/1/2016.
 */
public class ScoreArrayAdapter extends ArrayAdapter<ScoreObject> {

    private List<ScoreObject> scoreObjects;
    private Activity activity;

    public ScoreArrayAdapter(Activity activity, List<ScoreObject> list) {
        super(activity, R.layout.score_item, list);
        this.activity = activity;
        this.scoreObjects = list;
    }
    static class ViewHolder {
        TextView txtUserName;
        TextView txtScore;
        TextView txtRank;
        TextView txtTime;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;
        ScoreObject scoreObject = scoreObjects.get(position);

        if (convertView == null) {
            LayoutInflater inflator = LayoutInflater.from(activity);
            view = inflator.inflate(R.layout.score_item, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.txtUserName = (TextView) view.findViewById(R.id.txtUserName);
            viewHolder.txtScore = (TextView) view.findViewById(R.id.txtScore);
            viewHolder.txtRank = (TextView) view.findViewById(R.id.txtRank);
            viewHolder.txtTime = (TextView) view.findViewById(R.id.txtTime);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.txtUserName.setText(scoreObject.getUserName());
        holder.txtScore.setText(""+scoreObject.getHighestScore());
        holder.txtRank.setText(""+(position+1));
        holder.txtTime.setText(scoreObject.getLastUpdated());
        return view;
    }


}
