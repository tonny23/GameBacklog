package com.example.gamebacklog.ui;

import android.content.Intent;
import android.os.AsyncTask;
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
import com.example.gamebacklog.data.db.AppDatabase;
import com.example.gamebacklog.data.model.Game;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements GameAdapter.OnGameClickListener {

    private static final String TAG = "MainActivity";
    public final static int TASK_GET_ALL_GAMES = 0;
    public final static int TASK_DELETE_GAME = 1;
    public final static int TASK_UPDATE_GAME = 2;
    public final static int TASK_INSERT_GAME = 3;

    public static AppDatabase db;

    @BindView(R.id.rvBacklog)
    RecyclerView rvBacklog;

    @BindView(R.id.fabAdd)
    FloatingActionButton fabAdd;

    private static GameAdapter mAdapter;
    private static List<Game> mGameList;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            ButterKnife.bind(this);
            db = AppDatabase.getInstance(this);
            new GameAsyncTask(TASK_GET_ALL_GAMES).execute();

            mAdapter = new GameAdapter(mGameList, this);
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
                            int position = viewHolder.getAdapterPosition();
                            Game game = mGameList.get(position);
                            deleteGame(game);
//                            mGameList.remove(game);
                        }
                    };

        updateUI();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rvBacklog);
    }

    public static void onGameDbUpdated(List list) {
        mGameList = list;
        updateUI();
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
     * @param position the selected game to update
     */
    public void updateGame(int position) {
        Log.d(TAG, "updateGame");
        Intent intent = new Intent(this, GameActivity.class);
        Game gameToUpdate = mGameList.get(position);
        intent.putExtra("game", gameToUpdate);
        intent.setAction(Intent.ACTION_EDIT);
        startActivity(intent);
    }

    /**
     * Delete the swiped game
     *
     * @param game the game that has been swiped
     */
    public void deleteGame(Game game) {
        new GameAsyncTask(TASK_DELETE_GAME).execute(game);
        updateUI();
        Toast.makeText(MainActivity.this, R.string.game_deleted, Toast.LENGTH_SHORT).show();
    }

    /**
     * Retrieve the data from the db
     */
    private static void updateUI() {
        db.gameDao().getAllgames();
        mAdapter.swapList(mGameList);
        // Notify the adapter that it's data has changed so it can redraw itself
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onGameClick(int position) {
        updateGame(position);
    }

    public static class GameAsyncTask extends AsyncTask<Game, Void, List> {

        private int taskCode;

        public GameAsyncTask(int taskCode) {
            this.taskCode = taskCode;
        }

        @Override
        protected List doInBackground(Game... games) {
            switch (taskCode){
                case TASK_DELETE_GAME:
                    db.gameDao().deleteGame(games[0]);
                    break;
                case TASK_UPDATE_GAME:
                    db.gameDao().updateGame(games[0]);
                    break;
                case TASK_INSERT_GAME:
                    db.gameDao().insertGame(games[0]);
                    break;
            }
            //To return a new list with the updated data, we get all the data from the database again.
            return db.gameDao().getAllgames();
        }

        @Override
        protected void onPostExecute(List list) {
            super.onPostExecute(list);
            onGameDbUpdated(list);
        }

    }
}
