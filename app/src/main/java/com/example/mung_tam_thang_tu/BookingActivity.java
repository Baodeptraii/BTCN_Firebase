package com.example.mung_tam_thang_tu;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class BookingActivity extends AppCompatActivity {

    private TextView txtMovieTitle;
    private EditText edtSeat;
    private Spinner spnShowtime;
    private Button btnBook;
    private FirebaseFirestore db;
    private String movieId, movieTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        db = FirebaseFirestore.getInstance();
        
        movieId = getIntent().getStringExtra("movieId");
        movieTitle = getIntent().getStringExtra("movieTitle");

        txtMovieTitle = findViewById(R.id.txtMovieTitle);
        edtSeat = findViewById(R.id.edtSeat);
        spnShowtime = findViewById(R.id.spnShowtime);
        btnBook = findViewById(R.id.btnBook);

        txtMovieTitle.setText(movieTitle);

        // Giả lập danh sách suất chiếu (Showtimes)
        List<String> showtimes = new ArrayList<>();
        showtimes.add("18:00 - 20:00");
        showtimes.add("20:30 - 22:30");
        showtimes.add("23:00 - 01:00");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, showtimes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnShowtime.setAdapter(adapter);

        btnBook.setOnClickListener(v -> saveTicketToFirebase());
    }

    private void saveTicketToFirebase() {
        String seat = edtSeat.getText().toString().trim();
        String showtime = spnShowtime.getSelectedItem().toString();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (seat.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn ghế!", Toast.LENGTH_SHORT).show();
            return;
        }

        Ticket ticket = new Ticket(movieTitle, userId, seat, showtime, System.currentTimeMillis());

        db.collection("tickets")
                .add(ticket)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Đặt vé thành công!", Toast.LENGTH_SHORT).show();
                    finish(); // Quay lại màn hình chính
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}