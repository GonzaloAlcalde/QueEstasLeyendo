package ar.edu.undav.queestasleyendo;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private TextView email;
    private TextView firstPassword;
    private TextView secondPassword;
    private TextView name;
    private TextView surname;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inicializar();
    }

    private void inicializar(){
        inicializarRegisterButton();
        firstPassword = findViewById(R.id.passwordFirst);
        secondPassword = findViewById(R.id.passwordSecond);
        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        email = findViewById(R.id.email);
    }

    private void inicializarRegisterButton(){
        registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(verificarDatos()) {
                    registarUsuario(v);
                }
            }
        });
    }

    private boolean verificarDatos(){
        return verificarContrasena() && verificarEmail() && verificarApellido() && verificarNombre();
    }

    private boolean verificarContrasena(){
        boolean mismaContrasena = firstPassword.getText().toString().equals(secondPassword.getText().toString());
        if (!mismaContrasena) secondPassword.setError("No es la misma contraseña");
        return mismaContrasena;
    }

    private boolean verificarEmail(){
        String emailText = email.getText().toString();
        boolean emailValido = (emailText.contains("@") && emailText.contains("."));
        if (!emailValido) email.setError("Email invalido");
        return emailValido;
    }

    private boolean verificarNombre(){
        boolean nombreValido = verificarStringNoNulo(name.getText().toString());
        if(!nombreValido) name.setError("Campo obligatorio");
        return nombreValido;
    }

    private boolean verificarApellido(){
        boolean apellidoValido = verificarStringNoNulo(surname.getText().toString());
        if(!apellidoValido) surname.setError("Campo obligatorio");
        return apellidoValido;
    }

    private boolean verificarStringNoNulo(String s){
        return !s.equals("");
    }

    public void registarUsuario(View view){
        ManejadorArchivos.EscribirArchivoNuevo("usuarioLogeado","{\"email\":"+email+",\"pass\":"+firstPassword+"}", getApplicationContext());
        Toast.makeText(getApplicationContext(), "Usuario registrado", Toast.LENGTH_SHORT);
        finish();
    }
}
