package ar.edu.undav.queestasleyendo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ItemData[] itemsData;

    public MyAdapter(ItemData[] itemsData) {
        this.itemsData = itemsData;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, null);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData

        viewHolder.nombreLibro.setText(itemsData[position].getNombreLibro());
        viewHolder.autorLibro.setText("Autor: "+itemsData[position].getAutorLibro());
        viewHolder.editorialLibro.setText("Editorial: "+itemsData[position].getEditorialLibro());
        viewHolder.generoLibro.setText("Género: "+itemsData[position].getGeneroLibro());
        viewHolder.fechaLibro.setText("Fecha de leído: "+itemsData[position].getFechaLibro());
        viewHolder.puntajeLibro.setText("Puntaje: "+itemsData[position].getPuntajeLibro());
        viewHolder.imgViewIcon.setImageResource(itemsData[position].getImageUrl());


    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nombreLibro;
        public TextView autorLibro;
        public TextView editorialLibro;
        public TextView generoLibro;
        public TextView fechaLibro;
        public TextView puntajeLibro;
        public ImageView imgViewIcon;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            nombreLibro = (TextView) itemLayoutView.findViewById(R.id.nombreLibro);
            autorLibro = (TextView) itemLayoutView.findViewById(R.id.autorLibro);
            editorialLibro = (TextView) itemLayoutView.findViewById(R.id.editorialLibro);
            generoLibro = (TextView) itemLayoutView.findViewById(R.id.generoLibro);
            fechaLibro = (TextView) itemLayoutView.findViewById(R.id.fechaLibro);
            puntajeLibro = (TextView) itemLayoutView.findViewById(R.id.puntajeLibro);
            imgViewIcon = (ImageView) itemLayoutView.findViewById(R.id.item_icon);
        }
    }


    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return itemsData.length;
    }
}
