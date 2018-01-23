package br.edu.ufcg.fidu.views.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.EventListener;

import br.edu.ufcg.fidu.R;
import br.edu.ufcg.fidu.models.Donee;
import br.edu.ufcg.fidu.models.Donor;
import br.edu.ufcg.fidu.models.User;
import br.edu.ufcg.fidu.utils.FirebaseConnection;
import br.edu.ufcg.fidu.utils.SaveData;
import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfileActivity extends AppCompatActivity {
    private EditText etName;
    private EditText etOccupation;
    private EditText etWebsite;
    private EditText etAddress;
    private EditText etDescription;
    private EditText etFoundedIn;
    private EditText etBenefited;
    private Button btnUpdate;
    private ImageView profilePhoto;
    private ProgressBar uploadProgress;

    private SaveData sv;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;

    private final int IMAGE_GALLERY_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
        sv = new SaveData(this);

        profilePhoto = (ImageView) findViewById(R.id.profilePhoto);
        uploadProgress = (ProgressBar) findViewById(R.id.uploadProgress);
        uploadProgress.setVisibility(View.GONE);
        etName = (EditText) findViewById(R.id.etName);
        etOccupation = (EditText) findViewById(R.id.etOccupation);
        etWebsite = (EditText) findViewById(R.id.etWebsite);
        etAddress = (EditText) findViewById(R.id.etAddress);
        etDescription = (EditText) findViewById(R.id.etDescription);
        etFoundedIn = (EditText) findViewById(R.id.etFoundedIn);
        etBenefited = (EditText) findViewById(R.id.etBenefited);
        etBenefited.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String strValue = etBenefited.getText().toString();
                if (!strValue.equals("")) {
                    int number = Integer.parseInt(strValue);
                    if (number < 0) etBenefited.setText("0");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString();
                String occupation = etOccupation.getText().toString();
                String website = etWebsite.getText().toString();
                String address = etAddress.getText().toString();
                String description = etDescription.getText().toString();
                String strFoundedIn = etFoundedIn.getText().toString();
                String strBenefited = etBenefited.getText().toString();
                int foundedIn = strFoundedIn.equals("") ? 0 : Integer.parseInt(strFoundedIn);
                int benefited = strBenefited.equals("") ? 0 : Integer.parseInt(strBenefited);

                updateInfo(name, occupation, website, address, description, foundedIn, benefited);
            }
        });

        Button btnUpdatePhoto = (Button) findViewById(R.id.btnUpdatePhoto);
        btnUpdatePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPhoto();
            }
        });

        updateUI();
    }

    private void selectPhoto() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

        File pictureDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirPath = pictureDir.getPath();
        Uri data = Uri.parse(pictureDirPath);
        photoPickerIntent.setDataAndType(data, "image/*");
        startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_GALLERY_REQUEST) {
                final Uri imageUri = data.getData();
                final String uid = mAuth.getCurrentUser().getUid();

                // Show progress
                profilePhoto.setVisibility(View.GONE);
                uploadProgress.setVisibility(View.VISIBLE);

                final UploadTask uploadTask;
                uploadTask = mStorage.child("profile_images").child(uid).putFile(imageUri);

                // Success
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    updatePhoto();
                    profilePhoto.setVisibility(View.VISIBLE);
                    uploadProgress.setVisibility(View.GONE);
                // Failure
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    profilePhoto.setVisibility(View.VISIBLE);
                    uploadProgress.setVisibility(View.GONE);

                    Toast.makeText(UpdateProfileActivity.this,
                            e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    }
                });
            }
        }

    }

    private void updateInfo(String name, String occupation, String website, String address,
                            String description, int foundedIn, int benefited) {
        if (sv.isLogged()) {
            String uid = mAuth.getCurrentUser().getUid();
            String email = mAuth.getCurrentUser().getEmail();

            if (sv.getRole() == SaveData.DONEE) {
                Donee donee = new Donee(name, email, occupation, website, address, description,
                        foundedIn, benefited);
                mDatabase.child("users").child("donees").child(uid).setValue(donee);
                sv.writeDonee(donee);
            } else {
                Donor donor = new Donor(name, email, occupation, website);
                mDatabase.child("users").child("donors").child(uid).setValue(donor);
                sv.writeDonor(donor);
            }

            Toast.makeText(this, R.string.profile_updated, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(UpdateProfileActivity.this, MainActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        }
    }

    private void updateUI() {
        if (sv.isLogged()) {
            if (sv.getRole() == SaveData.DONEE) {
                updatePhoto();
                Donee user = sv.readDonee();
                etName.setText(user.getName());
                etOccupation.setText(user.getOccupation());
                etWebsite.setText(user.getWebsite());
                etAddress.setText(user.getAddress());
                etDescription.setText(user.getDescription());
                etFoundedIn.setText(user.getFoundedIn() == 0 ? "" : user.getFoundedIn() + "");
                etBenefited.setText(user.getBenefited() == 0 ? "" : user.getBenefited() + "");
            } else {
                etAddress.setVisibility(View.GONE);
                etDescription.setVisibility(View.GONE);
                etFoundedIn.setVisibility(View.GONE);
                etBenefited.setVisibility(View.GONE);

                Donor user = sv.readDonor();
                etName.setText(user.getName());
                etOccupation.setText(user.getOccupation());
                etWebsite.setText(user.getWebsite());
            }
        } else {
            Toast.makeText(this, "Carregando informações", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void updatePhoto() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference photoRef = mStorage.child("profile_images").child(uid);
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(photoRef)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(profilePhoto);
    }
}
