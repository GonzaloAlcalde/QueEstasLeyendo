package ar.edu.undav.queestasleyendo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    private TextView email;
    private TextView firstPassword;
    private TextView secondPassword;
    private TextView name;
    private TextView surname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inicializar();
    }

    private void inicializar(){
        firstPassword = findViewById(R.id.passwordFirst);
        secondPassword = findViewById(R.id.passwordSecond);
        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        email = findViewById(R.id.email);
        inicializarRegisterButton();
    }

    private void inicializarRegisterButton(){
        Button registerButton = findViewById(R.id.registry_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });
    }

    private void attemptRegister(){
        // Reset errors.
        email.setError(null);
        firstPassword.setError(null);
        secondPassword.setError(null);
        name.setError(null);
        surname.setError(null);

        boolean cancel = false;
        View focusView = null;

        // Store values at the time of the login attempt.
        String stringEmail = email.getText().toString();
        String stringPassword = firstPassword.getText().toString();
        String stringSecondPassword = secondPassword.getText().toString();
        String stringName = name.getText().toString();
        String stringSurname = surname.getText().toString();

        // Check for a valid password.
        if (!(stringPassword.equals(stringSecondPassword))){
            secondPassword.setError(getString(R.string.error_contrasenias_diferentes));
            focusView = secondPassword;
            cancel = true;
        }
        if (stringSecondPassword.isEmpty()) {
            secondPassword.setError(getString(R.string.error_field_required));
            focusView = secondPassword;
            cancel = true;
        }
        if (stringPassword.isEmpty()) {
            firstPassword.setError(getString(R.string.error_field_required));
            focusView = firstPassword;
            cancel = true;
        }
        // Check for a valid email address.
        if (!verificarEmail(stringEmail)) {
            email.setError(getString(R.string.error_invalid_email));
            focusView = email;
            cancel = true;
        }
        if (stringEmail.isEmpty()) {
            email.setError(getString(R.string.error_field_required));
            focusView = email;
            cancel = true;
        }
        if (stringSurname.isEmpty()) {
            surname.setError(getString(R.string.error_field_required));
            focusView = surname;
            cancel = true;
        }
        if (stringName.isEmpty()) {
            name.setError(getString(R.string.error_field_required));
            focusView = name;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            registrarUsuario();

        }
    }

    private boolean verificarEmail(String email){
        return (email.contains("@") && email.contains("."));
    }

    public void registrarUsuario() {
        String mail = email.getText().toString();
        String password = firstPassword.getText().toString();

        ManejadorArchivos.EscribirArchivo("users", "{\"email\":" + mail + ",\"pass\":" + password + "}\n", getApplicationContext());
        ManejadorArchivos.EscribirArchivoNuevo("usuarioLogeado", "{\"email\":" + mail + ",\"pass\":" + password + "}", getApplicationContext());

        Toast.makeText(getApplicationContext(), "Usuario registrado", Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(), (ManejadorArchivos.LeerArchivo("usuarioLogeado", getApplicationContext())).get(0), Toast.LENGTH_LONG).show();

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
