package com.richardphan.teamrandomizer;

import android.app.PendingIntent;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.TagLostException;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.io.IOException;

public class TagsFragment extends Fragment {
    private View view;
    private EditText etName;
    private Button btnWriteTag;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;

    public TagsFragment(NfcAdapter nfcAdapter) {
        super();
        this.nfcAdapter = nfcAdapter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.tags_fragment, container, false);

        etName = view.findViewById(R.id.etName);
        btnWriteTag = view.findViewById(R.id.btnWriteTag);

        return view;
    }


    public void writeTag(Tag tag) {
        Ndef ndef = Ndef.get(tag);
        NdefRecord ndefRecord = NdefRecord.createTextRecord("en", String.valueOf(etName.getText()));
        NdefMessage ndefMessage = new NdefMessage(ndefRecord);

        try {
            ndef.connect();
            ndef.writeNdefMessage(ndefMessage);

            // Success if got to here
            Toast.makeText(getActivity(), "Write to NFC Success", Toast.LENGTH_SHORT).show();
        } catch (FormatException e) {
            // if the NDEF Message to write is malformed
            System.out.println(e);
        } catch (TagLostException e) {
            // Tag went out of range before operations were complete
            System.out.println(e);
        } catch (IOException e){
            // if there is an I/O failure, or the operation is cancelled
            System.out.println(e);
        } finally {
            // Be nice and try and close the tag to
            // Disable I/O operations to the tag from this TagTechnology object, and release resources.
            try {
                ndef.close();
            } catch (IOException e) {
                // if there is an I/O failure, or the operation is cancelled
            }
        }
    }
}
