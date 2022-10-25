package com.richardphan.teamrandomizer;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TeamRandomizer {
    private int numPlayers;
    private ArrayList<Player> players;
    private ArrayList<ArrayList<Player>> teams;

    public TeamRandomizer() {
        this.numPlayers = 0;
        this.players = new ArrayList<>();
        this.teams = new ArrayList<>();
    }

    public TeamRandomizer(ArrayList<Player> players) {
        this.numPlayers = players.size();
        this.players = players;
        this.teams = new ArrayList<>();
    }

    public ArrayList<Player> randomizePlayers() {
        ArrayList<Player> ap = getActivePlayers();
        Collections.shuffle(ap);

        return ap;
    }

    public void splitPlayersExclusive(int teamSize) {
        ArrayList<ArrayList<Player>> teams = new ArrayList<>();
        ArrayList<Player> ap = randomizePlayers();
        int numTeams = ap.size() / teamSize;

        for (int i = 0; i < numTeams; i++) {
            List<Player> sublist = ap.subList(i * teamSize, (i + 1) * teamSize);
            ArrayList<Player> team = new ArrayList<>(sublist);
            teams.add(team);
        }

        this.teams = teams;

        Log.d(getClass().getName(), "Teams Exclusive: " + teams);
    }

    public void splitPlayersInclusive(int teamSize) {
        ArrayList<ArrayList<Player>> teams = new ArrayList<>();
        ArrayList<Player> ap = randomizePlayers();
        int numTeams = ap.size() / teamSize;

        for (int i = 0; i < numTeams; i++) {
            teams.add(new ArrayList<>());
        }

        for (int i = 0; i < ap.size(); i++) {
            teams.get(i % numTeams).add(ap.get(i));
        }

        this.teams = teams;

        Log.d(getClass().getName(), "Teams Inclusive: " + teams);
    }

    public void setPlayers(ArrayList<Player> players) {
        this.numPlayers = players.size();
        this.players = players;
        Log.d(getClass().getName(),"set players: " + players);
    }

    public void clearPlayers() {
        this.numPlayers = 0;
        this.players.clear();
        Log.d(getClass().getName(), "cleared players");
    }

    public void togglePlayer(String name) {
        for (Player p : players) {
            String pName = p.getName();
            if (pName.equals(name)) {
                p.toggleActive();
                Log.d(getClass().getName(), "Toggled " + pName);
                return;
            }
        }
    }

    private ArrayList<Player> getActivePlayers() {
        ArrayList<Player> ap = (ArrayList<Player>) players.clone();
        for (int i = ap.size() - 1; i >= 0; i--) {
            if (!ap.get(i).getActive()) {
                ap.remove(i);
            }
        }

        System.out.println(ap);

        return ap;
    }

    public int getActivePlayerCount() {
        return getActivePlayers().size();
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ArrayList<ArrayList<Player>> getTeams() {
        return teams;
    }

    public void addPlayer(Player player) {
        players.add(player);
        numPlayers += 1;
        Log.d(getClass().getName(), "added " + player);
        Log.d(getClass().getName(), "Total Players: " + numPlayers);
    }

    public void removePlayer(String name) {
        for (int i = 0; i < players.size(); i++) {
            String n = players.get(i).getName();
            if (n.equals(name)) {
                players.remove(i);
                return;
            }
        }
    }

    public int size() {
        return players.size();
    }
}
