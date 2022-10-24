package com.richardphan.teamrandomizer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ViewHolder> {
    private ArrayList<String> players;
    private LayoutInflater inflater;
    private OnItemClick listener;

    public PlayerAdapter(Context context, ArrayList<String> players, OnItemClick listener) {
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
        String player = players.get(position);
        holder.cbActivePlayer.setChecked(true);
        holder.textView.setText(player);
        holder.btnDelete.setTag(player);

        holder.cbActivePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick((String) holder.textView.getText());
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = (String) holder.btnDelete.getTag();

                for (int i = 0; i < players.size(); i++) {
                    if (players.get(i).equals(name)) {
                        players.remove(i);
                        notifyDataSetChanged();
                        return;
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox cbActivePlayer;
        TextView textView;
        ImageButton btnDelete;

        ViewHolder(View itemView) {
            super(itemView);
            cbActivePlayer = itemView.findViewById(R.id.cbActivePlayer);
            textView = itemView.findViewById(R.id.tvPlayerName);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
