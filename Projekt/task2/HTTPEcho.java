import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class HTTPEcho {
    static int BUFFERSIZE = 1024;
    static int MAXSIZE = 10000;

    public static void main(String[] args) throws IOException {

        //check if the input contains a port number
        int PORT = -1;      //cant create a socket with a negative number
        try {
            PORT = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.print("ERROR - No port number");
        }

        try {
            //opening a serversocket to the Integer port number at args[0], aka the first number. lecture 4, pg 21.
            ServerSocket HelloServer = new ServerSocket(PORT);

            while (true) {
                //accept socket, lecture 4 pg 21.
                Socket connect_Socket = HelloServer.accept();


                StringBuilder message = new StringBuilder();
                byte[] fromClient = new byte[BUFFERSIZE];
                int length;
                String Header = "HTTP/1.1 200 OK\r\n\r\n";


                // read all the encoded input from the client or until MAXSIZE.
                while ( connect_Socket.getInputStream().available() > 0 && message.length() < MAXSIZE) {
                    length = connect_Socket.getInputStream().read(fromClient);
                    //decode the input-stream, lecture 4.
                    message.append(new String(fromClient, 0, length, StandardCharsets.UTF_8));

                }

                String response = (Header + message.toString());

                //encode the output-stream and write back to the client, lecture 4.
                byte[] toClient = response.getBytes(StandardCharsets.UTF_8);
                connect_Socket.getOutputStream().write(toClient);
                connect_Socket.close();
            }
        }
        catch(Exception e){
            System.out.print("ERROR");
        }
    }
}