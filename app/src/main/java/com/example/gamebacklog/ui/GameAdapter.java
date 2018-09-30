package com.example.gamebacklog.ui;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gamebacklog.R;
import com.example.gamebacklog.data.db.GameContract;
import com.example.gamebacklog.data.model.Game;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

    private Cursor cursor;

    // A reference to a OnGameClickListener which will be invoked upon click event, note that this
    // value can be null and should always be checked accordingly before invoking
    private OnGameClickListener onGameClickListener;

    @Override
    public GameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate our item_game layout
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_backlog, parent, false);
        // Instantiate a GameViewHolder and pass our layout as it's view
        return new GameViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GameViewHolder holder, int position) {
        // Move the cursor to the right position
        cursor.moveToPosition(position);
        // Create a game object from the cursor's data
        Game game = new Game();
        game.setId(cursor.getInt(cursor.getColumnIndex(GameContract.COLUMN_NAME_ID)));
        game.setTitle(cursor.getString(cursor.getColumnIndex(GameContract.COLUMN_NAME_TITLE)));
        game.setPlatform(cursor.getString(cursor.getColumnIndex(GameContract.COLUMN_NAME_PLATFORM)));
        game.setDateAdded(cursor.getString(cursor.getColumnIndex(GameContract.COLUMN_NAME_DATE)));
        game.setStatus(cursor.getString(cursor.getColumnIndex(GameContract.COLUMN_NAME_STATUS)));
        game.setNotes(cursor.getString(cursor.getColumnIndex(GameContract.COLUMN_NAME_NOTES)));
        // Bind the game object to the view
        holder.bind(game);
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public void swapCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    /**
     * Set a listener to receive a callback when a user clicks on a game.
     *
     * @param listener the listener which will receive the callback
     */
    public void setOnGameClickListener(OnGameClickListener listener) {
        onGameClickListener = listener;
    }

    public interface OnGameClickListener {
        void onGameClick(Game game);
    }

    /**
     * A wrapper class representing a single view or row within our RecyclerView. The ViewHolder
     * holds a reference to all the views and the game object.
     */
    class GameViewHolder extends RecyclerView.ViewHolder {

        private Game game;
        private final View root;
        private final TextView title;
        private final TextView platform;
        private final TextView status;
        private final TextView date;

        GameViewHolder(View view) {
            super(view);
            root = view;
            title = view.findViewById(R.id.tvTitle);
            date = view.findViewById(R.id.tvDate);
            status = view.findViewById(R.id.tvStatus);
            platform = view.findViewById(R.id.tvPlatform);
        }

        public Game getGame() {
            return game;
        }

        void bind(final Game game) {
            this.game = game;
            title.setText(game.getTitle());
            date.setText(game.getDateAdded());
            status.setText(game.getStatus());
            platform.setText(game.getPlatform());

            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onGameClickListener != null) {
                        onGameClickListener.onGameClick(game);
                    }
                }
            });
        }
    }
}
