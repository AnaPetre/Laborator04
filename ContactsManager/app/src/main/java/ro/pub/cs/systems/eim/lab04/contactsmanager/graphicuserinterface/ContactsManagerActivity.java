package ro.pub.cs.systems.eim.lab04.contactsmanager.graphicuserinterface;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import ro.pub.cs.systems.eim.lab04.contactsmanager.R;
import ro.pub.cs.systems.eim.lab04.contactsmanager.general.Constants;

public class ContactsManagerActivity extends AppCompatActivity {

    private EditText nameEditText; // buton pentru nume
    private EditText phoneEditText; // buton pentru numarul de telefon
    private EditText emailEditText; // buton pentru email
    private EditText addressEditText; // buton pentru adresa
    private EditText jobTitleEditText; // buton pentru job
    private EditText companyEditText; // buton pentru companie
    private EditText websiteEditText; // buton pentru website
    private EditText imEditText; // buton pentru IM

    private Button showHideAdditionalFieldsButton; // buton pentru afisarea info ascunse
    private Button saveButton; // buton de SAVE
    private Button cancelButton; // buton de CANCEL
    private LinearLayout additionalFieldsContainer;

    // ascultator care realizeaza o actiune in functie de butonul care este apasat
    private ButtonClickListener buttonClickListener = new ButtonClickListener();
    private class ButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                // butoanele de VISIBLE / INVISIBLE: setarea vizibilitatii informatiilor aditionale
                case R.id.show_hide_additional_fields:
                    switch (additionalFieldsContainer.getVisibility()) {

                        // daca info sunt vizibile si vrem sa le ascundem / sa le facem invizibile
                        case View.VISIBLE:
                            showHideAdditionalFieldsButton.setText(getResources().getString(R.string.show_additional_fields));
                            additionalFieldsContainer.setVisibility(View.INVISIBLE);
                            break;
                        // daca info sunt invizibile si dorim sa le vedem / sa le facem vizibile
                        case View.INVISIBLE:
                            showHideAdditionalFieldsButton.setText(getResources().getString(R.string.hide_additional_fields));
                            additionalFieldsContainer.setVisibility(View.VISIBLE);
                            break;
                    }
                    break;

                // butonul de SAVE
                case R.id.save_button:
                    // avem urmatoarele info pe care le putem salva intr-un contact nou
                    String name = nameEditText.getText().toString();
                    String phone = phoneEditText.getText().toString();
                    String email = emailEditText.getText().toString();
                    String address = addressEditText.getText().toString();
                    String jobTitle = jobTitleEditText.getText().toString();
                    String company = companyEditText.getText().toString();
                    String website = websiteEditText.getText().toString();
                    String im = imEditText.getText().toString();

                    // avem o noua intentie de a insera date pentru noul contact
                    Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                    intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

                    // daca a fost introdus deja un nume => inseram numele in contact
                    if (name != null) {
                        intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
                    }

                    // daca a fost introdus deja un numar de telefon => inseram numarul in contact
                    if (phone != null) {
                        intent.putExtra(ContactsContract.Intents.Insert.PHONE, phone);
                    }

                    // daca a fost introdus deja un email => inseram email-ul in contact
                    if (email != null) {
                        intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);
                    }

                    // daca a fost introdus deja o adresa => inseram adresa in contact
                    if (address != null) {
                        intent.putExtra(ContactsContract.Intents.Insert.POSTAL, address);
                    }

                    // daca a fost introdus deja un job => inseram job-ul in contact
                    if (jobTitle != null) {
                        intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, jobTitle);
                    }

                    // daca a fost introdus deja o companie => inseram compania in contact
                    if (company != null) {
                        intent.putExtra(ContactsContract.Intents.Insert.COMPANY, company);
                    }

                    // daca a fost introdus deja un website => inseram website-ul in contact
                    // are forma de array pentru are mai multe campuri in URL
                    ArrayList<ContentValues> contactData = new ArrayList<>();
                    if (website != null) {
                        ContentValues websiteRow = new ContentValues();
                        websiteRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE);
                        websiteRow.put(ContactsContract.CommonDataKinds.Website.URL, website);
                        contactData.add(websiteRow);
                    }

                    // daca a fost introdus deja un IM => inseram IM in contact
                    if (im != null) {
                        ContentValues imRow = new ContentValues();
                        imRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE);
                        imRow.put(ContactsContract.CommonDataKinds.Im.DATA, im);
                        contactData.add(imRow);
                    }
                    intent.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, contactData);
                    /* pornesc activitarea de salvare a datelor / SAVE folosind un cod de cerere
                    prin intermediul caruia se va verifica rezultatul furnizat*/
                    startActivityForResult(intent, Constants.CONTACTS_MANAGER_REQUEST_CODE);
                    break;

                // butonul CANCEL
                case R.id.cancel_button:
                    // trimit inapoi rezultatul
                    setResult(Activity.RESULT_CANCELED, new Intent());
                    // opresc orice activitare de salvare pornita anterior
                    finish();
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_manager);

        nameEditText = (EditText)findViewById(R.id.name_edit_text);

        phoneEditText = (EditText)findViewById(R.id.phone_number_edit_text);

        emailEditText = (EditText)findViewById(R.id.email_edit_text);

        addressEditText = (EditText)findViewById(R.id.address_edit_text);

        jobTitleEditText = (EditText)findViewById(R.id.job_title_edit_text);

        companyEditText = (EditText)findViewById(R.id.company_edit_text);

        websiteEditText = (EditText)findViewById(R.id.website_edit_text);

        imEditText = (EditText)findViewById(R.id.im_edit_text);

        showHideAdditionalFieldsButton = (Button)findViewById(R.id.show_hide_additional_fields);
        showHideAdditionalFieldsButton.setOnClickListener(buttonClickListener);

        saveButton = (Button)findViewById(R.id.save_button);
        saveButton.setOnClickListener(buttonClickListener);

        cancelButton = (Button)findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(buttonClickListener);

        additionalFieldsContainer = (LinearLayout)findViewById(R.id.additional_fields_container);

        Intent intent = getIntent();
        //daca intentia este pornita si nu este nula
        if (intent != null) {
            /* preluam info din sectiunea extra, identificata prin cheia ro.pub.cs.systems.eim.lab04.contactsmanager.PHONE_NUMBER_KEY,
             iar continutul ei este plasat in campul textului corespunzator*/
            String phone = intent.getStringExtra("ro.pub.cs.systems.eim.lab04.contactsmanager.PHONE_NUMBER_KEY");
            if (phone != null) {
                phoneEditText.setText(phone);
            } else {
                Toast.makeText(this, getResources().getString(R.string.phone_error), Toast.LENGTH_LONG).show();
            }
        }
    }

    /*cand parasim aplicatia nativa pentru gestiunea agendei telefonice, verificam codul de cerere
     si se transmite inapoi un rezultat => adica putem vizualiza de exemplu, noul contact adaugat*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch(requestCode) {
            case Constants.CONTACTS_MANAGER_REQUEST_CODE:
                setResult(resultCode, new Intent());
                finish();
                break;
        }
    }
}
