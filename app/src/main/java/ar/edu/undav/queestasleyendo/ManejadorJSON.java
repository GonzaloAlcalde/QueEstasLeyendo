package ar.edu.undav.queestasleyendo;

import android.content.Context;
import android.os.SystemClock;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class ManejadorJSON {
    private static Context applicationContext;

    public static Boolean validarUsuarioContraseniaJSON(ArrayList<String> JSONString, String emailBuscado, String contraseniaBuscada, Context context) throws JSONException {
        Boolean loginValido = false;
        try {
            Boolean usuarioEncontrado;
            Boolean contraseniaEncontrada;
            loginValido = false;
            String email;
            String contrasenia;
            Iterator<String> i = JSONString.iterator();
            while (!loginValido && i.hasNext()) {
                usuarioEncontrado = false;
                contraseniaEncontrada = false;

                String linea = i.next();
                JSONObject jsonObj = new JSONObject(linea);

                email = jsonObj.getString("email");
                contrasenia = jsonObj.getString("pass");

                if (emailBuscado.equals(email) && contraseniaBuscada.equals(contrasenia)) {
                    loginValido = true;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return loginValido;
    }
}