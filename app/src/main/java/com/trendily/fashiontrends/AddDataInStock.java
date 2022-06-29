package com.trendily.fashiontrends;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.inputmethod.InputConnectionCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.FileObserver;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class AddDataInStock extends AppCompatActivity {

    private static final String TAG = "AddDataInStock";

    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;


    private ImageView myStockImage;
    Integer REQUEST_CAMERA=1, SELECT_FILE=0;

    Uri selectImageUri;

    StorageReference storageReference;

    ImageView backArrow;
    private ProgressDialog progressDialog;

    private StorageTask uploadTask;



    Spinner categorySelection;
    String[] items;
    ArrayAdapter<String> Adapter;
    EditText selectedCategoryShopName, selectedCategoryPrice,  selectedCategoryMembercity,  selectedCategoryMemberPhoneNumber,  selectedCategoryDescription, selectedCategoryPost;

    Button insertDataInDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data_in_stock);

        myStockImage = (ImageView) findViewById(R.id.stockImage);
        categorySelection = (Spinner) findViewById(R.id.selectCategory);

        items=getResources().getStringArray(R.array.selectcategory);
        Adapter=new ArrayAdapter<String>(this,R.layout.spinner_item,R.id.spinnerTextView,items);
        categorySelection.setAdapter(Adapter);

        selectedCategoryShopName = (EditText) findViewById(R.id.shopName);
        selectedCategoryPrice = (EditText) findViewById(R.id.price);
        selectedCategoryMembercity = (EditText) findViewById(R.id.city);
        selectedCategoryMemberPhoneNumber = (EditText) findViewById(R.id.phoneNumber);
        selectedCategoryPost = (EditText) findViewById(R.id.postCode);
        selectedCategoryDescription = (EditText) findViewById(R.id.description);
        insertDataInDatabase = (Button) findViewById(R.id.submit);

        progressDialog = new ProgressDialog(this);

        backArrow = (ImageView) findViewById(R.id.arrowBack);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });




        myStockImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();
            }
        });


        insertDataInDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (myStockImage.getDrawable() == null)
                {

                    Toast.makeText(AddDataInStock.this,"Please select your product image", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    addPost();
                }
//               else if (uploadTask != null && uploadTask.isInProgress()){
//
//                    Toast.makeText(AddDataInStock.this, "Upload in Progress", Toast.LENGTH_SHORT).show();
//                }
//                else {
//
//                    Toast.makeText(AddDataInStock.this, "Data Successfuly Inserted", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(AddDataInStock.this,Dashboard.class);
//                    startActivity(intent);
//                }

            }
        });


    }
    private  String getExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void addPost() {



        final String categorySelectionInsert = categorySelection.getSelectedItem().toString();
        final String selectedCategoryShop = selectedCategoryShopName.getText().toString();
        final String selectedCategoryPriceInsert = selectedCategoryPrice.getText().toString();
        final String selectedCategoryMembercityInsert = selectedCategoryMembercity.getText().toString();
        final String selectedCategoryMemberPhoneNumberInsert = selectedCategoryMemberPhoneNumber.getText().toString();
        final String selectedCategoryMemberPostCode = selectedCategoryPost.getText().toString();
        final String selectedCategoryDescriptionInsert = selectedCategoryDescription.getText().toString();

        if (categorySelectionInsert.equals("Select Category"))
        {
            Toast.makeText(this, "Select category", Toast.LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(selectedCategoryShop)){
            selectedCategoryShopName.requestFocus();
            selectedCategoryShopName.setError("Please enter Shop name");
        }
        else if (TextUtils.isEmpty(selectedCategoryPriceInsert))

        {
                selectedCategoryPrice.requestFocus();
                selectedCategoryPrice.setError("Set Price");

        }
        else if (TextUtils.isEmpty(selectedCategoryMembercityInsert))

        {
                selectedCategoryMembercity.requestFocus();
                selectedCategoryMembercity.setError("Please enter city name");

        }
        else if (TextUtils.isEmpty(selectedCategoryMemberPhoneNumberInsert))
            {
                selectedCategoryMemberPhoneNumber.requestFocus();
                selectedCategoryMemberPhoneNumber.setError("Please enter Phone number");
            }
        else if (!selectedCategoryMemberPhoneNumberInsert.matches("\\+[0-9]{10,13}$"))
            {

                selectedCategoryMemberPhoneNumber.requestFocus();
                selectedCategoryMemberPhoneNumber.setError("Please enter valid Phone number");
            }
        else if (TextUtils.isEmpty(selectedCategoryMemberPostCode))
            {
                selectedCategoryPost.requestFocus();
                selectedCategoryPost.setError("Please enter Post code");
            }
        else if (TextUtils.isEmpty(selectedCategoryDescriptionInsert))
            {
                selectedCategoryDescription.requestFocus();
                selectedCategoryDescription.setError("Write something about product");
            }

        else {

            progressDialog.setTitle("Adding Product");
            progressDialog.setMessage("Please wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

                storageReference = FirebaseStorage.getInstance().getReference(categorySelectionInsert);
                final StorageReference myRef = storageReference.child(System.currentTimeMillis() + "." + getExtension(selectImageUri));

                uploadTask = myRef.putFile(selectImageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                myRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        String ImageURI = String.valueOf(uri);

                                        HashMap insertData = new HashMap();
                                        insertData.put("ImageURL", ImageURI);
                                        insertData.put("Category", categorySelectionInsert);
                                        insertData.put("ShopName",selectedCategoryShop);
                                        insertData.put("CategoryPrice", selectedCategoryPriceInsert);
                                        insertData.put("MemberCity", selectedCategoryMembercityInsert);
                                        insertData.put("MemberPhone", selectedCategoryMemberPhoneNumberInsert);
                                        insertData.put("PostCode", selectedCategoryMemberPostCode);
                                        insertData.put("CategoryDescription", selectedCategoryDescriptionInsert);

                                        FirebaseDatabase.getInstance().getReference(categorySelectionInsert).push().setValue(insertData)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful())
                                                            {
                                                                Toast.makeText(AddDataInStock.this, "Data Successfuly Inserted", Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(AddDataInStock.this,Dashboard.class);
                                                                startActivity(intent);
                                                                progressDialog.dismiss();
                                                            }
                                                            else
                                                            {
                                                                Toast.makeText(AddDataInStock.this, "Something seems to be wrong. Please try again later.", Toast.LENGTH_LONG).show();
                                                                progressDialog.dismiss();
                                                            }
                                                    }
                                                });

                                        Log.d(TAG, "onSuccess: path" + ImageURI);

                                    }
                                });


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                // ...
                            }
                        });

            }



    }


    // select Image Gallery and Camera code start
    private void SelectImage(){
        final CharSequence[] items={"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddDataInStock.this);
        builder.setTitle("Add Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (items[i].equals("Camera")){
                   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                   {
                       if (checkSelfPermission(Manifest.permission.CAMERA) ==
                               PackageManager.PERMISSION_DENIED ||
                               checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                               PackageManager.PERMISSION_DENIED)
                       {
                           String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                           requestPermissions(permission, PERMISSION_CODE);
                       }

                       else
                           {
                           openCamera();
                           }
                   }
                   else
                       {

                       openCamera();
                       }

                }
                else if (items[i].equals("Gallery")){
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, "Select File"), SELECT_FILE);

                }
                else if (items[i].equals("Cancel")){
                    dialogInterface.dismiss();
                }
            }
        });

        builder.show();
    }
    private void openCamera() {

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From The Camera");
        selectImageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT , selectImageUri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case PERMISSION_CODE:{
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED){
                    openCamera();
                }
                else {
                    Toast.makeText(this, "Permission Denied....", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);

        if (resultCode == Activity.RESULT_OK){
            myStockImage.setImageURI(selectImageUri);

            if (requestCode == REQUEST_CAMERA){


            }
            else if (requestCode == SELECT_FILE){
                 selectImageUri = data.getData();
                myStockImage.setImageURI(selectImageUri);

            }
        }
    }
    // select Image Gallery and Camera code End

}


