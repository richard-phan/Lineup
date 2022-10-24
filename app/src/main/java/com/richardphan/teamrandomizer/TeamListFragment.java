package com.richardphan.teamrandomizer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class TeamListFragment extends Fragment {
    public static final String TEAM_OBJECT = "team";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.team_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();

        LinearLayout layout = view.findViewById(R.id.teamList);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(0, 0, 0, 24);

        ArrayList<String> players = args.getStringArrayList(TEAM_OBJECT);
        for (String player : players) {
            TextView tvPlayer = new TextView(getContext());
            tvPlayer.setText(player);
            tvPlayer.setTextSize(18);
            tvPlayer.setPadding(50, 50, 50, 50);
            tvPlayer.setBackgroundResource(R.drawable.list_item);
            tvPlayer.setLayoutParams(params);
            layout.addView(tvPlayer);
        }
    }
}
