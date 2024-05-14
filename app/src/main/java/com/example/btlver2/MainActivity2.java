package com.example.btlver2;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {

    TextView txttest;
    EditText editmasv, edittensv, editquequan;
    Button btninsert, btnupdate, btndelete, btnsearch;
    ListView lv;
    SQLiteDatabase db;
    ArrayList<String> SV = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        addControler();
        db = openOrCreateDatabase("QuanlySinhVien", MODE_PRIVATE, null);

        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='tblQLSV'", null);
        if (cursor.getCount() == 0) {
            db.execSQL("CREATE TABLE tblQLSV(masv primary key ,name text,quequan text)");
        }
        cursor.close();

        db = SQLiteDatabase.openDatabase("data/data/com.example.btlver2/QuanlySinhVien", null, SQLiteDatabase.CREATE_IF_NECESSARY);
        //db.execSQL("CREATE TABLE tblQLSV(masv primary key ,name text,quequan text)");
        loadDanhSach();
        try {
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String selectedItem = SV.get(position);


                    String[] parts = selectedItem.split("-");

                    // Thiết lập giá trị cho từng EditText tương ứng
                    editmasv.setText(parts[0]); // Giá trị masv
                    edittensv.setText(parts[1]); // Giá trị tensv
                    editquequan.setText(parts[2]); // Giá trị quequan
                }
            });

        }catch (SQLException ex){
            txttest.setText("lỗi" + ex);
        }
    }

    private void addControler() {
        txttest = findViewById(R.id.txtTest);
        editmasv = findViewById(R.id.edtmasv);
        edittensv = findViewById(R.id.edttensv);
        editquequan = findViewById(R.id.edtquequan);
        btninsert = findViewById(R.id.btninsert);
        btndelete = findViewById(R.id.btndelete);
        btnupdate = findViewById(R.id.btnUpdate);
        btnsearch = findViewById(R.id.btnsearch);
        lv = findViewById(R.id.lv);
    }

    private void loadDanhSach() {
        SV.clear();
        db = SQLiteDatabase.openDatabase("data/data/com.example.btlver2/QuanlySinhVien", null, SQLiteDatabase.CREATE_IF_NECESSARY);
        Cursor c1 = db.rawQuery("SELECT * FROM tblQLSV ", null);
        if (c1.moveToFirst()) {
            do {
                String masv = c1.getString(0);
                String tensv = c1.getString(1);
                String quequan = c1.getString(2);
                SV.add(masv + "-" + tensv + "-" + quequan);
            } while (c1.moveToNext());
        }
        c1.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity2.this, android.R.layout.simple_list_item_1, SV);
        lv.setAdapter(adapter);
        db.close();
    }

    public void insert(View view) {
        String masv = editmasv.getText().toString();
        String tensv = edittensv.getText().toString();
        String quequan = editquequan.getText().toString();
        String msg = "";
        String msv = new String(masv);
        String tsv = new String(tensv);
        String qq = new String(quequan);
        db = SQLiteDatabase.openDatabase("data/data/com.example.btlver2/QuanlySinhVien",null,SQLiteDatabase.CREATE_IF_NECESSARY);
        if (msv.equals("") || tsv.equals("") || qq.equals("")) {
            msg = "Hãy nhập đầy đủ thông tin ";

        } else {
            try {

                // db = SQLiteDatabase.openDatabase("data/data/data/data/com.example.btlandroid/QuanlySinhVien",null,SQLiteDatabase.CREATE_IF_NECESSARY);
                db.execSQL("INSERT INTO tblQLSV(masv,name,quequan)values('" + msv + "','" + tsv + "','" + qq + "');");
                msg = "Thêm thành công  "+ msv + "-" + tsv + "-" + qq;
                clear();
                Cursor c1 = db.rawQuery("select * from tblQLSV", null);
                int thetoal = c1.getCount();
                txttest.setText("so ban ghi la " + thetoal);
                loadDanhSach();
                db.close();

            }catch (SQLException ex)
            {
                txttest.setText("lõi" + ex);
            }

        }

        Toast.makeText(MainActivity2.this, msg, Toast.LENGTH_SHORT).show();
    }

    private void clear() {
        editmasv.setText("");
        edittensv.setText("");
        editquequan.setText("");
    }



    public void update(View view) {
        db = SQLiteDatabase.openDatabase("data/data/com.example.btlver2/QuanlySinhVien", null, SQLiteDatabase.CREATE_IF_NECESSARY);

        String masv = editmasv.getText().toString();
        String tensv = edittensv.getText().toString();
        String quequan = editquequan.getText().toString();
        ContentValues values = new ContentValues();
        values.put("masv", masv);
        values.put("name", tensv);
        values.put("quequan", quequan);
        boolean check = db.rawQuery("select *from tblQLSV where masv ='" + masv + "'", null).moveToFirst();
        if (check) {
            try {
                db.update("tblQLSV", values, "masv=?", new String[]{masv});
                Toast.makeText(MainActivity2.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                loadDanhSach();
                clear();
            } catch (SQLException ex) {
                txttest.setText("lỗi" + ex);
            }
            db.close();
        } else {
            Toast.makeText(MainActivity2.this, "vui long dien du thong tin ", Toast.LENGTH_SHORT).show();
        }
    }
    public void delete(View view) {
        db = SQLiteDatabase.openDatabase("data/data/com.example.btlver2/QuanlySinhVien", null, SQLiteDatabase.CREATE_IF_NECESSARY);

        String masv = editmasv.getText().toString();
        String tensv = edittensv.getText().toString();
        String quequan = editquequan.getText().toString();

        boolean check = db.rawQuery("select *from tblQLSV where masv ='" + masv + "'", null).moveToFirst();
        if (check) {
            try {
                db.delete("tblQLSV", "masv=?", new String[]{masv});
                loadDanhSach();
                clear();
                Toast.makeText(MainActivity2.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                db.close();
            } catch (Exception e) {
                db.close();
                Toast.makeText(MainActivity2.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(MainActivity2.this, "Không tìm thấy mã sinh viên ", Toast.LENGTH_SHORT).show();
        }
    }

    public void search(View view) {
    }
}




