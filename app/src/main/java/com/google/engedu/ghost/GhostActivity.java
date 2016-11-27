package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private static final String GHOST_TEXT = "ghostText";
    private static final String IS_USER_TURN = "isUserTurn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    private TextView ghostTextView;
    private TextView statusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            setContentView(R.layout.activity_ghost);
            AssetManager assetManager = getAssets();
            ghostTextView = (TextView) findViewById(R.id.ghostText);
            statusTextView = (TextView) findViewById(R.id.gameStatus);
            try {
                InputStream inputStream = assetManager.open("words.txt");
                dictionary = new FastDictionary(inputStream);
            } catch (IOException e) {
                Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
                toast.show();
            }
            //onStart(null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        char character = (char) event.getUnicodeChar();
        if (Character.isLetter(character) ) {
            ghostTextView.append(String.valueOf(character));
            String text = ghostTextView.getText().toString();
            if (dictionary.isWord(text)) {
                statusTextView.setText("Valid");
            }
            userTurn = false;
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    computerTurn();
                }
            }, 500);
        }
        return super.onKeyUp(keyCode, event);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(GHOST_TEXT, ghostTextView.getText().toString());
        outState.putBoolean(IS_USER_TURN, userTurn);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ghostTextView = (TextView) findViewById(R.id.ghostText);
        statusTextView = (TextView) findViewById(R.id.gameStatus);
        ghostTextView.setText(savedInstanceState.getString(GHOST_TEXT) + " Hello");
        userTurn = savedInstanceState.getBoolean(IS_USER_TURN);
    }

    private void computerTurn() {
        TextView label = (TextView) findViewById(R.id.gameStatus);
        // Do computer turn stuff then make it the user's turn again
        String ghostText = ghostTextView.getText().toString();
        if (dictionary.isWord(ghostText) && ghostText.length() >=4) {
            label.setText("Computer Victory - Already Valid Word");
        } else {
            String longer = dictionary.getAnyWordStartingWith(ghostText);
            if (longer == null) {
                label.setText("Challenge - Computer Victory");
            } else {
                ghostTextView.setText(longer.substring(0,ghostText.length()+1));
                userTurn = true;
                label.setText(USER_TURN);
            }
        }
    }

    public void onChallenge(View view) {
        String ghostText = ghostTextView.getText().toString();
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (dictionary.isWord(ghostText) && ghostText.length() >=4) {
            label.setText("User Victory - Already Valid Word");
        } else {
            String longerWord = dictionary.getAnyWordStartingWith(ghostText);
            if (longerWord != null) {
                label.setText("Computer Victory - " + longerWord);
            } else {
                label.setText("User Victory - No Longer Word Possible");
            }
        }
    }
}
