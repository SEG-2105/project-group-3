package ca.uottawa.team3.rentron;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ca.uottawa.team3.rentron.Users.Tickets.Ticket;

public class TicketsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tickets);

        Toolbar topBar = findViewById(R.id.topBar);
        setSupportActionBar(topBar);
        String address = getIntent().getStringExtra("address");
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
                String message = editTextMessage.getText().toString();
                int urgence = seekBarUrgence.getProgress() + 1;
                String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

                // Save the ticket information (e.g., to a database or a list)
                // For demonstration, we just show the information in a Toast
                // You can replace this part with your own implementation

                // below line is commented to pass compilation
                //Ticket ticket = new Ticket(idClient, idPropertyMgr, property, type, message, urgence);

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
}
