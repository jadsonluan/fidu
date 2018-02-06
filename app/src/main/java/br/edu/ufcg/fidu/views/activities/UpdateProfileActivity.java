package br.edu.ufcg.fidu.views.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Calendar;

import br.edu.ufcg.fidu.R;
import br.edu.ufcg.fidu.models.Donee;
import br.edu.ufcg.fidu.models.Donor;
import br.edu.ufcg.fidu.utils.SaveData;

public class UpdateProfileActivity extends AppCompatActivity {
    private EditText etName;
    private EditText etOccupation;
    private EditText etWebsite;
    private EditText etAddress;
    private EditText etDescription;
    private EditText etFoundedIn;
    private EditText etBenefited;
    private ImageView profilePhoto;
    private ProgressBar progressBar;
    private ProgressBar uploadBar;

    private SaveData sv;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;
    private String photoUrl;

    private final int IMAGE_GALLERY_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
        sv = new SaveData(this);
        photoUrl = null;

        profilePhoto = findViewById(R.id.profilePhoto);
        progressBar = findViewById(R.id.imgLoading);
        uploadBar = findViewById(R.id.uploadBar);
        etName = findViewById(R.id.etName);
        etOccupation = findViewById(R.id.etOccupation);
        etWebsite = findViewById(R.id.etWebsite);
        etAddress = findViewById(R.id.etAddress);
        etDescription = findViewById(R.id.etDescription);
        etFoundedIn = findViewById(R.id.etFoundedIn);
        etBenefited = findViewById(R.id.etBenefited);
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

