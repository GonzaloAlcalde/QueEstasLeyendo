package ar.edu.undav.queestasleyendo;

import android.content.Context;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.nfc.FormatException;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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

    /*
    public static JSONArray oredenarJSONArrayPorPuntaje(JSONArray arrayAOrdenar){
        return ordenarJSONArray(arrayAOrdenar, "puntaje");
    }
    */

    public static JSONArray ordenarJSONArrayPorFecha(JSONArray arrayAOrdenar){
        ArrayList<JSONObject> lista = convertirJSONArrayAList(arrayAOrdenar);
        final java.text.DateFormat formatter = java.text.DateFormat.getDateInstance();
        Collections.sort(lista, new Comparator<JSONObject>() {

            public int compare(JSONObject a, JSONObject b) {
                Date valA = new Date(), valB = new Date();
                try {
                    valA = formatter.parse(a.getString("fechaLibro"));
                    valB = formatter.parse(b.getString("fechaLibro"));
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
                catch (ParseException e){
                    e.printStackTrace();
                }
                return (valA).compareTo(valB);
            }
        });
        return convertirListAJSONArray(lista);
    }

    public static JSONArray ordenarJSONArray(JSONArray arrayAOrdenar){
        ArrayList<JSONObject> lista = convertirJSONArrayAList(arrayAOrdenar);
        Collections.sort(lista, new Comparator<JSONObject>() {

            public int compare(JSONObject a, JSONObject b) {
                Integer valA = 0, valB = 0;
                try {
                    valA = a.getInt("puntajeLibro");
                    valB = b.getInt("puntajeLibro");
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
                return valA.compareTo(valB);
            }
        });
        return convertirListAJSONArray(lista);
    }

    /*
    private static int compare(Object a, Object b, String tipo){
        switch (tipo){
            case "fecha":
                return ((Date)a).compareTo((Date)b);
            case "puntaje":
                return  ((Integer)a).compareTo((Integer)b);
        }
    }
    */

    private static ArrayList<JSONObject> convertirJSONArrayAList(JSONArray lista){

        ArrayList<JSONObject> lista2 = new ArrayList<>();
        for (int i = 0; i < lista.length(); i++) {
            try{
                lista2.add(lista.getJSONObject(i));
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }

        return lista2;
    }

    private static JSONArray convertirListAJSONArray(ArrayList<JSONObject> lista){
        JSONArray lista2= new JSONArray();
        for(int i = lista.size() - 1; i > 0; i--){
            lista2.put(lista.get(i));
        }
        return lista2;
    }
}
