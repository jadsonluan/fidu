package br.edu.ufcg.fidu;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.edu.ufcg.fidu.models.Donater;

public class DonaterSignupFragment extends Fragment {
    // UI Components
    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etPasswordConfirm;
    private Button btnSignup;

    // Firebase stuff
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public DonaterSignupFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("DonaterSignUpFragment", "onCreate()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_donater_signup, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        etName = (EditText) view.findViewById(R.id.etName);
        etEmail = (EditText) view.findViewById(R.id.etEmail);
        etPassword = (EditText) view.findViewById(R.id.etPassword);
        etPasswordConfirm = (EditText) view.findViewById(R.id.etPasswordConfirm);
        btnSignup = (Button) view.findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String passwordConfirm = etPasswordConfirm.getText().toString();

                signup(name, email, password, passwordConfirm);
            }
        });
    }

    private void signup(final String name, final String email, final String password, final String passwordConfirm) {
        if (!validate(name, email, password, passwordConfirm)) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String uid = task.getResult().getUser().getUid();;
                    Donater donater = new Donater(name, email, password);
                    mDatabase.child("users").child("donors").child(uid).setValue(donater);
                    Toast.makeText(getActivity(), R.string.signup_success, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), R.string.signup_failed, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean validate(String name, String email, String password, String passwordConfirm) {
        if (!password.equals(passwordConfirm)) {
            Toast.makeText(getActivity(), R.string.password_doesnt_matches, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.length() <= 4) {
            Toast.makeText(getActivity(), R.string.password_too_short, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!email.contains("@")) {
            Toast.makeText(getActivity(), R.string.email_invalid, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (name == null || name.trim().equals("")) {
            Toast.makeText(getActivity(), R.string.name_is_empty, Toast.LENGTH_SHORT).show();
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
