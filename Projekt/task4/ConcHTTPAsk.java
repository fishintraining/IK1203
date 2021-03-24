import java.net.*;
import java.io.*;

public class ConcHTTPAsk {

    public static void main(String[] args) throws IOException {

        //check if the input contains a port number
        int PORT = -1;      //cant create a socket with a negative number
        if (args.length > 0) {
            PORT = Integer.parseInt(args[0]);
        }
        else{
            PORT = 8888; //default
        }

        try {

            ServerSocket mySocket = new ServerSocket(PORT); //create a new socket with the port nr in arg[0], or 8888.

            while(true)
            {
                System.out.println("waiting for the server: ");
                Socket concurrent_connectionSocket = mySocket.accept();

                MyRunnable runnableObject = new MyRunnable(concurrent_connectionSocket); // as seen in https://stackoverflow.com/questions/877096/how-can-i-pass-a-parameter-to-a-java-thread

                new Thread(runnableObject).start();  //start a new thread
            }
        }
        catch(IOException error) {
            System.err.println("IOException ERROR");
        }
    }
}

