package ar.edu.undav.queestasleyendo;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class ManejadorArchivos {
    public static void EscribirArchivo(String nombreArchivo, String texto, Context contexto) {
        try {
            FileOutputStream fileOutputStream = contexto.openFileOutput(nombreArchivo, Context.MODE_APPEND);
            OutputStreamWriter writer= new OutputStreamWriter(fileOutputStream);
            writer.append(texto);

            fileOutputStream.flush();
            writer.flush();
            writer.close();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> LeerArchivo(String nombreArchivo, Context contexto) {
        ArrayList<String> texto = new ArrayList<String>();
        try {
            FileInputStream fileInputStream = contexto.openFileInput(nombreArchivo);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            String linea;
            while ((linea = bufferedReader.readLine()) != null) {
                texto.add(linea);
            }

            bufferedReader.close();
            inputStreamReader.close();
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return texto;
    }
/*
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo));
            String line;
            while ((line = reader.readLine()) != null)
            {
                texto.add(line);
            }
            reader.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return texto;
    }
}*/
}