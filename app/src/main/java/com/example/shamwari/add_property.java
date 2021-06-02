/*
REFERENCES

Abhiandroid.com. 2016. ProgressBar Tutorial With Example In Android Studio | Abhi Android. [online] Available at: <https://abhiandroid.com/ui/progressbar#:~:text=In%20Android%2C%20ProgressBar%20is%20used,use%20style%20attribute%20as%20horizontal.> [Accessed 31 May 2021].
Android Developers. 2021. Spinners  |  Android Developers. [online] Available at: <https://developer.android.com/guide/topics/ui/controls/spinner> [Accessed 31 May 2021].
AndroidExample.com. 2021. Create A Simple Listview - Android Example. [online] Available at: <https://androidexample.com/Create_A_Simple_Listview_-_Android_Example/index.php?view=article_discription&aid=65> [Accessed 31 May 2021].
Coding in Flow. 2021. Hide Soft Keyboard Programmatically - Coding in Flow. [online] Available at: <https://codinginflow.com/tutorials/android/hide-soft-keyboard-programmatically> [Accessed 31 May 2021].
Firebase. 2021. Delete files with Cloud Storage on Android  |  Firebase. [online] Available at: <https://firebase.google.com/docs/storage/android/delete-files> [Accessed 31 May 2021].
Firebase. 2021. Read and Write Data on Android  |  Firebase Realtime Database. [online] Available at: <https://firebase.google.com/docs/database/android/read-and-write> [Accessed 31 May 2021].
GeeksforGeeks. 2019. Android: How to Upload an image on Firebase storage? - GeeksforGeeks. [online] Available at: <https://www.geeksforgeeks.org/android-how-to-upload-an-image-on-firebase-storage/> [Accessed 31 May 2021].
Izuchukwu, C., 2017. How to Upload Images to Firebase from an Android App. [online] Code Envato Tuts+. Available at: <https://code.tutsplus.com/tutorials/image-upload-to-firebase-in-android-application--cms-29934> [Accessed 31 May 2021].
SemicolonWorld - Best Place for Developers. 2019. Checking if a particular value exists in the firebase database - SemicolonWorld. [online] Available at: <https://www.semicolonworld.com/question/49283/checking-if-a-particular-value-exists-in-the-firebase-database> [Accessed 31 May 2021].
Stack Overflow. 2011. How do I change TextView Value inside Java Code?. [online] Available at: <https://stackoverflow.com/questions/4768969/how-do-i-change-textview-value-inside-java-code> [Accessed 31 May 2021].
Stack Overflow. 2018. How to set image view from Firebase storage?. [online] Available at: <https://stackoverflow.com/questions/53617681/how-to-set-image-view-from-firebase-storage> [Accessed 31 May 2021].
Tutorialspoint.com. 2021. Java - Sending Email - Tutorialspoint. [online] Available at: <https://www.tutorialspoint.com/java/java_sending_email.htm> [Accessed 31 May 2021].
 */

package com.example.shamwari;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.IOException;
import java.util.UUID;

public class add_property extends AppCompatActivity {

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;

