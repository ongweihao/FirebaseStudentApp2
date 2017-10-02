package sg.edu.rp.webservices.firebasestudentapp2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewProfileActivity extends AppCompatActivity {

    private static final String TAG = "ViewProfileActivity";

    private TextView tvName, tvEmail, tvContactNo, tvHobbies;
    private Button btnEdit;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userProfileRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        userProfileRef = firebaseDatabase.getReference("profiles/" + firebaseUser.getUid());

        tvName = (TextView) findViewById(R.id.textViewName);
        tvName.setText(firebaseUser.getDisplayName());

        tvEmail = (TextView)findViewById(R.id.textViewEmail);
        tvEmail.setText(firebaseUser.getEmail());

        tvContactNo = (TextView)findViewById(R.id.textViewContactNo);
        tvHobbies = (TextView)findViewById(R.id.textViewHobbies);

        btnEdit = (Button)findViewById(R.id.buttonEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), EditProfileActivity.class);
                startActivity(i);
            }
        });


        userProfileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "userProfileRef.addValueEventListener -- onDataChange()");
                UserProfile profile = dataSnapshot.getValue(UserProfile.class);
                if (profile != null) {
                    Log.i(TAG, "profile: " + profile.toString());
                    tvContactNo.setText(profile.getContactNo());
                    tvHobbies.setText(profile.getHobbies());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Database error occurred", databaseError.toException());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_main) {
            Intent i = new Intent(getBaseContext(), MainActivity.class);
            startActivity(i);

        } else if (id == R.id.action_logout) {

            firebaseAuth.signOut();

            Intent i = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}
