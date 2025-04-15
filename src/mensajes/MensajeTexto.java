package mensajes;

public class MensajeTexto extends Mensaje {
    public MensajeTexto(String contenido, String autor) {
        super(contenido, autor);
    }

    @Override
    public String formatear() {
        return "! " + autor + ": " + contenido;
    }
}
