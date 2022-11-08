package com.richardphan.teamrandomizer;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, BadgeUpdateListener {
    public int currMenu;
    private BottomNavigationView bottomNavigationView;
    private BadgeDrawable badgeCount;
    private TeamsFragment teamsFragment;
    private PlayersFragment playersFragment;
    private TagsFragment tagsFragment;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private int tagAction;
    private TeamRandomizer tr;

    public final int NFC_IDLE = 0;
    public final int NFC_READ = 1;
    public final int NFC_WRITE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        tr = new TeamRandomizer();
        tr.setPlayers(getPlayersJson());
        tr.setTeams(getTeamsJson());

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        teamsFragment = new TeamsFragment();
        playersFragment = new PlayersFragment(this);
        tagsFragment = new TagsFragment();

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.menuPlayers);
        badgeCount = bottomNavigationView.getOrCreateBadge(R.id.menuPlayers);
        badgeCount.setNumber(tr.getPlayers().size());

        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        currMenu = item.getItemId();

        switch (item.getItemId()) {
            case R.id.menuPlayers:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, playersFragment).commit();
                tagAction = NFC_READ;
                return true;
            case R.id.menuTeams:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, teamsFragment).commit();
                tagAction = NFC_READ;
                return true;
            case R.id.menuTags:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, tagsFragment).commit();
                tagAction = NFC_WRITE;
                return true;
        }

        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }

        saveData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        String action = intent.getAction();

        if (action.equals(NfcAdapter.ACTION_TAG_DISCOVERED)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Tag tag = readNfc(intent);

            switch (tagAction) {
                case NFC_IDLE:
                    break;
                case NFC_READ:
                    NdefRecord record = Ndef.get(tag).getCachedNdefMessage().getRecords()[0];
                    byte[] payload = record.getPayload ();
                    byte[] textArray = Arrays.copyOfRange(payload, (int) payload[0] + 1 , payload.length);
                    String name = new String(textArray);
                    tr.addPlayer(new Player(name));
                    playersFragment.notifyDataSetChanged();
                    break;
                case NFC_WRITE:
                    tagsFragment.writeTag(tag);
                    break;
            }

        }
    }

    private Tag readNfc(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        return tag;
    }

    private void saveData() {
        ArrayList<Player> players = tr.getPlayers();
        writeSaveFile(players, "players.json");

        ArrayList<ArrayList<Player>> teams = tr.getTeams();
        writeSaveFile(teams, "teams.json");
    }

    private void writeSaveFile(Object data, String filename) {
        File dir = getFilesDir();
        try {
            FileWriter fw = new FileWriter(dir + "/" + filename);
            fw.write(new Gson().toJson(data));
            fw.close();
            System.out.println("Wrote data to " + filename);
        } catch (IOException e) {
            System.out.println("An error occurred");
            e.printStackTrace();
        }
    }

    private ArrayList<Player> getPlayersJson() {
        JsonParser parser = new JsonParser();
        ArrayList<Player> players = new ArrayList<>();
        try {
            JsonArray aPlayers = (JsonArray) parser.parse(new FileReader(getFilesDir() + "/players.json"));

            for (Object obj : aPlayers) {
                JsonObject oPlayer = (JsonObject) obj;
                String name = oPlayer.get("name").getAsString();
                boolean active = oPlayer.get("active").getAsBoolean();
                boolean captain = oPlayer.get("captain").getAsBoolean();
                players.add(new Player(name, active, captain));
            }
        } catch (FileNotFoundException e) {
            System.out.println("players.json not found. Ignoring");
        }

        return players;
    }

    private ArrayList<ArrayList<Player>> getTeamsJson() {
        JsonParser parser = new JsonParser();
        ArrayList<ArrayList<Player>> teams = new ArrayList<>();
        try {
            JsonArray aTeams = (JsonArray) parser.parse(new FileReader(getFilesDir() + "/teams.json"));

            for (Object oTeam : aTeams) {
                ArrayList<Player> players = new ArrayList<>();
                JsonArray aPlayers = (JsonArray) oTeam;
                for (Object oPlayer : aPlayers) {
                    JsonObject player = (JsonObject) oPlayer;
                    String name = player.get("name").getAsString();
                    boolean active = player.get("active").getAsBoolean();
                    boolean captain = player.get("captain").getAsBoolean();

                    players.add(new Player(name, active, captain));
                }
                teams.add(players);
            }
        } catch (FileNotFoundException e) {
            System.out.println("players.json not found. Ignoring");
        }

        return teams;
    }

    public TeamRandomizer getTeamRandomizer() {
        return tr;
    }


    @Override
    public void increment() {
        badgeCount.setNumber(badgeCount.getNumber() + 1);
    }

    @Override
    public void decrement() {
        badgeCount.setNumber(badgeCount.getNumber() - 1);
    }

    @Override
    public void set(int value) {
        badgeCount.setNumber(value);
    }
}
