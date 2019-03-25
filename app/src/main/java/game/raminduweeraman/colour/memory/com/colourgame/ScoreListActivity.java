package game.raminduweeraman.colour.memory.com.colourgame;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import java.util.ArrayList;
/**
 * Created by Ramindu.WEERAMAN on 3/1/2016.
 */
public class ScoreListActivity extends Activity {
    private ListView scoreList;
    private ScoreArrayAdapter scoreArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_list);
        scoreList = (ListView) findViewById(R.id.scoreList);
        scoreArrayAdapter = new ScoreArrayAdapter(this, getScoreList());
        scoreList.setAdapter(scoreArrayAdapter);
    }

    private ArrayList<ScoreObject> getScoreList() {
        ScoreDataSource scoreDataSource = new ScoreDataSource(this);
        ArrayList<ScoreObject> scoreObjects = scoreDataSource.getAllScores();
        return scoreObjects;
    }
}
