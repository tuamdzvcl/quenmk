package com.example.btlver2;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText edtmk, edttk;
    TextView txter;
    Button btndangnhap, btndangki;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControler();
    }

    private void addControler() {
        btndangnhap = findViewById(R.id.btndangnhap);
        txter = findViewById(R.id.txter);
        btndangki = findViewById(R.id.btndangki);
        edtmk = findViewById(R.id.edtmk);
        edttk = findViewById(R.id.edttk);

        btndangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dangNhap(view);
            }
        });

        btndangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dangKi(view);
            }
        });
    }

    public void dangNhap(View view) {
        String edittk = edttk.getText().toString();
        String editmk = edtmk.getText().toString();

        if (edittk.isEmpty() || editmk.isEmpty()) {
            Toast.makeText(MainActivity.this, "Không được để trống", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            db = SQLiteDatabase.openOrCreateDatabase("data/data/com.example.btlver2/QuanLyTaiKhoan", null);
            String query = "SELECT * FROM tblQLTK WHERE uesr = ? AND pass = ?";
            Cursor cursor = db.rawQuery(query, new String[]{edittk, editmk});

            if (cursor.moveToFirst()) {
                Toast.makeText(MainActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            } else {
                txter.setText("Tài khoản hoặc mật khẩu không đúng");
                clear();
            }

            cursor.close();
            db.close();
        } catch (SQLException ex) {
            txter.setText("Lỗi: " + ex.getMessage());
        }
    }

    private void clear() {
        edttk.setText("");
        edtmk.setText("");
    }

    public void dangKi(View view) {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setTitle("Đăng kí");
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dangki);

        EditText edtDangkiTk = dialog.findViewById(R.id.edtDangkiTk);
        EditText edtDangkiMk = dialog.findViewById(R.id.edtDangkiMk);
        Button btnDangkiSubmit = dialog.findViewById(R.id.btnquen);
        EditText editemali = dialog.findViewById(R.id.editemali);
        Button btnhuy = dialog.findViewById(R.id.btnhuy);
        btnhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        btnDangkiSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtDangkiTk.getText().toString().trim();
                String password = edtDangkiMk.getText().toString().trim();
                String email = editemali.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty() ||email.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Vui lòng không để trống!", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    db = SQLiteDatabase.openOrCreateDatabase("data/data/com.example.btlver2/QuanLyTaiKhoan", null);
                    db.execSQL("CREATE TABLE IF NOT EXISTS tblQLTK (uesr TEXT PRIMARY KEY, pass TEXT,email text)");

                    Cursor cursor = db.rawQuery("SELECT * FROM tblQLTK WHERE uesr = ?", new String[]{username});
                    if (cursor.moveToFirst()) {
                        Toast.makeText(MainActivity.this, "Tên đăng nhập đã tồn tại!", Toast.LENGTH_SHORT).show();
                        cursor.close();
                        return;
                    }
                    cursor.close();

                    ContentValues values = new ContentValues();
                    values.put("uesr", username);
                    values.put("pass", password);
                    values.put("email", email);

                    long result = db.insert("tblQLTK", null, values);
                    if (result == -1) {
                        Toast.makeText(MainActivity.this, "Đăng kí thất bại, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Đăng kí thành công!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                    db.close();
                } catch (SQLException ex) {
                    Toast.makeText(MainActivity.this, "Lỗi: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    public void laymk(View view) {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setTitle("Lấy Lại Mật Khẩu");
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.quenmk);

        EditText edttkquen = dialog.findViewById(R.id.edttkquen);
        EditText edteamilquen = dialog.findViewById(R.id.edtemailquen);
        Button btnquen = dialog.findViewById(R.id.btnquen);
        Button btnhuy = dialog.findViewById(R.id.btnhuy);
        TextView txtshow = dialog.findViewById(R.id.txtshow);
         btnhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        btnquen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edttkquen.getText().toString().trim();
                String email = edteamilquen.getText().toString().trim();

                if (username.isEmpty() ||email.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Vui lòng không để trống!", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    db = SQLiteDatabase.openOrCreateDatabase("data/data/com.example.btlver2/QuanLyTaiKhoan", null);
                    db.execSQL("CREATE TABLE IF NOT EXISTS tblQLTK (uesr TEXT PRIMARY KEY, pass TEXT, email TEXT)");

                    // Correctly query the database to check if the username and email exist
                    Cursor cursor = db.rawQuery("SELECT pass FROM tblQLTK WHERE uesr = ? AND email = ?", new String[]{username, email});
                    if (cursor.moveToFirst()) {
                        // Fetch the password from the result set
                        String password = cursor.getString(0);
                        txtshow.setText("Your password is: " + password);
                    } else {
                        Toast.makeText(MainActivity.this, "Tên đăng nhập hoặc email không đúng!", Toast.LENGTH_SHORT).show();
                    }
                    cursor.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Có lỗi xảy ra!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }
         }


