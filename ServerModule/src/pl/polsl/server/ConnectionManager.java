package pl.polsl.server;

import java.io.*;
import java.net.*;

public class ConnectionManager implements Closeable {
    private final String endSequence = "#END$";
    private int serverPort;
    private ServerSocket serverSocket;
    private Socket clientSocket;

    private PrintWriter output;
    private BufferedReader input;

    public ConnectionManager(int serverPort)
    {
        this.serverPort = serverPort;
    }

    /**
     * Creates a socket using provided during instantiation server port.
     * @throws IOException When unable to set uop a socket at provided port.
     */
    public void startServer() throws IOException
    {
        serverSocket = new ServerSocket(serverPort);
    }

    /**
     * Awaits for new connection. Holds the thread.
     * @return Reference to socket which the clientside got connected to, ready to work. Null if the server was not started with startServer method.
     * @throws IOException If not able to establish connection with clientside.
     */
    public Socket awaitConnection() throws IOException
    {
        if(serverSocket == null)
        {
            return null;
        }
        else {
            Socket socket = serverSocket.accept();

            iniIO(socket);
            clientSocket = socket;
            return socket;
        }
    }

    /**
     * Sends string with answer to the clientside bound to socket.
     * @param answer String with answer.
     */
    public void SendAnswer(String answer)
    {
        output.println(answer + endSequence);
        output.flush();
    }

    /**
     * Retrieves a command from the socket.
     * @return String containing a command from the clientside socket.
     * @throws IOException If not able to read from the clientside.
     */
    public String RetrieveCommand()throws IOException
    {
        return input.readLine();
    }
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
                                socket.getOutputStream())), false);
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    /**
     * If the socket is bound to, tries to close it.
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        /*if(serverSocket.isBound()) {
            serverSocket.close();
        }*/
        if(clientSocket.isConnected() && clientSocket.isBound())
        {
            clientSocket.close();
        }
    }

    public void printWelcome() {
        System.out.println("Greetings, Master! What is your wish today?");
    }
}
//TODO setup this class for client-side