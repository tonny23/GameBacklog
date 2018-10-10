package com.example.gamebacklog.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gamebacklog.R;
import com.example.gamebacklog.data.model.Game;

import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

    private List<Game> mGameList;

    // A reference to a OnGameClickListener which will be invoked upon click event, note that this
    // value can be null and should always be checked accordingly before invoking
    private OnGameClickListener onGameClickListener;

    public GameAdapter(List<Game> gameList, OnGameClickListener onGameClickListener) {
        mGameList = gameList;
        this.onGameClickListener = onGameClickListener;
    }

    @Override
    public GameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate our item_game layout
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_backlog, parent, false);
        // Instantiate a GameViewHolder and pass our layout as it's view
        return new GameViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GameViewHolder holder, final int position) {
        final Game game = mGameList.get(position);
        // Bind the game object to the view
        holder.title.setText(game.getTitle());
        holder.date.setText(game.getDateAdded());
        holder.status.setText(game.getStatus());
        holder.platform.setText(game.getPlatform());
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGameClickListener.onGameClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mGameList.size();
    }

    public void swapList (List<Game> newList) {
        mGameList = newList;
        if (newList != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

    public interface OnGameClickListener {
        void onGameClick(int position);
    }

    /**
     * A wrapper class representing a single view or row within our RecyclerView. The ViewHolder
     * holds a reference to all the views and the game object.
     */
    class GameViewHolder extends RecyclerView.ViewHolder {

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

    }
}
