package com.richardphan.teamrandomizer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PlayersFragment extends Fragment implements OnItemClick {
    private BadgeUpdateListener listener;
    private View view;
    private RecyclerView recyclerView;
    private PlayerAdapter adapter;
    private EditText etPlayerName;
    private Button btnAddPlayer;
    private Button btnClearPlayers;

    private ArrayList<Player> players;

    public PlayersFragment(BadgeUpdateListener listener) {
        super();
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.players_fragment, container, false);

        etPlayerName = view.findViewById(R.id.etPlayerName);
        btnAddPlayer = view.findViewById(R.id.btnAddPlayer);
        btnClearPlayers = view.findViewById(R.id.btnClearPlayers);

        btnAddPlayer.setOnClickListener(view -> {
            String name = etPlayerName.getText().toString();
            if (name.length() > 0) {
                this.onClick("ADD_PLAYER", name);
                etPlayerName.setText("");
            } else {
                Toast.makeText(getContext(), "Missing player name", Toast.LENGTH_SHORT).show();
            }
        });

        btnClearPlayers.setOnClickListener(view -> {
            this.onClick("CLEAR_PLAYER", null);
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        players = ((MainActivity) getActivity()).getTeamRandomizer().getPlayers();

        recyclerView = view.findViewById(R.id.playerList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        adapter = new PlayerAdapter(this.getContext(), players, this);
        recyclerView.setAdapter(adapter);
    }

    public void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(String action, String value) {
        TeamRandomizer tr = ((MainActivity) getActivity()).getTeamRandomizer();
        switch (action) {
            case "ADD_PLAYER":
                tr.addPlayer(new Player(value));
                listener.increment();
                adapter.notifyDataSetChanged();
                break;
            case "REMOVE_PLAYER":
                tr.removePlayer(value);
                listener.decrement();
                adapter.notifyDataSetChanged();
                break;
            case "CLEAR_PLAYER":
                tr.clearPlayers();
                listener.set(0);
                adapter.notifyDataSetChanged();
            case "TOGGLE_PLAYER":
                tr.togglePlayer(value);
                listener.set(tr.getActivePlayerCount());
                break;
            case "CAPTAIN_PLAYER":
                tr.toggleCaptain(value);
                break;
        }
    }
}
