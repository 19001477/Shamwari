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

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    //Firebase (2021)
    //Method to register a new user
    public void btnRegister(View view) {
        EditText tvUsername = (EditText) findViewById(R.id.tvEmail);
        EditText tvPassword1 = (EditText) findViewById(R.id.tvFullName);
        EditText tvPassword2 = (EditText) findViewById(R.id.tvPassword2);

        String username = tvUsername.getText().toString().trim();
        String password1 = tvPassword1.getText().toString().trim();
        String password2 = tvPassword2.getText().toString().trim();

        try {
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference agentRef = rootRef.child("Users").child("agent");
            DatabaseReference standardRef = rootRef.child("Users").child("standard");
            DatabaseReference agentUserRef = agentRef.child(username);
            DatabaseReference standardUserRef = standardRef.child(username);

            //Checks to see if the user already exists based on username
            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.exists()) {
                        //User does not exist
                        Switch simpleSwitch = (Switch) findViewById(R.id.swAdmin);
                        Boolean switchState = simpleSwitch.isChecked();
                        boolean isAgentSuccess = false;
                        boolean isStandardSuccess = false;

                        //Checks to see if user is an agent or not
                        if (switchState == true) {
                            if (password1.equals(password2)) {
                                //Registers agent user
                                agentRef.push().setValue(username);
                                agentUserRef.child("password").setValue(password1);
                                agentUserRef.child("username").setValue(username);
                                agentUserRef.child("userType").setValue("agent");
                                agentUserRef.child("listedProps").setValue(0);
                                agentUserRef.child("listGoal").setValue(5);
                                isAgentSuccess = true;
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"Passwords do not match",Toast.LENGTH_SHORT).show();
                            }
                            changeActivityAgent(isAgentSuccess, username);
                        }
                        else {
                            if (password1.equals(password2)) {
                                //Registers normal user
                                standardRef.push().setValue(username);
                                standardUserRef.child("password").setValue(password1);
                                standardUserRef.child("username").setValue(username);
                                standardUserRef.child("userType").setValue("standard");
                                isStandardSuccess = true;
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"Passwords do not match",Toast.LENGTH_SHORT).show();
                            }
                            changeActivityStandard(isStandardSuccess);
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"User already exists",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            };

            agentUserRef.addListenerForSingleValueEvent(eventListener);
        }
        catch (Exception e)
        {
            Log.w("Firebase", "Failed to update value.", e);
        }
    }

    //If user is an agent, goes to agent register activity for further details capturing
    public void changeActivityAgent(boolean isSuccess, String user) {
        if (isSuccess) {
            globals.loggedUser = user;

            Intent intent = new Intent(this, agent_register.class);
            startActivity(intent);
        }
    }

    //If user is standard, goes to standard home activity
    public void changeActivityStandard(boolean isSuccess) {
        if (isSuccess) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}