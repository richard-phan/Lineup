package com.richardphan.teamrandomizer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ViewHolder> {
    private ArrayList<Player> players;
    private LayoutInflater inflater;
    private OnItemClick listener;

    public PlayerAdapter(Context context, ArrayList<Player> players, OnItemClick listener) {
        this.inflater = LayoutInflater.from(context);
        this.players = players;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Player player = players.get(position);
        holder.cbActivePlayer.setChecked(players.get(position).getActive());
        holder.tbCaptain.setChecked(players.get(position).getCaptain());
        holder.textView.setText(player.getName());
        holder.textView.setTag(player.getName());

        holder.cbActivePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick("TOGGLE_PLAYER", (String) holder.textView.getText());
            }
        });

        holder.tbCaptain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = (String) holder.textView.getTag();
                listener.onClick("CAPTAIN_PLAYER", name);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = (String) holder.textView.getTag();
                listener.onClick("REMOVE_PLAYER", name);
            }
        });
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox cbActivePlayer;
        ToggleButton tbCaptain;
        TextView textView;
        ImageButton btnDelete;

        ViewHolder(View itemView) {
            super(itemView);
            cbActivePlayer = itemView.findViewById(R.id.cbActivePlayer);
            tbCaptain = itemView.findViewById(R.id.tbCaptain);
            textView = itemView.findViewById(R.id.tvPlayerName);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
