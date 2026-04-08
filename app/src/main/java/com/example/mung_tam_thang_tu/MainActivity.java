package com.example.mung_tam_thang_tu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private RecyclerView rvMovies;
    private MovieAdapter adapter;
    private List<Movie> movieList;
    private ProgressBar progressBar;
    private FloatingActionButton fabAddMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return;
        }

        rvMovies = findViewById(R.id.rvMovies);
        progressBar = findViewById(R.id.progressBar);
        fabAddMovie = findViewById(R.id.fabAddMovie);
        
        movieList = new ArrayList<>();

        adapter = new MovieAdapter(movieList, movie -> {
            Intent intent = new Intent(MainActivity.this, BookingActivity.class);
            intent.putExtra("movieId", movie.getId());
            intent.putExtra("movieTitle", movie.getTitle());
            startActivity(intent);
        });

        rvMovies.setAdapter(adapter);

        fabAddMovie.setOnClickListener(v -> showAddMovieDialog());

        loadMoviesFromFirestore();
    }

    private void showAddMovieDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thêm phim mới");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText titleInput = new EditText(this);
        titleInput.setHint("Tên phim");
        layout.addView(titleInput);

        final EditText genreInput = new EditText(this);
        genreInput.setHint("Thể loại (Hành động, Tình cảm...)");
        layout.addView(genreInput);

        final EditText posterInput = new EditText(this);
        posterInput.setHint("Link ảnh Poster (URL)");
        layout.addView(posterInput);

        final EditText descInput = new EditText(this);
        descInput.setHint("Mô tả phim");
        layout.addView(descInput);

        builder.setView(layout);

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String title = titleInput.getText().toString().trim();
            String genre = genreInput.getText().toString().trim();
            String poster = posterInput.getText().toString().trim();
            String desc = descInput.getText().toString().trim();

            if (title.isEmpty() || genre.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập ít nhất Tên và Thể loại", Toast.LENGTH_SHORT).show();
                return;
            }

            Movie movie = new Movie();
            movie.setTitle(title);
            movie.setGenre(genre);
            movie.setPosterUrl(poster);
            movie.setDescription(desc);

            db.collection("movies")
                    .add(movie)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(MainActivity.this, "Thêm phim thành công!", Toast.LENGTH_SHORT).show();
                        loadMoviesFromFirestore(); // Tải lại danh sách
                    })
                    .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void loadMoviesFromFirestore() {
        progressBar.setVisibility(View.VISIBLE);
        db.collection("movies")
                .get()
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        movieList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Movie movie = document.toObject(Movie.class);
                            movie.setId(document.getId());
                            movieList.add(movie);
                        }
                        adapter.notifyDataSetChanged();
                        
                        if (movieList.isEmpty()) {
                            Toast.makeText(this, "Chưa có phim nào! Nhấn nút '+' để thêm.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(this, "Lỗi: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}