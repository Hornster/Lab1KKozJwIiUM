package pl.polsl.clientside;

import java.io.*;
import java.net.Socket;

public class ConnectionManager implements Closeable, IConnectionManager {
    private int serverPort;
    private Socket clientSocket;

    private PrintWriter output;
    private BufferedReader input;
    private String serverAddress;

    /**
     * Retrieves input and output streams for the socket.
     * @param socket Socket for which the IO streams are retrieved.
     * @throws IOException If unable to retrieve either input or output stream from the socket.
     */
    private void iniIO(Socket socket) throws IOException
    {
        output = new PrintWriter(
                new BufferedWriter(
                        new OutputStreamWriter(
                                socket.getOutputStream())), true);
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    /**
     * If the socket is bound to, tries to close it.
     * @throws IOException If unable to close the socket and connection associated with it.
     */
    @Override
    public void close() throws IOException {
        /*if(serverSocket.isBound()) {
            serverSocket.close();
        }*/
        if(clientSocket != null) {
            if (clientSocket.isConnected() && clientSocket.isBound()) {
                clientSocket.close();
            }
        }
    }

    @Override
    public void sendMessage(String message) {
        output.print(message);
        output.flush();
    }

    @Override
    public String retreiveMessage() throws IOException {
        return input.readLine();
    }

    @Override
    public Socket connectToServer() throws IOException {
        close();
        if(serverPort > 0 && serverAddress != null) {
            clientSocket = new Socket(serverAddress, serverPort);
            iniIO(clientSocket);
            return clientSocket;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setServerAddress(String address) {
        serverAddress = address;
    }

    @Override
    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }
}


//TODO create implementation of client-side TCP protocol.