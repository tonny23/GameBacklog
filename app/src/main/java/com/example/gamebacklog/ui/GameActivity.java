package com.example.gamebacklog.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gamebacklog.R;
import com.example.gamebacklog.data.model.Game;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.gamebacklog.ui.MainActivity.TASK_INSERT_GAME;
import static com.example.gamebacklog.ui.MainActivity.TASK_UPDATE_GAME;

public class GameActivity extends AppCompatActivity {

    private static final String TAG = "GameActivity";

    @BindView(R.id.inputTitle)
    TextInputEditText inputTitle;

    @BindView(R.id.inputPlatform)
    TextInputEditText inputPlatform;

    @BindView(R.id.inputNotes)
    TextInputEditText inputNotes;

    @BindView(R.id.spinnerStatus)
    Spinner spinnerStatus;

    @BindView(R.id.fabSave)
    FloatingActionButton fabSave;

    private ArrayAdapter statusAdapter;
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        statusAdapter = ArrayAdapter.createFromResource(this, R.array.game_status,
                R.layout.support_simple_spinner_dropdown_item);

        spinnerStatus.setAdapter(statusAdapter);
        // Based on the action we will add or update a game
        if (getIntent().getAction() == Intent.ACTION_INSERT) {
            // We are adding a new game
            Log.d(TAG, "onCreate: Inserting");
            fabSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: saving");
                    saveGame();
                }
            });
        } else if (getIntent().getAction() == Intent.ACTION_EDIT) {
            // get game from previous activity
            Log.d(TAG, "onCreate: Editing");
            game = getIntent().getParcelableExtra("game");
            inputTitle.setText(game.getTitle());
            inputNotes.setText(game.getNotes());
            inputPlatform.setText(game.getPlatform());

            // Get the position of the game's status within the adapter
            int spinnerPosition = statusAdapter.getPosition(game.getStatus());
            spinnerStatus.setSelection(spinnerPosition);
            fabSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateGame(game);
                    Log.d(TAG, "onClick: updating");
                }
            });
        } else {
            Log.d(TAG, "onCreate: " + getIntent().getAction());
        }
    }

    /**
     * Check if input is empty and show error message if empty
     * @param input text input
     * @return true if input is empty, false if input is not empty
     */
    public boolean checkEmptyInput(TextInputEditText input){
        boolean empty;
        if (input.getText().toString().length() == 0) {
            empty = true;
            input.setError(getString(R.string.input_required));
        } else {
            empty = false;
        }
        return empty;
    }

    /**
     * Takes the values from the view and attempts to save a game entity within the database.
     * The title and platform input values are required and will result in an error message when
     * empty.
     */
    public void saveGame() {
        String title = inputTitle.getText().toString().trim();
        String notes = inputNotes.getText().toString().trim();
        String platform = inputPlatform.getText().toString().trim();
        String status = spinnerStatus.getSelectedItem().toString();

        if (!checkEmptyInput(inputTitle) && !checkEmptyInput(inputPlatform)) {
            Game game = new Game(title, platform, status, notes);
            new MainActivity.GameAsyncTask(TASK_INSERT_GAME).execute(game);
            Toast.makeText(this, R.string.game_saved, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /**
     * Takes the values from the view and attempts to update the game entity within the database.
     * The title and platform input values are required and will result in an error message when
     * empty.
     */
    private void updateGame(Game game) {
        // Get the input from the Views
        String title = inputTitle.getText().toString().trim();
        String notes = inputNotes.getText().toString().trim();
        String platform = inputPlatform.getText().toString().trim();
        String status = spinnerStatus.getSelectedItem().toString();

        // Validate that the title and platform is not empty
        if (!checkEmptyInput(inputTitle) && !checkEmptyInput(inputPlatform)) {
            game.setTitle(title);
            game.setNotes(notes);
            game.setPlatform(platform);
            game.setStatus(status);
            new MainActivity.GameAsyncTask(TASK_UPDATE_GAME).execute(game);
            Toast.makeText(this, R.string.game_modified, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;

    }
}