        Button btnUpdate = findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser();
            }
        });

        Button btnUpdatePhoto = findViewById(R.id.btnUpdatePhoto);
        btnUpdatePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPhotoPicker();
            }
        });

        refreshUI();
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_GALLERY_REQUEST) {
                uploadPhoto(data.getData());
            }
        }

    }

    private void uploadPhoto(Uri data) {
        if (data != null) {
            final String uid = mAuth.getCurrentUser().getUid();

            showProgress(true, true);

            final UploadTask uploadTask;
            uploadTask = mStorage.child("profile_images").child(uid).putFile(data);

            // Success
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    photoUrl = taskSnapshot.getDownloadUrl().toString();
                    loadPhoto(photoUrl);
                    // Failure
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    showProgress(false, true);

                    Toast.makeText(UpdateProfileActivity.this,
                            e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    System.out.println("Upload is " + progress + "% done");
                    int currentprogress = (int) progress;
                    uploadBar.setProgress(currentprogress);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    /**
     * Abre a galeria de fotos do usuário
     */
    private void showPhotoPicker() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

        File pictureDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirPath = pictureDir.getPath();
        Uri data = Uri.parse(pictureDirPath);
        photoPickerIntent.setDataAndType(data, "image/*");
        startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);
    }

    /**
     * Edita as informações de um usuário
     */
    private void updateUser() {
        String name = etName.getText().toString();
        String occupation = etOccupation.getText().toString();
        String website = etWebsite.getText().toString();
        String address = etAddress.getText().toString();
        String description = etDescription.getText().toString();
        String strFoundedIn = etFoundedIn.getText().toString();
        String strBenefited = etBenefited.getText().toString();
        int foundedIn = strFoundedIn.equals("") ? 0 : Integer.parseInt(strFoundedIn);
        int benefited = strBenefited.equals("") ? 0 : Integer.parseInt(strBenefited);

        if (!validate(name, foundedIn, benefited)) return;

        if (sv.isLogged()) {
            String uid = mAuth.getCurrentUser().getUid();

            if (sv.getRole() == SaveData.DONEE) {
                Donee donee = sv.readDonee();
                String url = photoUrl != null ? photoUrl : donee.getPhotoUrl();

                donee.setName(name);
                donee.setOccupation(occupation);
                donee.setAddress(address);
                donee.setDescription(description);
                donee.setFoundedIn(foundedIn);
                donee.setBenefited(benefited);
                donee.setWebsite(website);
                donee.setPhotoUrl(url);

                mDatabase.child("users").child("donees").child(uid).setValue(donee);
                sv.writeDonee(donee);
            } else {
                Donor donor = sv.readDonor();
                String url = photoUrl != null ? photoUrl : donor.getPhotoUrl();

                donor.setName(name);
                donor.setOccupation(occupation);
                donor.setWebsite(website);
                donor.setPhotoUrl(url);

                mDatabase.child("users").child("donors").child(uid).setValue(donor);
                sv.writeDonor(donor);
            }

            photoUrl = null;

            Toast.makeText(this, R.string.profile_updated, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(UpdateProfileActivity.this, MainActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        }
    }

    /**
     * Valida informações e exibe mensagens de erro nas views correspondente a cada informação
     * avaliada.
     *
     * @param name Nome do usuário
     * @param foundedIn Ano de fundação da instituição
     * @param benefited Quantidade de pessoas beneficiadas (apenas Donee)
     * @return true, se todas as informações forem válidas. Caso contrário, retorna false
     */
    private boolean validate(String name,
                             int foundedIn, int benefited) {
        boolean isValid = true;
        View focusView = null;

        if (name.trim().equals("")) {
            focusView = etName;
            isValid = false;
            etName.setError(getString(R.string.name_is_empty));
        }

        if (etFoundedIn.getVisibility() == View.VISIBLE) {
            if (foundedIn < 0 || foundedIn >  Calendar.getInstance().get(Calendar.YEAR)) {
                focusView = etFoundedIn;
                isValid = false;
                etFoundedIn.setError(getString(R.string.invalid_founded_in));
            }
        }

        if (etBenefited.getVisibility() == View.VISIBLE) {
            if (benefited < 0) {
                focusView = etBenefited;
                isValid = false;
                etBenefited.setError(getString(R.string.invalid_benefited));
            }
        }

        if (focusView != null) focusView.requestFocus();
        return isValid;
    }

    /**
     * Atualiza a interface do usuário com suas informações.
     */
    private void refreshUI() {
        if (sv.isLogged()) {
            if (sv.getRole() == SaveData.DONEE) {
                Donee user = sv.readDonee();
                etName.setText(user.getName());
                etOccupation.setText(user.getOccupation());
                etWebsite.setText(user.getWebsite());
                etAddress.setText(user.getAddress());
                etDescription.setText(user.getDescription());
                etFoundedIn.setText(user.getFoundedIn() == 0 ? "" : user.getFoundedIn() + "");
                etBenefited.setText(user.getBenefited() == 0 ? "" : user.getBenefited() + "");
                loadPhoto(user.getPhotoUrl());
            } else {

                etAddress.setVisibility(View.GONE);
                etDescription.setVisibility(View.GONE);
                etFoundedIn.setVisibility(View.GONE);
                etBenefited.setVisibility(View.GONE);

                Donor user = sv.readDonor();
                etName.setText(user.getName());
                etOccupation.setText(user.getOccupation());
                etWebsite.setText(user.getWebsite());
                loadPhoto(user.getPhotoUrl());
            }
//        } else {
//             TELA AINDA NÃO CARREGADA [FAZER ALGO SOBRE ISSO], TALVEZ UM LOADING
        }
    }

    /**
     * Carrega a imagem de URL especificada na view da foto de perfil do usuário
     *
     * @param url URL da imagem a ser carregada
     */
    private void loadPhoto(String url) {
        showProgress(false, false);

        try {
            if (!url.equals("")) {
                showProgress(true, false);
                Glide.with(profilePhoto.getContext())
                        .load(url)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                e.printStackTrace();
                                showProgress(false, false);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                showProgress(false, false);
                                return false;
                            }
                        })
                        .into(profilePhoto);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * Controla a visibilidade das barras de progresso e da foto de perfil do usuário.
     * Quando as barras de progresso estão visíveis, a foto de perfil não estará e vice-versa.
     *
     * @param show indica se as barras de progresso devem ou não serem mostradas
     * @param isUpload indica se a barra de upload deve ou não ser mostrada
     */
    private void showProgress(boolean show, boolean isUpload) {
        uploadBar.setVisibility(show && isUpload ? View.VISIBLE : View.GONE);
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        profilePhoto.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}

