package com.richardphan.teamrandomizer;

import android.util.Log;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class TeamRandomizer {
    private int numPlayers;
    private ArrayList<String> players;
    private List<List<String>> teams;

    public TeamRandomizer() {
        this.numPlayers = 0;
        this.players = new ArrayList<>();
        this.teams = new ArrayList<>();
    }

    public TeamRandomizer(ArrayList<String> players) {
        this.numPlayers = players.size();
        this.players = players;
        this.teams = new ArrayList<>();
    }

    public void randomize() {
        Collections.shuffle(players);
    }

    public void splitPlayersExclusive(int teamSize) {
        List<List<String>> teams = new ArrayList<>();
        int numTeams = numPlayers / teamSize;

        for (int i = 0; i < numTeams; i++) {
            List<String> sublist = players.subList(i * teamSize, (i + 1) * teamSize);
            ArrayList<String> team = new ArrayList<>(sublist);
            teams.add(team);
        }

        this.teams = teams;

        Log.d(getClass().getName(), "Teams Exclusive: " + teams);
    }

    public void splitPlayersInclusive(int teamSize) {
        List<List<String>> teams = new ArrayList<>();
        int numTeams = numPlayers / teamSize;

        for (int i = 0; i < numTeams; i++) {
            teams.add(new ArrayList<>());
        }

        for (int i = 0; i < players.size(); i++) {
            teams.get(i % numTeams).add(players.get(i));
        }

        this.teams = teams;

        Log.d(getClass().getName(), "Teams Inclusive: " + teams);
    }

    public void setPlayers(ArrayList<String> players) {
        this.numPlayers = players.size();
        this.players = players;
        Log.d(getClass().getName(),"set players: " + players);
    }

    public void clearPlayers() {
        this.numPlayers = 0;
        this.players.clear();
        Log.d(getClass().getName(), "cleared players");
    }

    public ArrayList<String> getPlayers() {
        return players;
    }

    public List<List<String>> getTeams() {
        return teams;
    }

    public void addPlayer(String player) {
        players.add(player);
        numPlayers += 1;
        Log.d(getClass().getName(), "added " + player);
        Log.d(getClass().getName(), "Total Players: " + String.valueOf(numPlayers));
    }

    public void removePlayer(String player) {

    }

    public int size() {
        return players.size();
    }
}
