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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class goals extends AppCompatActivity {

    private ProgressBar simpleProgressBar;
    private TextView tvGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);

        //Instantiates progress bar
        simpleProgressBar = (ProgressBar)findViewById(R.id.pbListed);
        tvGoal = (TextView) findViewById(R.id.tvProps);

        checkListed();
        getMax();
    }

    //Abhiandroid.com (2016)
    //Method that identifies an agents number of listed properties value
    private void checkListed() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference agentRef = rootRef.child("Users").child("agent").child(globals.loggedUser);

        //Gets number of listed properties value and sets progress bar to value
        agentRef.child("listedProps").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int value = dataSnapshot.getValue(int.class);
                simpleProgressBar.setProgress(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Error", "Failed to read value.", error.toException());
            }
        });
    }

    //Abhiandroid.com (2016)
    //Method that retrieves an agents goal property value and sets progress bar max value
    private void getMax() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference agentRef = rootRef.child("Users").child("agent").child(globals.loggedUser);

        //Retrieves max value and sets progress bar max
        agentRef.child("listGoal").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    int max = Integer.parseInt(String.valueOf(task.getResult().getValue()));
                    simpleProgressBar.setMax(max);
                    tvGoal.setText("Have " + max + " listed properties:");
                }
            }
        });
    }

    //Goes to change goal activity on button click
    public void changeGoal(View view) {
        Intent intent = new Intent(this, change_goal.class);
        startActivity(intent);
    }

    //Goes to agent home activity on button click
    public void btnBack(View view) {
        Intent intent = new Intent(this, agent_home.class);
        startActivity(intent);
    }
}