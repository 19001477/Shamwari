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
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class standard_home extends AppCompatActivity {

    //Declaring private global variables and listviews
    private String value;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard_home);

        loadGreeting();

        loadProperties();

        // ListView Item Click Listener
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //ListView Clicked item index
                int itemPosition = position;

                //ListView Clicked item value
                String itemValue = (String) lv.getItemAtPosition(position);

                //Sets chosen property in globals class
                globals.chosenProp = itemValue;

                goToPropView();
            }
        });
    }

    //Method that identifies the logged in users username and displays it back to them through textview
    private void loadGreeting() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference standardUserRef = rootRef.child("Users").child("standard").child(globals.loggedUser);

        TextView tvGreeting = (TextView) findViewById(R.id.tvGreeting);

        standardUserRef.child("username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                value = dataSnapshot.getValue(String.class);
                tvGreeting.setText("Welcome " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Error", "Failed to read value.", error.toException());
            }
        });
    }

    //Semicolonworld (2019)
    //Method to load properties into the listview
    private void loadProperties() {
        List<String> propList = new ArrayList<String>();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, propList);

        lv = (ListView) findViewById(R.id.lvAllProps);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference propRef = rootRef.child("Properties");

        propRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        //Gets node name
                        String answer = ds.getKey();
                        DatabaseReference propNameRef = propRef.child(answer).child("name");

                        propNameRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (!task.isSuccessful()) {
                                    Log.e("firebase", "Error getting data", task.getException());
                                }
                                else {
                                    //Adds property name to list
                                    propList.add(answer);
                                    arrayAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"Doesn't exist",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        //Pushes list items to listview
        lv.setAdapter(arrayAdapter);
    }

    //Goes to standard property view activity
    private void goToPropView() {
        Intent intent = new Intent(this, property_view_standard.class);
        startActivity(intent);
    }

    //Goes back to login activity
    public void goToLogin(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}