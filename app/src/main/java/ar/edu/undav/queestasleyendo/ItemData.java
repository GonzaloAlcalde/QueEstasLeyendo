package ar.edu.undav.queestasleyendo;

public class ItemData {
    private String nombreLibro;
    private int imageUrl;
    private String autorLibro;
    private String editorialLibro;
    private String generoLibro;
    private String fechaLibro;
    private String puntajeLibro;

    public ItemData(String nombreLibro, int imageUrl, String autorLibro, String editorialLibro, String generoLibro, String fechaLibro, String puntajeLibro){
        this.nombreLibro = nombreLibro;
        this.imageUrl = imageUrl;
        this.autorLibro = autorLibro;
        this.editorialLibro = editorialLibro;
        this.generoLibro = generoLibro;
        this.fechaLibro = fechaLibro;
        this.puntajeLibro = puntajeLibro;

    }

    public String getNombreLibro() {
        return nombreLibro;
    }

    public int getImageUrl() {
        return imageUrl;
    }

    public String getAutorLibro() {
        return autorLibro;
    }

    public String getEditorialLibro() {
        return editorialLibro;
    }

    public String getGeneroLibro() {
        return generoLibro;
    }

    public String getFechaLibro() {
        return fechaLibro;
    }

    public String getPuntajeLibro() {
        return puntajeLibro;
    }
}
