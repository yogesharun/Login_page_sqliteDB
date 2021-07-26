package com.example.sqldb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowData extends AppCompatActivity {
    ImageView profile;
    TextView name,rollno,dob,gender,department,email;
    Button back;
    DBhandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);
        back=(Button)findViewById(R.id.back);
        name=(TextView)findViewById(R.id.studetnname);
        rollno=(TextView)findViewById(R.id.studentrollno);
        dob=(TextView)findViewById(R.id.studentdob);
        gender=(TextView)findViewById(R.id.studentgender);
        email=(TextView)findViewById(R.id.studentemail);
        profile=(ImageView)findViewById(R.id.profile);
        department=(TextView)findViewById(R.id.studentdepart);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ShowData.this, MainActivity.class);
                startActivity(i);
            }
        });
        db=new DBhandler(ShowData.this);
        Bundle b=getIntent().getExtras();
        email.setText(b.getString("email"));
        Cursor cursor=db.readCourses(email.getText().toString());
        if(cursor.moveToFirst())
        {
            name.setText(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            rollno.setText(cursor.getString(cursor.getColumnIndexOrThrow("rollno")));
            department.setText(cursor.getString(cursor.getColumnIndexOrThrow("department")));
            gender.setText(cursor.getString(cursor.getColumnIndexOrThrow("gender")));
            dob.setText(cursor.getString(cursor.getColumnIndexOrThrow("dob")));
            byte[] bytesImage = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));
            cursor.close();
            Bitmap bitmapImage = BitmapFactory.decodeByteArray(bytesImage, 0, bytesImage.length);
            profile.setImageBitmap(bitmapImage);

        }

    }
}