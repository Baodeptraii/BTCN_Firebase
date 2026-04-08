package com.example.mung_tam_thang_tu;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private Button btnLogin;
    private TextView txtRegister;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtRegister = findViewById(R.id.txtRegister);

        btnLogin.setOnClickListener(v -> loginUser());

        txtRegister.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng đăng ký sẽ được cập nhật!", Toast.LENGTH_SHORT).show();
        });
    }

    private void loginUser() {
        String emailInput = edtEmail.getText().toString().trim();
        String passwordInput = edtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(emailInput)) {
            edtEmail.setError("Email/Username không được để trống");
            return;
        }

        if (TextUtils.isEmpty(passwordInput)) {
            edtPassword.setError("Mật khẩu không được để trống");
            return;
        }

        String email = emailInput;
        String password = passwordInput;
        
        // Hỗ trợ gõ tắt
        if (emailInput.equalsIgnoreCase("admin")) {
            email = "admin@movieapp.com";
        }
        
        // Quan trọng: Trên Firebase mật khẩu tối thiểu 6 ký tự. 
        // Nếu bạn tạo user trên Firebase với pass là "123456", hãy sửa dòng dưới thành "123456"
        if (passwordInput.equals("123")) {
            password = "password123"; 
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        String error = task.getException() != null ? task.getException().getMessage() : "Sai thông tin";
                        Toast.makeText(LoginActivity.this, "Lỗi: " + error, Toast.LENGTH_LONG).show();
                    }
                });
    }
}