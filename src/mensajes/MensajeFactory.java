package mensajes;

public class MensajeFactory {
    public static Mensaje crearMensaje(String tipo, String contenido, String autor) {
        switch (tipo.toLowerCase()) {
            case "alerta":
                return new MensajeAlerta(contenido, autor);
            case "notificacion":
                return new MensajeNotificacion(contenido, autor);
            default:
                return new MensajeTexto(contenido, autor);
        }
    }
}