    private String name;
    private Spinner catSpinner;
    private CheckBox cbImage;

    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property);

        Button btnSelect = findViewById(R.id.btnUpload);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        populateSpinner();

        //On pressing btnSelect imageSelect() is called
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                imageSelect();
            }
        });
    }

    //Firebase (2021)
    //Method to add database values to spinner
    private void populateSpinner() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference agentCatRef = rootRef.child("Categories");

        //Identifies all values under the categories node in database and adds them to list
        agentCatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> categories = new ArrayList<String>();

                for (DataSnapshot addressSnapshot: dataSnapshot.getChildren()) {
                    String category = addressSnapshot.getValue(String.class);
                    if (category!=null){
                        categories.add(category);
                    }
                }

                //Pushes list items to spinner
                catSpinner = (Spinner) findViewById(R.id.spCategories);
                ArrayAdapter<String> addressAdapter = new ArrayAdapter<String>(add_property.this, android.R.layout.simple_spinner_item, categories);
                addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                catSpinner.setAdapter(addressAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    //Firebase (2021)
    public void addProperty(View view) {
        EditText tvPropName = (EditText) findViewById(R.id.tvPropName);
        EditText tvAddress = (EditText) findViewById(R.id.tvAddress);
        EditText tvPrice = (EditText) findViewById(R.id.tvPrice);
        EditText tvSize = (EditText) findViewById(R.id.tvSize);

        name = tvPropName.getText().toString().trim();
        String address = tvAddress.getText().toString().trim();
        String price = tvPrice.getText().toString().trim();
        String size = tvSize.getText().toString().trim();

        try {
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference propertyRef = rootRef.child("Properties");
            DatabaseReference propertyNameRef = rootRef.child("Properties").child(name);

            //Adds all property values captured from EditTexts into the database under a unique property node.
            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.exists()) {
                        try {
                            //Property does not exist
                            propertyNameRef.setValue(name);
                            propertyNameRef.child("name").setValue(name);
                            propertyNameRef.child("address").setValue(address);
                            propertyNameRef.child("price").setValue(price);
                            propertyNameRef.child("size").setValue(size);
                            propertyNameRef.child("agent").setValue(globals.loggedUser);
                            propertyNameRef.child("category").setValue(catSpinner.getSelectedItem().toString().trim());
                            uploadImage();
                            getPropertyNum();
                            clearFields();
                        }
                        catch(Exception e) {
                            Toast.makeText(getApplicationContext(),"Error: " + e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Property already exists",Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            };

            propertyNameRef.addListenerForSingleValueEvent(eventListener);

        } catch (Exception e) {
            Toast.makeText(add_property.this, "Image Uploaded!", Toast.LENGTH_SHORT).show();
        }
    }

    //Method clears all text from EditText fields
    private void clearFields() {
        EditText tvPropName = (EditText) findViewById(R.id.tvPropName);
        EditText tvAddress = (EditText) findViewById(R.id.tvAddress);
        EditText tvPrice = (EditText) findViewById(R.id.tvPrice);
        EditText tvSize = (EditText) findViewById(R.id.tvSize);

        tvPropName.setText("");
        tvAddress.setText("");
        tvPrice.setText("");
        tvSize.setText("");
    }

    //Method returns to agent home view
    public void returnHome(View view) {
        Intent intent = new Intent(this, agent_home.class);
        startActivity(intent);
    }

    // Geeksforgeeks (2019)
    // Select Image method
    private void imageSelect() {
        cbImage = (CheckBox) findViewById(R.id.cbImage);

        try {
            // Defining Implicit Intent to mobile gallery
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);
            cbImage.setChecked(true);
        }
        catch(Exception e) {
            Toast.makeText(getApplicationContext(),"Error: " + e.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    // Geeksforgeeks (2019)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    //Geeksforgeeks (2019)
    //UploadImage method
    private void uploadImage() {
        if (filePath != null) {
            //Code for showing progressDialog while uploading
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/" + name);
            //Adding listeners on upload or failure of image
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //Image uploaded successfully
                    progressDialog.dismiss();
                    Toast.makeText(add_property.this, "Image Uploaded!", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //Error, Image not uploaded
                    progressDialog.dismiss();
                    Toast.makeText(add_property.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                //Progress Listener for loading
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(),"No image found",Toast.LENGTH_SHORT).show();
        }
    }

    //Method grabs the property number value for the specific agent
    private void getPropertyNum() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference agentRef = rootRef.child("Users").child("agent").child(globals.loggedUser);

        agentRef.child("listedProps").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    incrementPropertyNum(Integer.parseInt(String.valueOf(task.getResult().getValue())));
                }
            }
        });
    }

    //Method takes value found in getPropertyNum() method and increments it, overwriting the property num value for the specific agent
    private void incrementPropertyNum(int num) {
        int propNum = num+1;

        try {
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference agentPropRef = rootRef.child("Users").child("agent").child(globals.loggedUser).child("listedProps");

            agentPropRef.setValue(propNum);
        }
        catch (Exception e)
        {
            Log.w("Firebase", "Failed to update value.", e);
        }
    }
}