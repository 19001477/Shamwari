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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class add_category extends AppCompatActivity {

    //Creates class-wide listview
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        //Runs method on layout create
        loadCategories();

        // ListView Item Click Listener
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String itemValue = (String) lv.getItemAtPosition(position);

                //Sets category EditText to itemValue
                EditText tvCategory = (EditText) findViewById(R.id.tvCategory);
                tvCategory.setText(itemValue);

                //Closes keyboard if open
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
            }
        });
    }

    //Firebase (2021)
    private void loadCategories() {
        globals.loadCat = true;

        //Creates list that holds all categories
        List<String> catList = new ArrayList<String>();

        lv = (ListView) findViewById(R.id.lvCategories);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, catList);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference catRef = rootRef.child("Categories");

        //Gets all children under categories node and inserts them into the list
        catRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String answer = ds.getValue().toString();
                        catList.add(answer);
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        //Pushes list items to listview
        lv.setAdapter(arrayAdapter);
    }

    public void btnAddCategory(View view) {
        //Closes keyboard
        closeKeyboard();

        EditText tvCategory = (EditText) findViewById(R.id.tvCategory);

        String category = tvCategory.getText().toString().trim();

        try {
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference catRef = rootRef.child("Categories");
            DatabaseReference catSpecRef = catRef.child(category);

            //Adds category from EditText to the database
            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.exists()) {
                        //Category does not exist
                        catSpecRef.setValue(category);
                        clearCat();
                        loadCategories();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Category already exists",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            };

            catSpecRef.addListenerForSingleValueEvent(eventListener);
        }
        catch (Exception e)
        {
            Log.w("Firebase", "Failed to update value.", e);
        }
    }

    //Coding in flow (2021)
    //Method to close keyboard
    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    //Returns to agent home view on button click
    public void goToAgentHome(View view) {
        Intent intent = new Intent(this, agent_home.class);
        startActivity(intent);
    }

    //Firebase (2021)
    //Method to delete a category on button click
    public void btnDeleteCategory(View view) {
        closeKeyboard();
        EditText tvCategory = (EditText) findViewById(R.id.tvCategory);

        String category = tvCategory.getText().toString().trim();

        try {
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference catRef = rootRef.child("Categories");
            DatabaseReference catSpecRef = catRef.child(category);

            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.exists()) {
                        //Category does not exist
                        Toast.makeText(getApplicationContext(),"Category does not exist",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        //Category exists therefore is deleted from database
                        catSpecRef.removeValue();
                        clearCat();
                        loadCategories();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            };

            catSpecRef.addListenerForSingleValueEvent(eventListener);
        }
        catch (Exception e)
        {
            Log.w("Firebase", "Failed to update value.", e);
        }
    }

    private void clearCat() {
        //Clears all text from the EditText box
        EditText tvCategory = (EditText) findViewById(R.id.tvCategory);
        tvCategory.setText("");
    }
}