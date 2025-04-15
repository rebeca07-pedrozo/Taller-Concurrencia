package mensajes;

public class MensajeAlerta extends Mensaje {
    public MensajeAlerta(String contenido, String autor) {
        super(contenido, autor);
    }

    @Override
    public String formatear() {
        return "!!!! ALERTA de " + autor + ": " + contenido.toUpperCase();
    }
}
