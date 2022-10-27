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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.io.IOException;

public class TagsFragment extends Fragment {
    private View view;
    private TextView tvScanStatus;
    private EditText etName;

    public TagsFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.tags_fragment, container, false);

        tvScanStatus = view.findViewById(R.id.tvScanStatus);
        etName = view.findViewById(R.id.etName);

        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String status;
                int len = etName.getText().length();
                if (len == 0) {
                    status = getResources().getString(R.string.scanNameRequired);
                } else {
                    status = getResources().getString(R.string.scanReady);
                }

                tvScanStatus.setText(status);
            }
        });

        return view;
    }

    public void writeTag(Tag tag) {
        String name = etName.getText().toString();

        if (name.length() == 0) {
            Toast.makeText(getActivity(), getResources().getString(R.string.scanNameRequired), Toast.LENGTH_SHORT).show();
            return;
        }

        Ndef ndef = Ndef.get(tag);
        NdefRecord ndefRecord = NdefRecord.createTextRecord("en", String.valueOf(etName.getText()));
        NdefMessage ndefMessage = new NdefMessage(ndefRecord);

        try {
            ndef.connect();
            ndef.writeNdefMessage(ndefMessage);

            // Success if got to here
            Toast.makeText(getActivity(), "Write to NFC Success", Toast.LENGTH_SHORT).show();
            etName.setText("");
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
