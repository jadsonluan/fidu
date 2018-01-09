package br.edu.ufcg.fidu.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.edu.ufcg.fidu.R;
import br.edu.ufcg.fidu.models.Donee;
import br.edu.ufcg.fidu.utils.SaveData;
import br.edu.ufcg.fidu.views.activities.MainActivity;
import br.edu.ufcg.fidu.views.activities.SelectRoleActivity;

public class DoneeSignupFragment extends Fragment {

    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etPasswordConfirm;
    private EditText etAddress;
    private Button btnSignup;
    private View signupProgress;
    private View signupForm;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public DoneeSignupFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_donee_signup, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        signupProgress = getActivity().findViewById(R.id.signupProgress);
        signupForm = getActivity().findViewById(R.id.signupForm);

        etName = view.findViewById(R.id.etName);
        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        etPasswordConfirm = view.findViewById(R.id.etPasswordConfirm);
        etAddress = view.findViewById(R.id.etAddress);
        btnSignup = view.findViewById(R.id.btnSignup);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String passwordConfirm = etPasswordConfirm.getText().toString();
                String address = etAddress.getText().toString();

                signup(name, email, password, passwordConfirm, address);
            }
        });
    }

    private void signup(final String name, final String email, final String password,
                        final String passwordConfirm, final String address) {

        if (!validate(name, email, password, passwordConfirm, address)) {
            return;
        }

        showProgress(true);
        mAuth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                showProgress(false);
                
                if (task.isSuccessful()) {
                    String uid = task.getResult().getUser().getUid();
                    Donee donee = new Donee(name, email, address);
                    SaveData saveData = new SaveData(SelectRoleActivity.context);
                    saveData.writeDonatee(donee);
                    mDatabase.child("users").child("donees").child(uid).setValue(donee);
                    Toast.makeText(getActivity(), R.string.signup_success, Toast.LENGTH_SHORT)
                            .show();
                    startActivity(new Intent(SelectRoleActivity.context, MainActivity.class));
                    getActivity().finish();
                }
                else {
                    Toast.makeText(getActivity(), R.string.signup_failed, Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

    }

    private void showProgress(boolean show) {
        signupForm.setVisibility(show ? View.GONE : View.VISIBLE);
        signupProgress.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private boolean validate(String name, String email, String password, String passwordConfirm,
                             String address) {
        if (name == null || name.trim().equals("")) {
            Toast.makeText(getActivity(), R.string.name_is_empty, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (address == null || address.trim().equals("")) {
            Toast.makeText(getActivity(), R.string.address_empty, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (email == null || email.trim().equals("")) {
            Toast.makeText(getActivity(), R.string.email_empty, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!email.contains("@") || !email.contains(".")) {
            Toast.makeText(getActivity(), R.string.email_invalid, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.length() < 5) {
            Toast.makeText(getActivity(), R.string.password_too_short, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(passwordConfirm)) {
            Toast.makeText(getActivity(), R.string.password_doesnt_match, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
