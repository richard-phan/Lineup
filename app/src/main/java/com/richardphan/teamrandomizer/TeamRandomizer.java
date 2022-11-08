package com.richardphan.teamrandomizer;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

public class TeamRandomizer {
    public final boolean INCLUSIVE = true;
    public final boolean EXCLUSIVE = false;

    private ArrayList<Player> players;
    private ArrayList<ArrayList<Player>> teams;

    public TeamRandomizer() {
        this.players = new ArrayList<>();
        this.teams = new ArrayList<>();
    }

    public TeamRandomizer(ArrayList<Player> players, ArrayList<ArrayList<Player>> teams) {
        this.players = players;
        this.teams = teams;
    }

    public ArrayList<Player> randomizePlayers() {
        ArrayList<Player> ap = getActivePlayers();
        Collections.shuffle(ap);
        return arrangeCaptains(ap);
    }

    public ArrayList<Player> arrangeCaptains(ArrayList<Player> players) {
        ArrayList<Player> arranged = new ArrayList<>();
        for (Player p : players) {
            if (p.getCaptain()) {
                arranged.add(0, p);
            } else {
                arranged.add(p);
            }
        }

        return arranged;
    }

    public void splitPlayers(boolean inclusive, int teamSize) {
        ArrayList<ArrayList<Player>> teams = new ArrayList<>();
        ArrayList<Player> ap = randomizePlayers();
        int numTeams = ap.size() / teamSize;

        for (int i = 0; i < numTeams; i++) {
            teams.add(new ArrayList<>());
        }

        int nPlayers = inclusive ? ap.size() : numTeams * teamSize;
        System.out.println(nPlayers);

        for (int i = 0; i < nPlayers; i++) {
            teams.get(i % numTeams).add(ap.get(i));
        }

        this.teams = teams;

        Log.d(getClass().getName(), "Teams: " + teams);
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
        Log.d(getClass().getName(),"set players: " + players);
    }

    public void clearPlayers() {
        this.players.clear();
        Log.d(getClass().getName(), "cleared players");
    }

    public void setTeams(ArrayList<ArrayList<Player>> teams) {
        this.teams = teams;
        Log.d(getClass().getName(),"set teams: " + teams);
    }

    public void togglePlayer(String name) {
        for (Player p : players) {
            String pName = p.getName();
            if (pName.equals(name)) {
                p.toggleActive();
                Log.d(getClass().getName(), "Toggled Active " + pName);
                return;
            }
        }
    }

    public void toggleCaptain(String name) {
        for (Player p : players) {
            String pName = p.getName();
            if (pName.equals(name)) {
                p.toggleCaptain();
                Log.d(getClass().getName(), "Toggled Active " + pName);
                return;
            }
        }
    }

    private ArrayList<Player> getActivePlayers() {
        ArrayList<Player> ap = (ArrayList<Player>) players.clone();
        ap.removeIf(player -> !player.getActive());
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
        Log.d(getClass().getName(), "added " + player);
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
