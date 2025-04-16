package cliente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public abstract class ClienteGUI {
    private Socket socket;
    private BufferedReader entrada;
    private PrintWriter salida;

    private JFrame frame = new JFrame("Chat");
    private JTextArea areaMensajes = new JTextArea(15, 50);
    private JTextField campoEntrada = new JTextField(40);
    private JButton botonEnviar = new JButton("Enviar");
    private DefaultListModel<String> modelUsuarios = new DefaultListModel<>();
    JList<String> listaUsuarios = new JList<>(modelUsuarios);

    public ClienteGUI() {
        Color fondo = new Color(30, 30, 30);
        Color texto = new Color(230, 230, 230);
        Color fondoPanel = new Color(45, 45, 45);
        Color azulOscuro = new Color(60, 60, 80);

        areaMensajes.setEditable(false);
        areaMensajes.setBackground(fondo);
        areaMensajes.setForeground(texto);
        areaMensajes.setCaretColor(texto);

        campoEntrada.setBackground(fondoPanel);
        campoEntrada.setForeground(texto);
        campoEntrada.setCaretColor(texto);

        botonEnviar.setBackground(azulOscuro);
        botonEnviar.setForeground(texto);
        botonEnviar.setFocusPainted(false);

        listaUsuarios.setBackground(fondo);
        listaUsuarios.setForeground(texto);
        listaUsuarios.setSelectionBackground(new Color(80, 80, 120));
        listaUsuarios.setSelectionForeground(texto);

        JPanel panel = new JPanel();
        panel.setBackground(fondo);
        panel.add(campoEntrada);
        panel.add(botonEnviar);

        frame.getContentPane().setBackground(fondo);
        frame.getContentPane().add(new JScrollPane(areaMensajes), BorderLayout.CENTER);
        frame.getContentPane().add(panel, BorderLayout.SOUTH);
        frame.getContentPane().add(new JScrollPane(listaUsuarios), BorderLayout.WEST);

        campoEntrada.addActionListener(e -> enviarMensaje());
        botonEnviar.addActionListener(e -> enviarMensaje());

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        conectar();
    }

    protected void conectar() {
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
                        if (mensaje.startsWith("/usuarios")) {
                            actualizarListaUsuarios(mensaje);
                        } else {
                            areaMensajes.append(mensaje + "\n");
                        }
                    }
                } catch (IOException e) {
                    areaMensajes.append("Conexión cerrada\n");
                }
            }).start();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error al conectar con el servidor.");
            e.printStackTrace();
        }
    }

    void actualizarListaUsuarios(String mensaje) {
        String[] partes = mensaje.split(" ");
        modelUsuarios.clear();
        for (int i = 1; i < partes.length; i++) {
            modelUsuarios.addElement(partes[i]);
        }
    }

    private void enviarMensaje() {
        String mensaje = campoEntrada.getText().trim();
        if (!mensaje.isEmpty()) {
            try {
                salida.println(mensaje);
                campoEntrada.setText("");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Error al enviar el mensaje.");
                e.printStackTrace();
            }
        }
    }

}