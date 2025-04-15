package servidor;

import java.io.*;
import java.net.*;
import java.util.*;

public class Servidor {
    private static final int PUERTO = 12345;
    private static final Set<PrintWriter> clientes = Collections.synchronizedSet(new HashSet<>());
    private static final Map<String, Socket> usuarios = Collections.synchronizedMap(new HashMap<>());

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PUERTO);
        System.out.println("Servidor iniciado en el puerto " + PUERTO);

        while (true) {
            Socket socket = serverSocket.accept();
            new Thread(new ManejadorCliente(socket)).start();
        }
    }

    static class ManejadorCliente implements Runnable {
        private Socket socket;
        private PrintWriter salida;
        private String nombreUsuario;

        ManejadorCliente(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try (
                    BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ) {
                salida = new PrintWriter(socket.getOutputStream(), true);

                while (true) {
                    salida.println("LOGIN?");
                    nombreUsuario = entrada.readLine();
                    if (nombreUsuario == null || usuarios.containsKey(nombreUsuario)) {
                        salida.println("ERROR");
                    } else {
                        usuarios.put(nombreUsuario, socket);
                        clientes.add(salida);
                        salida.println("OK");
                        enviarATodos("🔵 " + nombreUsuario + " se ha conectado.");
                        break;
                    }
                }

                String mensaje;
                while ((mensaje = entrada.readLine()) != null) {
                    enviarATodos("💬 " + nombreUsuario + ": " + mensaje);
                }
            } catch (IOException e) {
                System.out.println("Error con el cliente " + nombreUsuario);
            } finally {
                try {
                    if (nombreUsuario != null) {
                        usuarios.remove(nombreUsuario);
                        enviarATodos("🔴 " + nombreUsuario + " se ha desconectado.");
                    }
                    if (salida != null) {
                        clientes.remove(salida);
                    }
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Error al cerrar conexión");
                }
            }
        }

        private void enviarATodos(String mensaje) {
            synchronized (clientes) {
                for (PrintWriter cliente : clientes) {
                    cliente.println(mensaje);
                }
            }
        }
    }
}
