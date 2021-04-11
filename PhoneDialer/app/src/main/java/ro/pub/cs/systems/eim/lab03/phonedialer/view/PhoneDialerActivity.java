package ro.pub.cs.systems.eim.lab03.phonedialer.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import ro.pub.cs.systems.eim.lab03.phonedialer.R;
import ro.pub.cs.systems.eim.lab03.phonedialer.general.Constants;

public class PhoneDialerActivity extends AppCompatActivity {

    private EditText phoneNumberEditText;
    private ImageButton callImageButton;
    private ImageButton hangupImageButton;
    private ImageButton backspaceImageButton;
    private ImageButton contactsImageButton;
    private Button genericButton;

    private CallImageButtonClickListener callImageButtonClickListener = new CallImageButtonClickListener();
    private class CallImageButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (ContextCompat.checkSelfPermission(PhoneDialerActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        PhoneDialerActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        Constants.PERMISSION_REQUEST_CALL_PHONE);
            } else {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + phoneNumberEditText.getText().toString()));
                startActivity(intent);
            }
        }
    }

    private HangupImageButtonClickListener hangupImageButtonClickListener = new HangupImageButtonClickListener();
    private class HangupImageButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            finish();
        }
    }

    private BackspaceButtonClickListener backspaceButtonClickListener = new BackspaceButtonClickListener();
    private class BackspaceButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String phoneNumber = phoneNumberEditText.getText().toString();
            if (phoneNumber.length() > 0) {
                phoneNumber = phoneNumber.substring(0, phoneNumber.length() - 1);
                phoneNumberEditText.setText(phoneNumber);
            }
        }
    }

    // metoda ce este apelata cand apasam / click pe ImageButton-ul de Contacte
    // ne redirectioneaza catre ContactsManagerActivity (celalalt proiect)
    private ContactsImageButtonClickListener contactsImageButtonClickListener = new ContactsImageButtonClickListener();
    private class ContactsImageButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            // la inceput introducem un numar de telefon vizibil in EditText
            String phoneNumber = phoneNumberEditText.getText().toString();
            if (phoneNumber.length() > 0) {
                // se creaza o intentie care se leaga de activitatea Contacts Manager
                Intent intent = new Intent("ro.pub.cs.systems.eim.lab04.contactsmanager.intent.action.ContactsManagerActivity");
                intent.putExtra("ro.pub.cs.systems.eim.lab04.contactsmanager.PHONE_NUMBER_KEY", phoneNumber);
                startActivityForResult(intent, Constants.CONTACTS_MANAGER_REQUEST_CODE);
            } else {
                Toast.makeText(getApplication(), getResources().getString(R.string.phone_error), Toast.LENGTH_LONG).show();
            }
        }
    }

    private GenericButtonClickListener genericButtonClickListener = new GenericButtonClickListener();
    private class GenericButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            phoneNumberEditText.setText(phoneNumberEditText.getText().toString() + ((Button)view).getText().toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_dialer);
        //setare orientare ca portret
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        phoneNumberEditText = (EditText)findViewById(R.id.phone_number_edit_text);

        callImageButton = (ImageButton)findViewById(R.id.call_image_button);
        callImageButton.setOnClickListener(callImageButtonClickListener);

        hangupImageButton = (ImageButton)findViewById(R.id.hangup_image_button);
        hangupImageButton.setOnClickListener(hangupImageButtonClickListener);

        backspaceImageButton = (ImageButton)findViewById(R.id.backspace_image_button);
        backspaceImageButton.setOnClickListener(backspaceButtonClickListener);

        for (int index = 0; index < Constants.buttonIds.length; index++) {
            genericButton = (Button)findViewById(Constants.buttonIds[index]);
            genericButton.setOnClickListener(genericButtonClickListener);
        }

        // declarare variabile pentru butonul de contacte + apelul metodei de adaugare a contactelor
        contactsImageButton = (ImageButton)findViewById(R.id.contacts_image_button);
        contactsImageButton.setOnClickListener(contactsImageButtonClickListener);
    }

    /*metoda transmite numarul de telefon in campul extra asociat Contacts Manager,
     identificabil prin intermediul unei chei*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        String phoneNumber = phoneNumberEditText.getText().toString();
        if (phoneNumber.length() > 0) {
            intent = new Intent("ro.pub.cs.systems.eim.lab04.contactsmanager.intent.action.ContactsManagerActivity");
            intent.putExtra("ro.pub.cs.systems.eim.lab04.contactsmanager.PHONE_NUMBER_KEY", phoneNumber);
            startActivityForResult(intent, Constants.CONTACTS_MANAGER_REQUEST_CODE);
        } else {
            //mesajul pentru phone_error a fost declarat in res/values/strings.xml
            Toast.makeText(getApplication(), getResources().getString(R.string.phone_error), Toast.LENGTH_LONG).show();
        }
    }
}
