package ar.edu.undav.queestasleyendo;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CrearLibro extends AppCompatActivity {

    private EditText libroNombre;
    private EditText libroAutor;
    private EditText libroEditorial;
    private EditText libroGenero;
    private EditText libroFecha;
    private SeekBar puntuacion;
    private Button botonAceptar;
    private Button botonCancelar;
    private TextView numPuntaje;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_libro);

        libroNombre= findViewById(R.id.libroNombre);
        libroAutor= findViewById(R.id.libroAutor);
        libroEditorial= findViewById(R.id.libroEditorial);
        libroGenero= findViewById(R.id.libroGenero);
        numPuntaje= findViewById(R.id.textView7);
        numPuntaje.setText("5");

        libroNombre.requestFocus();

        libroFecha= findViewById(R.id.libroFecha);
        libroFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        puntuacion= findViewById(R.id.seekBar);
        puntuacion.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String seekbarValue = String.valueOf(progress);
                numPuntaje.setText(seekbarValue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        botonAceptar= findViewById(R.id.button);
        botonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validar();
            }
        });

        botonCancelar= findViewById(R.id.button2);
        botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    private void validar(){
        libroNombre.setError(null);
        libroAutor.setError(null);
        libroEditorial.setError(null);
        libroGenero.setError(null);
        libroFecha.setError(null);

        boolean cancel = false;
        View focusView = null;

        String nombre = libroNombre.getText().toString();
        String autor = libroAutor.getText().toString();
        String editorial = libroEditorial.getText().toString();
        String genero = libroGenero.getText().toString();
        String fecha = libroFecha.getText().toString();

        String puntaje= String.valueOf(puntuacion.getProgress());

        if(fecha.isEmpty()){
            libroFecha.setError("");
            focusView= libroFecha;
            cancel = true;
        }
        else if(esDespuesDeHoy(fecha)){
            libroFecha.setError("");
            focusView= libroFecha;
            Snackbar.make(libroFecha, "La fecha elegida es posterior a la actual", Snackbar.LENGTH_SHORT).show();
            cancel = true;
        }
        if(genero.isEmpty()){
            libroGenero.setError(getString(R.string.error_field_required));
            focusView= libroGenero;
            cancel = true;
        }
        if(editorial.isEmpty()){
            libroEditorial.setError(getString(R.string.error_field_required));
            focusView= libroEditorial;
            cancel = true;
        }
        if(autor.isEmpty()){
            libroAutor.setError(getString(R.string.error_field_required));
            focusView= libroAutor;
            cancel = true;
        }
        if(nombre.isEmpty()){
            libroNombre.setError(getString(R.string.error_field_required));
            focusView= libroNombre;
            cancel = true;
        }

        if(cancel){
            focusView.requestFocus();
        }
        else{
            Intent i= getIntent();
            i.putExtra("Nombre", nombre);
            i.putExtra("Autor", autor);
            i.putExtra("Editorial", editorial);
            i.putExtra("Genero", genero);
            i.putExtra("Fecha", fecha);
            i.putExtra("Puntaje", puntaje);
            setResult(RESULT_OK, i);
            finish();
        }
    }

    private boolean esDespuesDeHoy(String fecha) {
        boolean fechaElegidaEsDespuesDeHoy=false;
        Date fechaActual= Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy");
        Date fechaElegida = null;
        try {
            fechaElegida= dateFormat.parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (fechaActual.before(fechaElegida)){
            fechaElegidaEsDespuesDeHoy=true;
        }
        return fechaElegidaEsDespuesDeHoy;
    }

    private void showDatePickerDialog(){
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because january is zero
                String stringDay= String.format("%02d", day);
                String stringMonth= String.format("%02d", month+1);
                final String selectedDate = stringDay + "/" + stringMonth + "/" + year;
                libroFecha.setText(selectedDate);
            }
        });
        newFragment.show(getFragmentManager(), "datePicker");
    }
}
