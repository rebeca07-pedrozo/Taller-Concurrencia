package servidor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class ServidorTest {
    private Servidor servidor;
    private Socket mockSocket;
    private ByteArrayOutputStream byteArrayOutputStream;
    private PrintWriter mockSalida;

    @BeforeEach
    public void setUp() throws IOException {
        servidor = new Servidor();

        mockSocket = new Socket();

        byteArrayOutputStream = new ByteArrayOutputStream();
        mockSalida = new PrintWriter(byteArrayOutputStream, true);

        servidor.getClientes().add(mockSalida);
    }

    @Test
    public void testEnviarATodos() {
        String mensaje = "¡Nuevo mensaje para todos!";

        servidor.enviarATodos(mensaje);

        String result = byteArrayOutputStream.toString().trim();
        assertTrue(result.contains(mensaje), "El mensaje no fue enviado a todos los clientes.");
    }
}