package cliente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ClienteGUI {
    private Socket socket;
    private BufferedReader entrada;
    private PrintWriter salida;

    private JFrame frame = new JFrame("Chat");
    private JTextArea areaMensajes = new JTextArea(15, 50);
    private JTextField campoEntrada = new JTextField(40);
    private JButton botonEnviar = new JButton("Enviar");

    public ClienteGUI() {
        areaMensajes.setEditable(false);
        frame.getContentPane().add(new JScrollPane(areaMensajes), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.add(campoEntrada);
        panel.add(botonEnviar);
        frame.getContentPane().add(panel, BorderLayout.SOUTH);

        campoEntrada.addActionListener(e -> enviarMensaje());
        botonEnviar.addActionListener(e -> enviarMensaje());

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        conectar();
    }

    private void conectar() {
        try {
            socket = new Socket("localhost", 12345);
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            salida = new PrintWriter(socket.getOutputStream(), true);

            while (true) {
                String respuesta = entrada.readLine();
                if ("LOGIN?".equals(respuesta)) {
                    String nombre = JOptionPane.showInputDialog(frame, "Ingresa tu nombre:");
                    salida.println(nombre);
                } else if ("ERROR".equals(respuesta)) {
                    JOptionPane.showMessageDialog(frame, "Nombre ya usado. Intenta otro.");
                } else if ("OK".equals(respuesta)) {
                    break;
                }
            }

            new Thread(() -> {
                String mensaje;
                try {
                    while ((mensaje = entrada.readLine()) != null) {
                        areaMensajes.append(mensaje + "\n");
                    }
                } catch (IOException e) {
                    areaMensajes.append("Conexión cerrada\n");
                }
            }).start();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error al conectar con el servidor.");
        }
    }

    private void enviarMensaje() {
        String mensaje = campoEntrada.getText().trim();
        if (!mensaje.isEmpty()) {
            salida.println(mensaje);
            campoEntrada.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ClienteGUI::new);
    }   
}
