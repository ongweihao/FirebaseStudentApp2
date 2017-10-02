package sg.edu.rp.webservices.firebasestudentapp2;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity {

    private static final String TAG = "EditProfileActivity";

    private TextView tvEmail;
    private EditText etName, etContactNo, etHobbies;
    private Button btnUpdate;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userProfileRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        userProfileRef = firebaseDatabase.getReference("profiles/" + firebaseUser.getUid());

        etName = (EditText)findViewById(R.id.editTextName);
        etName.setText(firebaseUser.getDisplayName());

        tvEmail = (TextView) findViewById(R.id.textViewEmail);
        tvEmail.setText(firebaseUser.getEmail());

        etContactNo = (EditText)findViewById(R.id.editTextContactNo);
        etHobbies = (EditText)findViewById(R.id.editTextHobbies);

        btnUpdate = (Button)findViewById(R.id.buttonUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(etName.getText().toString())
                        .build();

                firebaseUser.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User profile updated.");
                                }
                            }
                        });

                UserProfile profile = new UserProfile(etContactNo.getText().toString(), etHobbies.getText().toString());
                userProfileRef.setValue(profile);

                Toast.makeText(EditProfileActivity.this, "User profile updated successfully", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getBaseContext(), ViewProfileActivity.class);
                startActivity(i);
            }
        });

        userProfileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "userProfileRef.addValueEventListener -- onDataChange()");
                UserProfile profile = dataSnapshot.getValue(UserProfile.class);
                if (profile != null) {
                    Log.i(TAG, "contact no: " + profile.toString());
                    etContactNo.setText(profile.getContactNo());
                    etHobbies.setText(profile.getHobbies());
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {

            firebaseAuth.signOut();

            Intent i = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}
