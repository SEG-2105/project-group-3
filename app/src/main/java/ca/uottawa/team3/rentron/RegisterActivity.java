package ca.uottawa.team3.rentron;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    int selectedRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // activate/populate role spinner:
        Spinner spinner = (Spinner)findViewById(R.id.roleSpinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.roles_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int spinnerPos, long id) {
        LinearLayout birthYearLayout = (LinearLayout)findViewById(R.id.birthYearLayout);
        LinearLayout addressLayout = (LinearLayout)findViewById(R.id.addressLayout);
        switch (spinnerPos) {
            case 0: // client
                selectedRole = 0;
                birthYearLayout.setVisibility(View.VISIBLE);
                addressLayout.setVisibility(View.GONE);
                break;
            case 1: // landlord
                selectedRole = 1;
                birthYearLayout.setVisibility(View.GONE);
                addressLayout.setVisibility(View.VISIBLE);
                break;
            case 2: // property manager
                selectedRole = 2;
                birthYearLayout.setVisibility(View.GONE);
                addressLayout.setVisibility(View.GONE);
                break;
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        findViewById(R.id.birthYearLayout).setVisibility(View.GONE);
        findViewById(R.id.addressLayout).setVisibility(View.GONE);
    }

    // BEFORE TESTING, TRY IMPLEMENTING PASSWORD HASHING....
    public void onClickAddUser() {
        UserCreator userCreator = new UserCreator();
        User newUser;

        EditText emailField = (EditText)findViewById(R.id.editTextUsername);
        EditText passwordField = (EditText)findViewById(R.id.editTextPassword);
        EditText firstNameField = (EditText)findViewById(R.id.editTextFirstName);
        EditText lastNameField = (EditText)findViewById(R.id.editTextLastName);
        EditText addressField = (EditText)findViewById(R.id.editTextAddress);
        EditText birthYearField = (EditText)findViewById(R.id.editTextBirthYear);

        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        String firstName = firstNameField.getText().toString();
        String lastName = lastNameField.getText().toString();
        String address = addressField.getText().toString();
        String birthYear = birthYearField.getText().toString();

        switch (selectedRole) {
            case 0: // client
                newUser = new Client(firstName, lastName, email, password, birthYear);
                break;
            case 1: // landlord
                newUser = new Landlord(firstName, lastName, email, password, address);
                break;
            case 2: // property manager
                newUser = new PropertyMgr(firstName, lastName, email, password);
                break;
            default:
                newUser = new Client("","","","", ""); // create invalid Client
        }
        if (userCreator.add(newUser)) { // if registration successful
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivityForResult (intent,0);
        }
    }

}