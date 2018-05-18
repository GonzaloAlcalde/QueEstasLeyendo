package ar.edu.undav.queestasleyendo;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static android.content.Context.MODE_PRIVATE;

public abstract class ManejadorArchivos {
    public void EscribirArchivo(String nombreArchivo, String texto, Context contexto){
        try{
            FileOutputStream fileOutputStream = contexto.openFileOutput(nombreArchivo, MODE_PRIVATE);
            fileOutputStream.write(texto.getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String LeerArchivo(String nombreArchivo, Context contexto) {
        String texto = null;
        try {
            FileInputStream fileInputStream = contexto.openFileInput(nombreArchivo);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            while ((texto = bufferedReader.readLine()) != null) {
                stringBuffer.append(texto + "\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return texto;
    }

}
