package ro.pub.cs.systems.eim.lab03.phonedialer.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import ro.pub.cs.systems.eim.lab03.phonedialer.R;
import ro.pub.cs.systems.eim.lab03.phonedialer.general.Constants;

public class PhoneDialerActivity extends AppCompatActivity {

    private EditText phoneNumberEditText; // variabila pentru spatiul unde apare nr de telefon
    private ImageButton callImageButton;  // variabila pentru imaginea de call
    private ImageButton hangupImageButton; // variabila pentru imaginea de hangup
    private ImageButton backspaceImageButton; // variabila pentru imaginea de stergere
    private Button genericButton; // variabila ce va reprezenta pe rand fiecare tasta

    // ascultator pentru butonul CALL de tip ImageButton
    // acesta va spune ce actiune facem daca dam click pe CALL
    private CallImageButtonClickListener callImageButtonClickListener = new CallImageButtonClickListener();
    private class CallImageButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            // daca nu avem permisiunea de a suna (setata in AndroidManifest.xml)
            if (ContextCompat.checkSelfPermission(PhoneDialerActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // cerem permisiuni de CALL
                ActivityCompat.requestPermissions(
                        PhoneDialerActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        Constants.PERMISSION_REQUEST_CALL_PHONE);
            // daca avem permisiunea de a efectua CALL
            } else {
                // generam o intentie pentru CALL
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + phoneNumberEditText.getText().toString()));
                // pornim activitatea de CALL
                startActivity(intent);
            }
        }
    }

    // ascultator pentru butonul HANGUP de tip ImageButton
    // acesta va spune ce actiune facem daca dam click pe HANGUP
    private HangupImageButtonClickListener hangupImageButtonClickListener = new HangupImageButtonClickListener();
    private class HangupImageButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            // daca dam click pe butonul de hangup oprim activitatea pe care am pornit-o mai sus,
            // adica cea de CALL
            finish();
        }
    }

    // ascultator pentru butonul BACKSPACE/STERGERE de tip ImageButton
    // acesta va spune ce actiune facem daca dam click pe BACKSPACE/STERGERE
    private BackspaceButtonClickListener backspaceButtonClickListener = new BackspaceButtonClickListener();
    private class BackspaceButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            // am aici numarul de telefon sub forma unui string
            String phoneNumber = phoneNumberEditText.getText().toString();
            // atata timp cat exista caractere in EditText => ele pot fi sterse
            if (phoneNumber.length() > 0) {
                // se sterge mereu ultima cifra / ultimul caracter
                phoneNumber = phoneNumber.substring(0, phoneNumber.length() - 1);
                phoneNumberEditText.setText(phoneNumber);
            }
        }
    }

    // ascultator pentru tastele 0,1,2...9, # si *
    private GenericButtonClickListener genericButtonClickListener = new GenericButtonClickListener();
    private class GenericButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            // in functie de ce este scris pe butonul respectiv, asta va fi printat in EditText
            phoneNumberEditText.setText(phoneNumberEditText.getText().toString() + ((Button)view).getText().toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_dialer);

        // setare orientare ca portret (vezi modificarile si in AndroidManifest.xml)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // declararea butoanelor si variabilelor folosite + apelul metodelor pentru fiecare buton

        // variabila pentru EditText
        phoneNumberEditText = (EditText)findViewById(R.id.phone_number_edit_text);

        // variabila pentru butonul de CALL + apelul metodei pentru CALL
        callImageButton = (ImageButton)findViewById(R.id.call_image_button);
        callImageButton.setOnClickListener(callImageButtonClickListener);

        // variabila pentru butonul de HANGUP + apelul metodei pentru HANGUP
        hangupImageButton = (ImageButton)findViewById(R.id.hangup_image_button);
        hangupImageButton.setOnClickListener(hangupImageButtonClickListener);

        // variabila pentru butonul de BACKSPACE/STERGERE + apelul metodei pentru BACKSPACE/STERGERE
        backspaceImageButton = (ImageButton)findViewById(R.id.backspace_image_button);
        backspaceImageButton.setOnClickListener(backspaceButtonClickListener);

        // variabila pentru tastele 0,1,2..9, # si * + apelul metodei pentru taste
        for (int index = 0; index < Constants.buttonIds.length; index++) {
            genericButton = (Button)findViewById(Constants.buttonIds[index]);
            genericButton.setOnClickListener(genericButtonClickListener);
        }
    }
}
