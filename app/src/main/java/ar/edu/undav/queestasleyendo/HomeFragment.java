package ar.edu.undav.queestasleyendo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import static android.app.Activity.RESULT_CANCELED;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private TextView texto;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_home, container, false);

        texto= v.findViewById(R.id.textView5);
        final FloatingActionButton fab = v.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup= new PopupMenu(getActivity(), fab);
                popup.getMenuInflater().inflate(R.menu.popupmenu_subiractividad, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.libro:
                                Toast.makeText(getActivity(), "libro", Toast.LENGTH_SHORT).show();
                                int requestCode = 0;
                                Intent intent = new Intent(getActivity(), CrearLibro.class);
                                startActivityForResult(intent,requestCode);
                                return true;
                            case R.id.pelicula:
                                Toast.makeText(getActivity(), "pelicula", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.serie:
                                Toast.makeText(getActivity(), "serie", Toast.LENGTH_SHORT).show();
                                return true;
                            default:
                                return true;
                        }
                    }
                });
                popup.show(); //showing popup menu
            }
        });
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED){
            Toast.makeText(getActivity(), "Cancelado", Toast.LENGTH_SHORT).show();
        }
        else{
            String nombre= data.getExtras().getString("Nombre");
            String autor= data.getExtras().getString("Autor");
            String editorial= data.getExtras().getString("Editorial");
            String genero= data.getExtras().getString("Genero");
            String fecha= data.getExtras().getString("Fecha");
            String puntaje= data.getExtras().getString("Puntaje");

            //getActivity().deleteFile("libros");
            File f = getActivity().getFileStreamPath("libros");
            if (f.length() == 0) {
                //El archivo no existe, crearlo
                ManejadorArchivos.EscribirArchivoNuevo("libros", "{\"usuarios\":[]}", getActivity());
                Toast.makeText(getActivity(), "archivo creado", Toast.LENGTH_SHORT).show();

            }

            ArrayList<String> listaStringUsuarioLogeado= ManejadorArchivos.LeerArchivo("usuarioLogeado", getActivity());
            JSONObject JSONUsuarioLogeado = null;
            try {
                JSONUsuarioLogeado = new JSONObject(listaStringUsuarioLogeado.get(0));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String mailUsuarioLogeado = null;
            try {
                mailUsuarioLogeado = JSONUsuarioLogeado.getString("email");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ArrayList<String> listaLibros= ManejadorArchivos.LeerArchivo("libros",getActivity());
            String stringListaLibros= listaLibros.get(0);

            JSONObject JSONObjectUsuarios = null;
            try {
                JSONObjectUsuarios = new JSONObject(stringListaLibros);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject UsuarioYLibros = ManejadorJSON.buscarDatosJSONUsuario(JSONObjectUsuarios, mailUsuarioLogeado, getActivity());

            String email = null;

            if (UsuarioYLibros == null){
                //Agregar usuario y libro nuevo
                JSONObject usuarioNuevo = new JSONObject();
                JSONObject libro = new JSONObject();
                try {
                    libro.put("nombreLibro", nombre);
                    libro.put("autorLibro", autor);
                    libro.put("editorialLibro", editorial);
                    libro.put("generoLibro", genero);
                    libro.put("fechaLibro", fecha);
                    libro.put("puntajeLibro", puntaje);

                    usuarioNuevo.put("email", mailUsuarioLogeado);
                    usuarioNuevo.accumulate("libros",libro);

                    JSONObjectUsuarios.accumulate("usuarios", usuarioNuevo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Toast.makeText(getActivity(), "primer if /", Toast.LENGTH_SHORT).show();
            }
            else{
                //Agregar libro a usuario existente
                JSONObject libro = new JSONObject();
                try {
                    libro.put("nombreLibro", nombre);
                    libro.put("autorLibro", autor);
                    libro.put("editorialLibro", editorial);
                    libro.put("generoLibro", genero);
                    libro.put("fechaLibro", fecha);
                    libro.put("puntajeLibro", puntaje);

                    UsuarioYLibros.accumulate("libros",libro);

                    ManejadorJSON.actualizarValorEnJSONObject(JSONObjectUsuarios, UsuarioYLibros);

                    email= UsuarioYLibros.getString("email");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Toast.makeText(getActivity(), "else /"+mailUsuarioLogeado+"/"+email, Toast.LENGTH_SHORT).show();
            }
            String stringJSONObjectUsuarios = JSONObjectUsuarios.toString();
            ManejadorArchivos.EscribirArchivoNuevo("libros", stringJSONObjectUsuarios, getActivity());

            texto.setText(stringJSONObjectUsuarios);

        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            Toast.makeText(context, "INICIO", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
