package com.richardphan.teamrandomizer;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, OnItemClick {
    private BottomNavigationView bottomNavigationView;
    private TeamsFragment teamsFragment;
    private PlayersFragment playersFragment;
    private TagsFragment tagsFragment;
    private SettingsFragment settingsFragment;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private int tagAction;
    private TeamRandomizer tr;

    private final int NFC_READ = 0;
    private final int NFC_WRITE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this, "This device does not support NFC.", Toast.LENGTH_LONG).show();
            finish();
        }

        tr = new TeamRandomizer();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        teamsFragment = new TeamsFragment();
        playersFragment = new PlayersFragment();
        tagsFragment = new TagsFragment(nfcAdapter);
        settingsFragment = new SettingsFragment();

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.menuPlayers);

        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
            case R.id.menuSettings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, settingsFragment).commit();
                tagAction = NFC_READ;
                return true;
        }

        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        String action = intent.getAction();

        if (action.equals(NfcAdapter.ACTION_TAG_DISCOVERED)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Tag tag;
            switch (tagAction) {
                case NFC_READ:
                    tag = readNfc(intent);
                    NdefRecord record = Ndef.get(tag).getCachedNdefMessage().getRecords()[0];
                    byte[] payload = record.getPayload ();
                    byte[] textArray = Arrays.copyOfRange(payload, (int) payload[0] + 1 , payload.length);
                    String name = new String(textArray);
                    tr.addPlayer(name);
                    playersFragment.notifyDataSetChanged();
                    break;
                case NFC_WRITE:
                    tag = readNfc(intent);
                    tagsFragment.writeTag(tag);
            }

        }
    }

    private Tag readNfc(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        return tag;
    }

    public TeamRandomizer getTeamRandomizer() {
        return tr;
    }

    @Override
    public void onClick(String value) {

    }
}
