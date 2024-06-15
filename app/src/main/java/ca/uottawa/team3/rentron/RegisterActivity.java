package ca.uottawa.team3.rentron;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import javax.crypto.SecretKey;

import ca.uottawa.team3.rentron.Users.Client;
import ca.uottawa.team3.rentron.Users.Hashing;
import ca.uottawa.team3.rentron.Users.Landlord;
import ca.uottawa.team3.rentron.Users.PropertyMgr;
import ca.uottawa.team3.rentron.Users.User;
import ca.uottawa.team3.rentron.Users.UserCreator;

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

    public void onClickAddUser(View view) {
        UserCreator userCreator = new UserCreator(getApplicationContext());
        User newUser;

        EditText emailField = (EditText)findViewById(R.id.editTextUsername);
        EditText passwordField = (EditText)findViewById(R.id.editTextPassword);
        EditText confirmPasswordField = (EditText)findViewById(R.id.editTextConfirmPassword);
        EditText firstNameField = (EditText)findViewById(R.id.editTextFirstName);
        EditText lastNameField = (EditText)findViewById(R.id.editTextLastName);
        EditText addressField = (EditText)findViewById(R.id.editTextAddress);
        EditText birthYearField = (EditText)findViewById(R.id.editTextBirthYear);

        String email = emailField.getText().toString();
        String passwordPlain = passwordField.getText().toString();
        String confirmPassword = confirmPasswordField.getText().toString();
        String firstName = firstNameField.getText().toString();
        String lastName = lastNameField.getText().toString();
        String address = addressField.getText().toString();
        String birthYear = birthYearField.getText().toString();
        if (birthYear.equals("")) {
            birthYear = "0";
        }

        email = email.toLowerCase();

        // basic field checking
        if (!passwordPlain.equals(confirmPassword)) {
            Toast.makeText(getApplicationContext(), "Passwords do not match.", Toast.LENGTH_LONG).show();
            return;
        }
        if ((selectedRole == 0) && (Integer.valueOf(birthYear) < 1900 || Integer.valueOf(birthYear) >= 2024)) {
            Toast.makeText(getApplicationContext(), "Birth year is invalid.", Toast.LENGTH_LONG).show();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getApplicationContext(), "Email is invalid.", Toast.LENGTH_LONG).show();
            return;
        }

        // password hash
        Hashing hasher = new Hashing(getApplicationContext());
        byte[] salt = hasher.generateSalt();
        SecretKey password = hasher.hashPassword(passwordPlain.toCharArray(), salt);

        switch (selectedRole) {
            case 0: // client
                newUser = new Client(firstName, lastName, email, password, salt, birthYear);
                break;
            case 1: // landlord
                newUser = new Landlord(firstName, lastName, email, password, salt, address);
                break;
            case 2: // property manager
                newUser = new PropertyMgr(firstName, lastName, email, password, salt);
                break;
            default:
                // create invalid Client as "default" user, we should never be in this state though
                newUser = new Client("","","",null, null, "");
        }

        if (userCreator.add(newUser)) { // if registration successful
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivityForResult (intent,0);
        }
    }

}