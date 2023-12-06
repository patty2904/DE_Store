package Application;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.Socket;

public class DE_STORE_Client implements Serializable {

    private static final long serialVersionUID = 1L;
	private Socket clientSocket;
    private DataOutputStream dataOutputStream;
    private ObjectInputStream objectInputStream;

    // Parameterless constructor
    public DE_STORE_Client() {
       
    }

    // Starts connection with server
    public void startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
            objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
            System.out.println("Connected to server!");
        } catch (IOException e) {
            System.out.println("Error connecting to server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Send request to server
    public Object sendRequest(String requestType, String arg) throws IOException, ClassNotFoundException {
        sendData(requestType, arg);
        return receiveData();
    }

    // Send data to server
    private void sendData(String requestType, String arg) throws IOException {
        System.out.println("Sending Request to the ServerSocket");
        dataOutputStream.writeUTF(requestType);
        dataOutputStream.flush();

        if (arg != null) {
            dataOutputStream.writeUTF(arg);
            dataOutputStream.flush();
        }
    }

    // Receive data back
    private Object receiveData() throws IOException, ClassNotFoundException {
        System.out.println("Receiving response from server");
        return objectInputStream.readObject();
    }

    // Close the connection
    public void stopConnection() {
        try {
            System.out.println("Closing socket.");
            if (clientSocket != null) {
                clientSocket.close();
            }
            if (dataOutputStream != null) {
                dataOutputStream.close();
            }
            if (objectInputStream != null) {
                objectInputStream.close();
            }
        } catch (IOException ex) {
            System.out.println("Error closing connection: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}