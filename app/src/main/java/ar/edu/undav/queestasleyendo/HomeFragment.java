package ar.edu.undav.queestasleyendo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
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
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private JSONObject JSONObjectUsuarios;
    private JSONObject UsuarioYLibros;
    private String mailUsuarioLogeado;

    private MyAdapter mAdapter;
    private RecyclerView recyclerView;

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

        final FloatingActionButton fab = v.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(getActivity(), fab);
                popup.getMenuInflater().inflate(R.menu.popupmenu_subiractividad, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.libro:
                                int requestCode = 0;
                                Intent intent = new Intent(getActivity(), CrearLibro.class);
                                startActivityForResult(intent, requestCode);
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

        File f = getActivity().getFileStreamPath("libros");
        if (f.length() == 0) {
            //El archivo no existe, crearlo
            ManejadorArchivos.EscribirArchivoNuevo("libros", "{\"usuarios\":[]}", getActivity());
            Toast.makeText(getActivity(), "archivo creado", Toast.LENGTH_SHORT).show();

        }


        obtenerLibrosJSON();

        // 1. get a reference to recyclerView
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        // this is data from recycler view
        ItemData[] itemsData = new ItemData[0];
        if(!(UsuarioYLibros==null)) {
            try {
                itemsData = crearItemsDataLibros();
            } catch (NullPointerException e) {
                itemsData = null;
            }
        }

        if (!(itemsData == null)) {
            // 2. set layoutManger
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            // 3. create an adapter
            mAdapter = new MyAdapter(itemsData);
            // 4. set adapter
            recyclerView.setAdapter(mAdapter);
            // 5. set item animator to DefaultAnimator
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }

        return v;
    }

    private ItemData[] crearItemsDataLibros() {
        JSONArray arrayLibros = null;
        ItemData[] itemsData = null;
        try {
            arrayLibros = UsuarioYLibros.getJSONArray("libros");

            int cantidadLibros = arrayLibros.length();

            if(cantidadLibros>0){
                itemsData = new ItemData[arrayLibros.length()];
                String nombreLibro;
                String autorLibro;
                String editorialLibro;
                String generoLibro;
                String fechaLibro;
                String puntajeLibro;

                for (int i = 0; i < arrayLibros.length(); i++) {
                    JSONObject row = arrayLibros.getJSONObject(i);
                    nombreLibro = row.getString("nombreLibro");
                    autorLibro = row.getString("autorLibro");
                    editorialLibro = row.getString("editorialLibro");
                    generoLibro = row.getString("generoLibro");
                    fechaLibro = row.getString("fechaLibro");
                    puntajeLibro = row.getString("puntajeLibro");

                    itemsData[i] = new ItemData(nombreLibro, R.drawable.ic_library_books_black_24dp, autorLibro, editorialLibro, generoLibro, fechaLibro, puntajeLibro);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return itemsData;
    }

    private void obtenerLibrosJSON() {
        ArrayList<String> listaStringUsuarioLogeado= ManejadorArchivos.LeerArchivo("usuarioLogeado", getActivity());
        JSONObject JSONUsuarioLogeado = null;
        try {
            JSONUsuarioLogeado = new JSONObject(listaStringUsuarioLogeado.get(0));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            mailUsuarioLogeado = JSONUsuarioLogeado.getString("email");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<String> listaLibros= ManejadorArchivos.LeerArchivo("libros",getActivity());
        String stringListaLibros= listaLibros.get(0);

        try {
            JSONObjectUsuarios = new JSONObject(stringListaLibros);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        UsuarioYLibros = ManejadorJSON.buscarDatosJSONUsuario(JSONObjectUsuarios, mailUsuarioLogeado, getActivity());
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

                    JSONArray libros = new JSONArray();
                    libros.put(libro);

                    usuarioNuevo.put("email", mailUsuarioLogeado);
                    usuarioNuevo.put("libros", libros);

                    JSONObjectUsuarios.accumulate("usuarios", usuarioNuevo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            String stringJSONObjectUsuarios = JSONObjectUsuarios.toString();
            ManejadorArchivos.EscribirArchivoNuevo("libros", stringJSONObjectUsuarios, getActivity());

            obtenerLibrosJSON();

            ItemData[] itemsData = new ItemData[0];
            if(!(UsuarioYLibros==null)) {
                try {
                    itemsData = crearItemsDataLibros();
                } catch (NullPointerException e) {
                    itemsData = null;
                }
            }
            if (!(itemsData == null)) {
                // 2. set layoutManger
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                // 3. create an adapter
                mAdapter = new MyAdapter(itemsData);
                // 4. set adapter
                recyclerView.setAdapter(mAdapter);
                // 5. set item animator to DefaultAnimator
                recyclerView.setItemAnimator(new DefaultItemAnimator());
            }
        }
    }

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
        void onFragmentInteraction(Uri uri);
    }
}
