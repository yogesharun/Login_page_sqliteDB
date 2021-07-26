package com.example.sqldb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {
    private EditText name, rollno, dep,email,pass,cpass;
    private DatePicker Dob;
    private RadioGroup Gender;
    private Button add,upload;
    ImageView iv;
    private DBhandler dbHandler;
    int SELECT_PICTURE=200;
    byte[] image;


    String sname="",sdep="",semail="",spass="",sdob="",sgender="";
    int srollno=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        name=(EditText)findViewById(R.id.Studentname);
        iv=(ImageView)findViewById(R.id.imageView);
        rollno=(EditText)findViewById(R.id.Rollno);
        dep=(EditText)findViewById(R.id.Dept);
        email=(EditText)findViewById(R.id.email);
        pass=(EditText)findViewById(R.id.Password);
        cpass=(EditText)findViewById(R.id.Confirmpass);
        Gender=(RadioGroup)findViewById(R.id.Gender);
        dbHandler=new DBhandler(RegisterActivity.this);
        add=(Button)findViewById(R.id.idBtnAddinfo);
        Dob=(DatePicker)findViewById(R.id.DOB);
        Gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.Male: sgender="M";
                        break;
                    case R.id.Female:sgender="F";
                        break;
                }
            }
        });
        upload=(Button)findViewById(R.id.upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
            }
        });
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        Dob.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                sdob=dayOfMonth+"/"+monthOfYear+"/"+year;
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sname = name.getText().toString();
                if(rollno.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please enter all the data..", Toast.LENGTH_SHORT).show();
                    return;
                }
                srollno=Integer.parseInt(rollno.getText().toString());
                sdep = dep.getText().toString();
                semail = email.getText().toString();
                spass=pass.getText().toString();
                if (cpass.getText().toString() .equals(spass)) {
                    MessageDigest messageDigest = null;
                    try {
                        messageDigest = MessageDigest.getInstance("MD5");
                        messageDigest.update(spass.getBytes());
                        byte[] hashPassword=messageDigest.digest();
                        StringBuilder stringBuilder=new StringBuilder();
                        for(byte b: hashPassword)
                        {
                            stringBuilder.append(String.format("%02x",b));
                        }
                        spass=stringBuilder.toString();
                        if (sname.isEmpty() && srollno==-1 && sdep.isEmpty() && semail.isEmpty() && sgender.isEmpty() && sdep.isEmpty()) {
                            Toast.makeText(RegisterActivity.this, "Please enter all the data..", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        dbHandler.addInformation(sname,srollno,sdep,semail,spass,sgender,sdob,image);
                        Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(i);
                    }
                    catch (NoSuchAlgorithmException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "Password missmatch..", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    iv.setImageURI(selectedImageUri);
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),selectedImageUri);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
                        image= byteArrayOutputStream.toByteArray();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }



                }
            }
        }



    }
}