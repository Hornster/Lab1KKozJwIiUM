package pl.polsl.clientside;

import java.io.IOException;
import java.net.Socket;

public interface IConnectionManager {
    /**
     * Sends message to server through established connection.
     * @param message Message to send.
     */
    void sendMessage(String message);

    /**
     * Awaits response from the server.
     * @return Message provided by the server.
     */
    String retreiveMessage() throws IOException;
    /**
     * Connect client to server.
     * @return Socket with connection information. NULL if not managed to connect.
     */
    Socket connectToServer() throws IOException;

    /**
     * Set the server address which the client will attempt to connect to.
     * @param address The server address.
     */
    void setServerAddress(String address);

    /**
     * Set the server port which the client will attempt to connect to.
     * @param serverPort New server port.
     */
    void setServerPort(int serverPort);
}
