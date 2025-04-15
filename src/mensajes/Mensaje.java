package mensajes;

public abstract class Mensaje {
    protected String contenido;
    protected String autor;

    public Mensaje(String contenido, String autor) {
        this.contenido = contenido;
        this.autor = autor;
    }

    public abstract String formatear();
}
