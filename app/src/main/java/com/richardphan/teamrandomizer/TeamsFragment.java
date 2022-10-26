package com.richardphan.teamrandomizer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;


public class TeamsFragment extends Fragment {
    private TextView tvTeamSize;
    private SeekBar sbTeamSize;
    private Button btnInclusive;
    private Button btnExclusive;
    private TabLayout tabLayout;
    private TabLayoutMediator tabLayoutMediator;
    private ViewPager2 viewPager;
    private TeamAdapter teamAdapter;
    private TeamRandomizer teamRandomizer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.teams_fragment, container, false);

        teamRandomizer = ((MainActivity) getActivity()).getTeamRandomizer();

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        teamAdapter = new TeamAdapter(this);
        tvTeamSize = view.findViewById(R.id.tvTeamSize);
        sbTeamSize = view.findViewById(R.id.sbTeamSize);
        btnInclusive = view.findViewById(R.id.btnInclusive);
        btnExclusive = view.findViewById(R.id.btnExclusive);

        teamAdapter.setTeamRandomizer(teamRandomizer);
        teamAdapter.notifyDataSetChanged();

        viewPager.setSaveEnabled(false);
        viewPager.setAdapter(teamAdapter);

        if (teamRandomizer.getPlayers().size() < 3) {
            sbTeamSize.setEnabled(false);
            tvTeamSize.setText(getString(R.string.team_size) + " NOT ENOUGH PLAYERS");
        } else {
            sbTeamSize.setMin(2);
            sbTeamSize.setMax(teamRandomizer.size() / 2);

            int avg = (sbTeamSize.getMin() + sbTeamSize.getMax()) / 2;
            sbTeamSize.setProgress(avg);

            tvTeamSize.setText(getString(R.string.team_size) + " " + avg);
        }

        sbTeamSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int teamSize, boolean b) {
                tvTeamSize.setText(getString(R.string.team_size) + " " + teamSize);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnInclusive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teamRandomizer.splitPlayers(teamRandomizer.INCLUSIVE, sbTeamSize.getProgress());

                viewPager.setAdapter(teamAdapter);
                new TabLayoutMediator(tabLayout, viewPager,
                        (tab, position) -> tab.setText("TEAM " + (position + 1))
                ).attach();
            }
        });

        btnExclusive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teamRandomizer.splitPlayers(teamRandomizer.EXCLUSIVE, sbTeamSize.getProgress());

                viewPager.setAdapter(teamAdapter);
                tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager,
                        (tab, position) -> tab.setText("TEAM " + (position + 1)));
                tabLayoutMediator.attach();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        viewPager.setAdapter(teamAdapter);
        tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText("TEAM " + (position + 1)));
        tabLayoutMediator.attach();
    }
}

