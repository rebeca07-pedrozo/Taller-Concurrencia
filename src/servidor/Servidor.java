package servidor;

import mensajes.Mensaje;
import mensajes.MensajeFactory;

import java.io.*;
import java.net.*;
import java.util.*;

public class Servidor {
    private static final int PUERTO = 12345;
    private static final Set<PrintWriter> clientes = Collections.synchronizedSet(new HashSet<>());
    private static final Map<String, Socket> usuarios = Collections.synchronizedMap(new HashMap<>());

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = ServidorSingleton.getInstancia().getServerSocket();

        while (true) {
            Socket socket = serverSocket.accept();
            new Thread(new ManejadorCliente(socket)).start();
        }
    }

    private static void enviarListaUsuarios() {
        StringBuilder lista = new StringBuilder("/usuarios");
        synchronized (usuarios) {
            for (String usuario : usuarios.keySet()) {
                lista.append(" ").append(usuario);
            }
        }
        synchronized (clientes) {
            for (PrintWriter cliente : clientes) {
                cliente.println(lista.toString());
            }
        }
    }

    public void enviarATodos(String mensaje) {
        synchronized (clientes) {
            for (PrintWriter cliente : clientes) {
                cliente.println(mensaje);
            }
        }
    }

    public Set<PrintWriter> getClientes() {
        return clientes;
    }

    static class ManejadorCliente implements Runnable {
        private Socket socket;
        private PrintWriter salida;
        private String nombreUsuario;

        ManejadorCliente(Socket socket) {
            this.socket = socket;
        }

        private String detectarTipo(String mensaje) {
            if (mensaje.startsWith("/alerta ")) return "alerta";
            if (mensaje.startsWith("/noti ")) return "notificacion";
            return "texto";
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
                        new Servidor().enviarATodos(":) " + nombreUsuario + " se ha conectado.");
                        enviarListaUsuarios();
                        break;
                    }
                }

                String mensaje;
                while ((mensaje = entrada.readLine()) != null) {
                    Mensaje m = MensajeFactory.crearMensaje(detectarTipo(mensaje), mensaje, nombreUsuario);
                    new Servidor().enviarATodos(m.formatear());
                }

            } catch (IOException e) {
                System.out.println("Error con el cliente " + nombreUsuario);
                e.printStackTrace();
            } finally {
                try {
                    if (nombreUsuario != null) {
                        usuarios.remove(nombreUsuario);
                        new Servidor().enviarATodos("!!!!!!!!! " + nombreUsuario + " se ha desconectado.");
                        enviarListaUsuarios();
                    }
                    if (salida != null) {
                        clientes.remove(salida);
                    }
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Error al cerrar la conexión de " + nombreUsuario + ". " + e.getMessage());
                }
            }
        }
    }
}
