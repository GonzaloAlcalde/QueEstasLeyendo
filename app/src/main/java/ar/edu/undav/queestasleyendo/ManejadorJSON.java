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

    public static JSONObject buscarDatosJSONUsuario(JSONObject JSONObjectABuscar, String emailBuscado, Context context){
        Boolean usuarioEncontrado = false;
        JSONObject DatosJSONDeUsuario = null;
        int i=0;
        try{
            String email;
            JSONArray JSONArrayABuscar = JSONObjectABuscar.getJSONArray("usuarios");
            while (!usuarioEncontrado && i < JSONArrayABuscar.length()){
                DatosJSONDeUsuario = JSONArrayABuscar.getJSONObject(i);
                email = DatosJSONDeUsuario.getString("email");
                if (emailBuscado.equals(email)){
                    usuarioEncontrado = true;
                }
                i++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(!usuarioEncontrado){
            DatosJSONDeUsuario= null;
        }
        return DatosJSONDeUsuario;
    }

    public static void actualizarValorEnJSONObject(JSONObject TodosLosValores, JSONObject ValorActualizado){
        JSONArray array = null;
        String emailBuscado;
        try {
            array = TodosLosValores.getJSONArray("usuarios");
            emailBuscado = ValorActualizado.getString("email");

            for(int i=0; i < array.length(); i++){
                JSONObject object = array.getJSONObject(i);
                String email = object.getString("email");
                if (email.equals(emailBuscado)){
                    array.put(i, ValorActualizado);
                    TodosLosValores.put("usuarios", array);
                    break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}