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
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.storage.StorageReference;

public class property_view_standard extends AppCompatActivity {

    //Declaring private global variables
    private String name;
    private String address;
    private String category;
    private String size;
    private String price;
    private String agentEmail;
    private String agentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_view_standard);

        getAgentName();
        setValues();
    }

    //Method that sets all of the global variables to be used by other methods
    private void setValues() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference propRef = rootRef.child("Properties").child(globals.chosenProp);

        propRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        //Fetches the key for each child of the parent node
                        String key = ds.getKey();

                        //Runs key through switch to determine what variable needs to be assigned with the keys value
                        switch(key) {
                            case "name":
                                name = ds.getValue().toString();
                                break;
                            case "address":
                                address = ds.getValue().toString();
                                break;
                            case "category":
                                category = ds.getValue().toString();
                                break;
                            case "size":
                                size = ds.getValue().toString();
                                break;
                            case "price":
                                price = ds.getValue().toString();
                                break;
                            case "agent":
                                break;
                            default:
                                throw new IllegalStateException("Unexpected value: " + key);
                        }
                    }
                }
                //Runs loadDetails method
                loadDetails();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    //Method to set TextView fields with the data that was collected in setValues() method
    private void loadDetails() {
        TextView tvName = (TextView) findViewById(R.id.tvPropertyName);
        TextView tvAddress = (TextView) findViewById(R.id.tvFullAddress);
        TextView tvCategory = (TextView) findViewById(R.id.tvPropCategory);
        TextView tvSize = (TextView) findViewById(R.id.tvRoomSize);
        TextView tvPrice = (TextView) findViewById(R.id.tvPropPrice);

        tvName.setText(name);
        tvAddress.setText(address);
        tvCategory.setText(category);
        tvSize.setText(size + "m2");
        tvPrice.setText("R" + price + ".00 pm");
        getImage();
    }

    //Stackoverflow (2018)
    //Method to retrieve the corresponding property image from firebase storage and displays it in the imageview
    private void getImage() {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference photoReference = storageReference.child("images/" + globals.chosenProp);

        ImageView imageView = (ImageView) findViewById(R.id.ivImage);

        final long ONE_MEGABYTE = 1024 * 1024;
        photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageView.setImageBitmap(bmp);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(), "No Such file or Path found!", Toast.LENGTH_LONG).show();
            }
        });
    }

    //Tutorialspoint (2021)
    //This method registers the button click and opens the users email client with a preloaded message saying that they are interested in the property
    public void sendInterest(View view) {
        Log.i("Send email", "");
        String[] TO = {agentEmail};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "You have an interested buyer for " + globals.chosenProp);
        emailIntent.putExtra(Intent.EXTRA_TEXT, "User: " + globals.loggedUser + " has shown interest in " + globals.chosenProp + ". Respond to them via email to arrange a showing or to exchange information.");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(property_view_standard.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    //Method that retrieves the name of the agent that listed the current property
    private void getAgentName() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference agentRef = rootRef.child("Properties").child(globals.chosenProp).child("agent");

        agentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    agentName = dataSnapshot.getValue().toString();
                }
                getAgentEmail();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    //Method to retrieve the email address of the agent that listed the current property
    private void getAgentEmail() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference emailRef = rootRef.child("Users").child("agent").child(agentName).child("email");

        emailRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    agentEmail = dataSnapshot.getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    public void goToStandardHome(View view) {
        navBack();
    }

    //Goes back to standard home activity
    private void navBack() {
        Intent intent = new Intent(this, standard_home.class);
        startActivity(intent);
    }
}