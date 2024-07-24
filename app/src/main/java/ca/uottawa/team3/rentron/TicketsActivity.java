package ca.uottawa.team3.rentron;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ca.uottawa.team3.rentron.Users.Tickets.Courier;
import ca.uottawa.team3.rentron.Users.Tickets.Ticket;

public class TicketsActivity extends AppCompatActivity {

    private SharedPreferences pref;
    private String role, email, address,manager,client;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        db = FirebaseFirestore.getInstance();
        pref = getSharedPreferences("activeUser", Context.MODE_PRIVATE);
        byte[] active = Base64.decode(pref.getString("active", ""), Base64.DEFAULT);
        byte[] active1 = Base64.decode(pref.getString("activeRole", ""), Base64.DEFAULT);;

        String activeEmail = "";
        String activeRole = "";
        try {
            activeEmail = new String(active, "UTF-8");
            activeRole = new String(active1, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        role = activeRole;
        email = activeEmail;

        setContentView(R.layout.activity_tickets);

        Toolbar topBar = findViewById(R.id.topBar);
        setSupportActionBar(topBar);
        address = getIntent().getStringExtra("address");
        if (role.equals("client")) {
            manager = getIntent().getStringExtra("manager");
        }
        getSupportActionBar().setTitle(address);

        topBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                startActivityForResult(intent, 0);
                finish();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button buttonOpenDialog = findViewById(R.id.btnCreateTicket);
        buttonOpenDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTicketDialog();
            }
        });

        // TODO: The ticket_dialog_choose_action dialog does not yet show up, but functionality is almost done
    }

    private void openTicketDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                                    .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
        View dialogView = getLayoutInflater().inflate(R.layout.ticket_dialog_create_ticket, null);
        builder.setView(dialogView);

        Spinner spinnerType = dialogView.findViewById(R.id.spinner_type);
        EditText editTextName = dialogView.findViewById(R.id.edittext_name);
        EditText editTextMessage = dialogView.findViewById(R.id.edittext_message);
        SeekBar seekBarUrgence = dialogView.findViewById(R.id.seekbar_urgence);
        TextView textViewUrgenceValue = dialogView.findViewById(R.id.textview_urgence_value);
        Button buttonSubmit = dialogView.findViewById(R.id.button_submit);

        seekBarUrgence.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewUrgenceValue.setText(String.valueOf(progress + 1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        AlertDialog dialog = builder.create();

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = spinnerType.getSelectedItem().toString();
                String name = editTextName.getText().toString();
                String message = editTextMessage.getText().toString();

                int urgence = seekBarUrgence.getProgress() + 1;
                String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                message = dateTime + "\n" + message;

                // Save the ticket information (e.g., to a database or a list)
                // For demonstration, we just show the information in a Toast
                // You can replace this part with your own implementation

                // below line is commented to pass compilation
                Ticket ticket = new Ticket(email, manager, address, type, message, urgence, name);
                Courier courier = new Courier(getApplicationContext(), db);
                courier.sendMessage(ticket);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.ticket_dialog_choose_action, null);
        builder.setView(dialogView);

        // Get the dialog elements
        Button acceptButton = dialogView.findViewById(R.id.button_accept);
        Button declineButton = dialogView.findViewById(R.id.button_decline);
        Button sendButton = dialogView.findViewById(R.id.button_send);
        final EditText inputMessage = dialogView.findViewById(R.id.input_message);
        final Button btnSend = dialogView.findViewById(R.id.button_send);

        final AlertDialog dialog = builder.create();
        dialog.show();

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                // The property manager chose to accept the ticket
            }
        });

        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputMessage.setVisibility(View.VISIBLE);
                btnSend.setVisibility(View.VISIBLE);
                acceptButton.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputMessage.getVisibility() == View.VISIBLE && !inputMessage.getText().toString().isEmpty()) {
                    String message = inputMessage.getText().toString();
                    dialog.dismiss();
                    // The property manager chose to accept the ticket
                } else {
                    Toast.makeText(TicketsActivity.this, "Please enter a message", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void ticketRatingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.ticket_dialog_select_rating, null);
        builder.setView(dialogView);

        final RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);
        final EditText editTextMessage = dialogView.findViewById(R.id.editTextMessage);
        Button buttonSubmit = dialogView.findViewById(R.id.buttonSubmit);

        final AlertDialog dialog = builder.create();
        dialog.show();

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float rating = ratingBar.getRating();
                String message = editTextMessage.getText().toString();

                Toast.makeText(TicketsActivity.this, "Rating: " + rating + "\nMessage: " + message, Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
