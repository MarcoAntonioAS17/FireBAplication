package com.example.firebapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class FormClass extends Activity {

    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int REQUEST_PERMISION_CAMERA = 3001;
    private ImageView imgvPerfil;
    private Bitmap imageBitmap;
    private EditText etName, etLastName, etAddress, etAge, etPhone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulario_layout);


        imgvPerfil = findViewById(R.id.ivPhoto);
        etName = findViewById(R.id.form_etName);
        etLastName = findViewById(R.id.form_etLastName);
        etAddress = findViewById(R.id.form_etAddress);
        etAge = findViewById(R.id.form_etAge);
        etPhone = findViewById(R.id.form_etPhone);
    }

    public void Button_SaveInfo(View view){
        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageRef = storage.getReference();
        SimpleDateFormat sdf = new SimpleDateFormat("MMddyyhhmmss");
        Date date = new Date();
        String name = sdf.format(date);
        StorageReference perfilRef = storageRef.child("img"+ name + ".jpg");
        //StorageReference perfilImagenesRef = storageRef.child("imagenes/"+name);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = perfilRef.putBytes(data);
        AtomicReference<String> s_uri = null;
        uploadTask.addOnFailureListener(e -> {
            Log.e("TYAM","Error al subir photo" + e.getMessage());
        }).addOnCompleteListener(taskSnapshot -> {
            if(taskSnapshot.isComplete()){
                Task<Uri> getUriTask = perfilRef.getDownloadUrl();

                getUriTask.addOnCompleteListener(t -> {
                    Uri uri = t.getResult();
                    if( uri == null) return;
                    Log.i("TYAM","Enlace "+uri.toString());
                    saveCompleteInfo(name,uri.toString());
                });
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void Button_Take_Picture(View view){
        int camPermission = checkSelfPermission(Manifest.permission.CAMERA);

        if(camPermission != PackageManager.PERMISSION_DENIED){
            requestPermissions(
                    new String[] { Manifest.permission.CAMERA}, REQUEST_PERMISION_CAMERA
            );

            return;
        }
        take_photo();
    }

    void take_photo(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_PERMISION_CAMERA){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                take_photo();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(REQUEST_IMAGE_CAPTURE == requestCode && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imgvPerfil.setImageBitmap(imageBitmap);

        }
    }

    void saveCompleteInfo(String name,String urlImage){
        Individuo people = new Individuo();
        people.Picture = urlImage;
        people.Name = etName.getText().toString();
        people.Last_Name = etLastName.getText().toString();
        people.Address = etAddress.getText().toString();
        people.Age = etAge.getText().toString();
        people.Phone = etPhone.getText().toString();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference new_people = database.getReference("Usuarios");

        HashMap<String, Object> node = new HashMap<>();
        node.put(name,people);

        new_people.updateChildren(node)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getBaseContext(), "Registrado con exito",Toast.LENGTH_LONG).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getBaseContext(),"Error al agregar",Toast.LENGTH_LONG).show();
                    Log.e("TYAM","Error:"+e.getMessage());
                });


    }
}
