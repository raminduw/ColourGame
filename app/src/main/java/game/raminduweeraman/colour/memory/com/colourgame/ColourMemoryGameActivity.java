package game.raminduweeraman.colour.memory.com.colourgame;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Ramindu.WEERAMAN on 3/1/2016.
 */
public class ColourMemoryGameActivity extends Activity {
    private static final String TAG = ColourMemoryGameActivity.class.getSimpleName();
    private static int ROW_COUNT = -1;
    private static int COL_COUNT = -1;
    private Context context;
    private int[][] cards;
    private List<Integer> cardImagesList;
    private CardObject firstCard;
    private CardObject secondCard;
    private ButtonListener buttonListener;
    private TextView txtScore;
    private int currentScore = 0;
    private int matchingCount = 0;
    private static Object lock = new Object();
    private TableLayout cardTable;
    private CardsUpdateHandler cardsUpdateHandler;
    private final static int TOTAL_MATCHING_COUNT = 8;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        cardsUpdateHandler = new CardsUpdateHandler();
        loadCardImages();
        setContentView(R.layout.colour_memory_game_activity);

        buttonListener = new ButtonListener();
        cardTable = (TableLayout) findViewById(R.id.cardTable);
        txtScore = (TextView) findViewById(R.id.txtScore);
        context = cardTable.getContext();
        startNewCardGame(IConstants.CARD_GAME_COLUMN_COUNT, IConstants.CARD_GAME_ROW_COUNT);


    }

    private void startNewCardGame(int c, int r) {
        ROW_COUNT = r;
        COL_COUNT = c;

        cards = new int[COL_COUNT][ROW_COUNT];

        TableRow tableRow = ((TableRow) findViewById(R.id.tableRow));
        tableRow.removeAllViews();

        cardTable = new TableLayout(context);
        tableRow.addView(cardTable);

        for (int y = 0; y < ROW_COUNT; y++) {
            cardTable.addView(generateCardGameSingleRow(y));
        }

        firstCard = null;
        loadCards();

        currentScore = 0;
        txtScore.setText("" + currentScore);


    }

    private void loadCardImages() {

        cardImagesList = new ArrayList<Integer>();
        cardImagesList.add(R.drawable.colour1);
        cardImagesList.add(R.drawable.colour2);
        cardImagesList.add(R.drawable.colour3);
        cardImagesList.add(R.drawable.colour4);
        cardImagesList.add(R.drawable.colour5);
        cardImagesList.add(R.drawable.colour6);
        cardImagesList.add(R.drawable.colour7);
        cardImagesList.add(R.drawable.colour8);

    }

    private void loadCards() {
        try {
            int size = ROW_COUNT * COL_COUNT;
            ArrayList<Integer> list = new ArrayList<Integer>();
            for (int i = 0; i < size; i++) {
                list.add(new Integer(i));
            }

            Random random = new Random();
            for (int i = size - 1; i >= 0; i--) {
                int t = 0;
                if (i > 0) {
                    t = random.nextInt(i);
                }
                t = list.remove(t).intValue();
                cards[i % COL_COUNT][i / COL_COUNT] = t % (size / 2);
            }
        } catch (Exception e) {
            Log.e(TAG, "loadCards Exception " + e.getLocalizedMessage());
        }

    }

    private TableRow generateCardGameSingleRow(int y) {
        TableRow row = new TableRow(context);
        row.setHorizontalGravity(Gravity.CENTER);

        for (int x = 0; x < COL_COUNT; x++) {
            row.addView(createImageButton(x, y));
        }
        return row;
    }

    private View createImageButton(int x, int y) {
        Button button = new Button(context);
        //button.setBackground(backImage);
        button.setBackgroundResource(R.drawable.card_bg);
        button.setId(100 * x + y);
        button.setOnClickListener(buttonListener);
        return button;
    }

    class ButtonListener implements OnClickListener {

        @Override
        public void onClick(View v) {

            synchronized (lock) {
                if (firstCard != null && secondCard != null) {
                    return;
                }
                int id = v.getId();
                int x = id / 100;
                int y = id % 100;
                turnCard((Button) v, x, y);
            }

        }

        private void turnCard(Button button, int x, int y) {
            button.setBackgroundResource(cardImagesList.get(cards[x][y]));

            if (firstCard == null) {
                firstCard = new CardObject(button, x, y);
            } else {

                if (firstCard.x == x && firstCard.y == y) {
                    return; //the user pressed the same card
                }

                secondCard = new CardObject(button, x, y);

                TimerTask timerTask = new TimerTask() {

                    @Override
                    public void run() {
                        try {
                            synchronized (lock) {
                                cardsUpdateHandler.sendEmptyMessage(0);
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "turnCard Exception " + e.getLocalizedMessage());
                        }
                    }
                };

                Timer timer = new Timer(false);
                timer.schedule(timerTask, IConstants.CARD_FLIP_DELAY);
            }


        }

    }

    class CardsUpdateHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            synchronized (lock) {
                flipCards();
            }
        }

        private void flipCards() {
            if (cards[secondCard.x][secondCard.y] == cards[firstCard.x][firstCard.y]) {//same card
                currentScore = currentScore + 2;
                txtScore.setText("" + currentScore);
                firstCard.button.setVisibility(View.INVISIBLE);
                secondCard.button.setVisibility(View.INVISIBLE);
                matchingCount++;
                if (matchingCount == TOTAL_MATCHING_COUNT) {
                    showSaveScoreDialog();
                }
            } else {
                currentScore = currentScore - 1;
                txtScore.setText("" + currentScore); //not same card
                secondCard.button.setBackgroundResource(R.drawable.card_bg);
                firstCard.button.setBackgroundResource(R.drawable.card_bg);
            }

            firstCard = null;
            secondCard = null;
        }
    }


    private void showSaveScoreDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_view);
        dialog.setCancelable(false);
        TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText(getString(R.string.save_score_message));
        final EditText edtUserName = (EditText) dialog.findViewById(R.id.username);

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        dialogButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(edtUserName.getText().toString())) {
                    //save currentScore
                    saveScore(edtUserName.getText().toString());
                    dialog.dismiss();
                    startNewCardGame(IConstants.CARD_GAME_COLUMN_COUNT, IConstants.CARD_GAME_ROW_COUNT);
                } else {
                    edtUserName.setError(getString(R.string.username_empty_error));
                }
            }
        });

        dialog.show();

    }


    public void clickOnHighScore(View view) {

        ScoreDataSource scoreDataSource = new ScoreDataSource(this);
        ArrayList<ScoreObject> scoreObjects = scoreDataSource.getAllScores();
        if (scoreObjects.size() > 0) {
            Intent myIntent = new Intent(ColourMemoryGameActivity.this, ScoreListActivity.class);
            ColourMemoryGameActivity.this.startActivity(myIntent);
        } else {
            Toast.makeText(this, getString(R.string.high_scores_not_available),
                    Toast.LENGTH_LONG).show();
        }


    }

    private void saveScore(String userName) {
        ScoreDataSource scoreDataSource = new ScoreDataSource(getApplicationContext());
        ScoreObject obj = scoreDataSource.getCurrentScore(userName);
        if (obj != null) {
            if (obj.getHighestScore() < currentScore) {
                scoreDataSource.addScore(userName, currentScore);
            }
        } else {
            scoreDataSource.addScore(userName, currentScore);
        }


    }


}