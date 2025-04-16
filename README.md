Proyecto: Chat Cliente-Servidor Multicliente
Fecha del análisis: 13/04/2025
Versión: 1.0

Estas son tres de las recomendaciones de seguridad que arrojo SonarQube 

1. Validación insuficiente de entrada del usuario
Ubicación: Cliente.java / JFrameForm.java – Método que envía mensajes al servidor
Severidad: Alta
Descripción:
La entrada del usuario no está siendo sanitizada antes de enviarse al servidor. Esto puede permitir ataques como inyección de comandos, XSS reflejado si el mensaje es renderizado en una interfaz web, o incluso provocar fallos si se manipulan comandos del protocolo.
Recomendación:
Implementar validaciones OWASP, por ejemplo:
```
mensaje = mensaje.replaceAll("[<>]", ""); 
```

2. Uso de sockets sin cifrado
Ubicación: Servidor.java, Cliente.java – Comunicación entre cliente y servidor

Severidad: Crítica

Descripción:
El sistema usa Socket y ServerSocket sin ningún cifrado, lo que expone todos los mensajes a interceptación en red (sniffing). Esto es especialmente grave si se transmite información sensible como nombres o contraseñas.

Recomendación:
Migrar a SSLSocket y SSLServerSocket, o implementar una capa TLS usando certificados autofirmados durante el desarrollo:
```
SSLServerSocketFactory factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
SSLServerSocket serverSocket = (SSLServerSocket) factory.createServerSocket(1234);
```
3. Manejo adecuado de excepciones
Ubicación: ClienteGUI.java – Método conectar(), Método enviarMensaje()

Severidad: Media

Descripción: El sistema maneja las excepciones de manera inapropiada, mostrando los detalles de las excepciones al usuario a través de printStackTrace() y mensajes genéricos como "Error al conectar con el servidor". Esto puede exponer información sensible de la aplicación, como el tipo de excepción y la pila de ejecución, lo que puede ser explotado por un atacante para identificar vulnerabilidades.
Recomendación: Utilizar un sistema adecuado de manejo de excepciones que no exponga detalles internos al usuario. En su lugar, registrar los errores en un archivo de logs y mostrar mensajes genéricos o amigables al usuario. También se recomienda utilizar un framework de logging como java.util.logging o SLF4J para asegurar una correcta gestión de errores. 

```
private static final Logger logger = Logger.getLogger(ClienteGUI.class.getName());

private void conectar() {
    try {
        socket = new Socket("localhost", 12345);
        entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        salida = new PrintWriter(socket.getOutputStream(), true);
    } catch (IOException e) {
        logger.log(Level.SEVERE, "Error al conectar al servidor", e);
        JOptionPane.showMessageDialog(frame, "Hubo un error al intentar conectarse al servidor. Por favor, intente más tarde.");
    }
}
 ```

Imagenes de interfaz 

![Image](https://github.com/user-attachments/assets/ba80b91d-f007-4e5b-ae72-d06ef3662d39)

![Image](https://github.com/user-attachments/assets/9e688ee6-a702-4456-85d5-3246f7d1e164)
![Image](https://github.com/user-attachments/assets/28b52e02-5cce-41b3-a04b-2dcc5763bcea)

![Image](https://github.com/user-attachments/assets/d3b1e2cd-e73d-4b9f-8995-69b4716a5e86)
![Image](https://github.com/user-attachments/assets/c1c8ddd1-728a-479a-a896-c7c3fae17a92)
