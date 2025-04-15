package mensajes;

public class MensajeNotificacion extends Mensaje {
    public MensajeNotificacion(String contenido, String autor) {
        super(contenido, autor);
    }

    @Override
    public String formatear() {
        return " Notificación de " + autor + ": " + contenido;
    }
}
