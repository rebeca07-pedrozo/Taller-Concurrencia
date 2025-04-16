package cliente;

import org.junit.jupiter.api.*;
import javax.swing.*;

public class ClienteGUITest {

    @Test
    public void testActualizarListaUsuarios() {
        ClienteGUI clienteGUI = new ClienteGUI() {
            @Override
            protected void conectar() {
            }
        };

        String mensaje = "/usuarios user1 user2 user3";
        clienteGUI.actualizarListaUsuarios(mensaje);

        DefaultListModel<String> modelo = (DefaultListModel<String>) clienteGUI.listaUsuarios.getModel();

        Assertions.assertEquals(3, modelo.getSize());
        Assertions.assertEquals("user1", modelo.getElementAt(0));
        Assertions.assertEquals("user2", modelo.getElementAt(1));
        Assertions.assertEquals("user3", modelo.getElementAt(2));
    }
}
