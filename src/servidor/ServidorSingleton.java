package servidor;

import java.io.IOException;
import java.net.ServerSocket;

public class ServidorSingleton {
    private static ServidorSingleton instancia;
    private ServerSocket serverSocket;

    private ServidorSingleton() {
        try {
            serverSocket = new ServerSocket(12345);
            System.out.println("Servidor iniciado en el puerto 12345");
        } catch (IOException e) {
            throw new RuntimeException("No se pudo iniciar el servidor", e);
        }
    }

    public static ServidorSingleton getInstancia() {
        if (instancia == null) {
            instancia = new ServidorSingleton();
        }
        return instancia;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }
}
