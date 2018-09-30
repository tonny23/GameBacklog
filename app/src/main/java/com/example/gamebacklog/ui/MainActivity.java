package com.example.gamebacklog.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.gamebacklog.R;
import com.example.gamebacklog.data.GameRepository;
import com.example.gamebacklog.data.model.Game;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @BindView(R.id.rvBacklog)
    RecyclerView rvBacklog;

    @BindView(R.id.fabAdd)
    FloatingActionButton fabAdd;

    private GameAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mAdapter = new GameAdapter();
        //Add a animation when a new entry is added to the recyclerview
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(200L);
        itemAnimator.setRemoveDuration(200L);

        rvBacklog.setAdapter(mAdapter);
        rvBacklog.setLayoutManager(new LinearLayoutManager(this));
        rvBacklog.setItemAnimator(itemAnimator);

        // Instantiate an ItemTouchHelper which will listen for move and swipe events on each row
        // within our RecyclerView and it will propagate these events to the itemTouchCallback
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder
                            target) {
                        return false;
                    }

                    //Called when a user swipes left or right on a ViewHolder
                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        Game game = ((GameAdapter.GameViewHolder) viewHolder).getGame();
                        deleteGame(game);
                    }
                };

        updateUI();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rvBacklog);

        // When an entry is clicked on the entry can be updated
        mAdapter.setOnGameClickListener(new GameAdapter.OnGameClickListener() {
            @Override
            public void onGameClick(Game game) {
                updateGame(game);
            }
        });
    }

    /**
     * Navigate to the game activity to add a new game
     */
    @OnClick(R.id.fabAdd)
    public void addGame() {
        Log.d(TAG, "addGame: fab");
        Intent intent = new Intent(this, GameActivity.class);
        intent.setAction(Intent.ACTION_INSERT);
        startActivity(intent);
    }

    /**
     * Navigate to the game actvity to update the selected game
     *
     * @param game the selected game to update
     */
    public void updateGame(Game game) {
        Log.d(TAG, "updateGame");
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("game", game);
        intent.setAction(Intent.ACTION_EDIT);
        startActivity(intent);
    }

    /**
     * Delete the swiped game
     *
     * @param game the game that has been swiped
     */
    public void deleteGame(Game game) {
        GameRepository gameRepository = new GameRepository(MainActivity.this);
        gameRepository.delete(game.getId());
        // Get a new cursor from our database
        Cursor cursor = gameRepository.findAll();
        mAdapter.swapCursor(cursor);
        mAdapter.notifyDataSetChanged();
        Toast.makeText(MainActivity.this, R.string.game_deleted, Toast.LENGTH_SHORT).show();
    }

    /**
     * Retrieve the data from the db
     */
    private void updateUI() {
        GameRepository gameRepository = new GameRepository(this);
        // Get all games from the database and add them to the adapter
        Cursor cursor = gameRepository.findAll();
        mAdapter.swapCursor(cursor);
        // Notify the adapter that it's data has changed so it can redraw itself
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }
}
