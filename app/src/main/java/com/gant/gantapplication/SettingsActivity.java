package com.gant.gantapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.app.appsearch.StorageInfo;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gant.gantapplication.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.coroutines.ContinuationKt;


public class SettingsActivity extends AppCompatActivity
{
    private CircleImageView profileImageView;
    private EditText fullNameEditText, userPhoneEditText, addressEditText;
    private TextView profileChangeTextBtn, closeTextBtn, saveTextBtn;

    private Uri imageUri;
    private String myUrl = "";
    private StorageTask uploadTask;
    private StorageReference storageProfilePictureRef;
    private String checker = "";

    private ProgressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        storageProfilePictureRef = FirebaseStorage.getInstance() .getReference() .child("Profile Pictures");

        profileImageView = (CircleImageView) findViewById(R.id.settings_profile_image);
        fullNameEditText = (EditText) findViewById(R.id.settings_full_name);
        userPhoneEditText = (EditText) findViewById(R.id.settings_phone_number);
        addressEditText = (EditText) findViewById(R.id.settings_address);
        profileChangeTextBtn = (TextView) findViewById(R.id.profile_image_change_btn);
        closeTextBtn = (TextView) findViewById(R.id.close_settings_btn);
        saveTextBtn = (TextView) findViewById(R.id.update_account_settings_btn);


        userInfoDisplay(profileImageView, fullNameEditText, userPhoneEditText, addressEditText);


        closeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        SaveTextButton.setOnClickListener(new Veiw.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (checker.equals("clicked"))
                {
                    userInfoSaved();
                }
                else
                {
                    updateOnlyUserInfo();
                }
            }
        });

        profileChangeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                checker = "clicked";

                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                        .start(SettingsActivity.this);
            }
        });
    }



    private void updateOnlyUserInfo()
    {
        HashMap<String, Object> userMap = new HashMap<>();
        userMap. put("name", fullNameEditText.getText().toString());
        userMap. put("address", addressEditText.getText().toString());
        userMap. put("phoneOrder", userPhoneEditText.getText().toString());
        ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);

        startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
        Toast.makeText(SettingsActivity.this,"Profile Info update Successfully", Toast.LENGTH_SHORT).show();
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            profileImageView.setImageURI(imageUri);
        }
        else
        {
            Toast.makeText(this,"Error, Try Again.",Toast.LENGTH_SHORT).show();

            startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
            finish();
        }
    }



    private void userInfoSaved()
    {
        if (TextUtils.isEmpty(fullNameEditText.getText().toString()))
        {
            Toast.makeText(this,"Name is mandatory.",Toast.LENGTH_SHORT).show();
        }
        else-if (TextUtils.isEmpty(addressEditText.getText().toString()))
    {
        Toast.makeText(this,"Name is address.",Toast.LENGTH_SHORT).show();
    }
    else-if (TextUtils.isEmpty(userPhoneEditText.getText().toString()))
    {
        Toast.makeText(this,"Name is mandatory.",Toast.LENGTH_SHORT).show();
    } else if (checker.equals("clicked"))
    {
        uploadImage();
    }
    }



    private void uploadImage()
    {
        final ProgressDialog ProgressDialog = new ProgressDialog(this);
        ProgressDialog.setTitle("Update Profile");
        ProgressDialog.setMessage("Please wait, while we are updating your account information");
        ProgressDialog.setCanceledOnTouchOutside(false);
        ProgressDialog.show();

        if (imageUri != null)
        {
            final StorageReference fileRef = storageProfilePictureRef
                    .child(Prevalent.currentOnlineUser.getPhone() + ".jpg");

            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new ContinuationKt())
            {
                @Override
                public Object then(@NonNull Task task) throw Exception
                {
                    if (!task.isSuccessful())
                    {
                        throw task.getException();
                    }

                    return fileRef.getDownloadUrl();
                }
            })
            .addOnCompleteListener(new OnCompleteListener<Uri>()
        {
            @Override
            public void onComplete(@NonNull Task<Uri> task)
            {
                if (task.isSuccessful())
                {
                    Uri downloadUrl = task.getResult();
                    myUrl = downloadUrl.toString();

                    DatabaseReference ref = FirebaseDatabase.getInstance() .getReference() .child("Users");

                    HashMap<String, Object> userMap = new HashMap<>();
                    userMap. put("name", fullNameEditText.getText().toString());
                    userMap. put("address", addressEditText.getText().toString());
                    userMap. put("phoneOrder", userPhoneEditText.getText().toString());
                    userMap. put("image", myUrl);
                    ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);

                    prgressDialog.dismiss();

                    startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
                    Toast.makeText(SettingsActivity.this,"Profile Info update Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                {
                    prgressDialog.dismiss();
                    Toast.makeText(SettingsActivity.this,"Error.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        }
        else
        {
            Toast.makeText(this,"image is not selected.",Toast.LENGTH_SHORT).show();
        }
    }

    private void userInfoDisplay(CircleImageView profileImageView, EditText fullNameEditText, EditText userPhoneEditText, EditText addressEditText)
    {
        DatabaseReference UsersRef = FirebaseDatabase.getInstance() .getReference() .child("Users") .child(Prevalent.currentOnlineUser.getPhone());

        UsersRef.addValueEventListener(new ValueEventListener() {
                                           @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   if (dataSnapshot.exists())
                   {
                   if (dataSnapshot.child("image").exists())
                   {
                   String image = dataSnapshot.child("image") .getValue() .toString();
                   String name = dataSnapshot.child("name") .getValue() .toString();
                   String phone = dataSnapshot.child("phone") .getValue() .toString();
                   String address = dataSnapshot.child("address") .getValue() .toString();

                   Picasso.get() .load(image) .into(profileImageView);
                   fullNameEditText.setText(name);
                   userPhoneEditText.setText(phone);
                   addressEditText.setText(address);
                    }
             }

          }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseerror) {

               }
    });

    }
}