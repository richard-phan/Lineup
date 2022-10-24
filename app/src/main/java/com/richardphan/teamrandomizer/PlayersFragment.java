package com.richardphan.teamrandomizer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PlayersFragment extends Fragment implements OnItemClick {
    private View view;
    private RecyclerView recyclerView;
    private PlayerAdapter adapter;
    private Button btnAddPlayer;
    private Button btnClearPlayers;

    private ArrayList<String> players;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.players_fragment, container, false);

        btnAddPlayer = view.findViewById(R.id.btnAddPlayer);
        btnClearPlayers = view.findViewById(R.id.btnClearPlayers);

        btnAddPlayer.setOnClickListener(view -> {
            ((MainActivity) getActivity()).getTeamRandomizer().addPlayer("Player " + (int) (Math.random() * 100));
            adapter.notifyDataSetChanged();
        });

        btnClearPlayers.setOnClickListener(view -> {
            ((MainActivity) getActivity()).getTeamRandomizer().clearPlayers();
            adapter.notifyDataSetChanged();
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
    public void onClick(String value) {
        System.out.println(value);
    }
}
