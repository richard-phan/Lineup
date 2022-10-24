package com.richardphan.teamrandomizer;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class TeamAdapter extends FragmentStateAdapter {
    private TeamRandomizer teamRandomizer;

    public TeamAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.d("Fragment Creation", String.valueOf(position));

        Fragment fragment = new TeamListFragment();
        Bundle args = new Bundle();

        if (!teamRandomizer.getTeams().isEmpty()) {
            ArrayList<String> team = (ArrayList<String>) teamRandomizer.getTeams().get(position);
            args.putStringArrayList(TeamListFragment.TEAM_OBJECT, team);
            fragment.setArguments(args);
        }

        return fragment;
    }

    @Override
    public int getItemCount() {
        return teamRandomizer.getTeams().size();
    }

    public void setTeamRandomizer(TeamRandomizer teamRandomizer) {
        this.teamRandomizer = teamRandomizer;
    }
}
