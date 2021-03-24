import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import tcpclient.TCPClient;


public class MyRunnable implements Runnable {

    static int BUFFERSIZE = 1024;
    static int MAXSIZE = 10000;
    Socket connectionSocket;

    public MyRunnable(Socket connectionSocket){    //parameter for runnable
        this.connectionSocket = connectionSocket;
    }

    public void run(){  //run with the above parameter
        System.out.println("Hello from a thread!");

        try {
            
            String[] uri_value;

            while (true) {

                StringBuilder message = new StringBuilder();
                byte[] fromClient = new byte[BUFFERSIZE];
                int length;
                boolean status = true;
                String hostname = null;
                int port = -1;
                String inputString = null;
                String clientAnswer = null;
                String HTTP_status = null;
                StringBuilder answer = new StringBuilder();


                // read all the encoded input from the client or until MAXSIZE.
                while (connectionSocket.getInputStream().available() > 0 && message.length() < MAXSIZE) {
                    length = connectionSocket.getInputStream().read(fromClient);
                    //decode the input-stream, lecture 4.
                    message.append(new String(fromClient, 0, length, StandardCharsets.UTF_8));

                    if (status) {
                        //split the message from the browser into parts, as seen in https://docs.oracle.com/javase/7/docs/api/java/lang/String.html#split(java.lang.String)
                        String uri = message.toString();
                        uri_value = uri.split(" |=|&");
                        int wordcount = uri_value.length;
                        System.out.println("WORDCOUNT: " + wordcount);

                        //check for error senarios
                        if(uri_value[1].matches("(?i).*/favicon.ico.*") || !(uri_value[1].matches("(?i).*/ask?.*")))
                        {
                            HTTP_status = "HTTP/1.1 404 Not Found\r\n\r\n";
                        }
                        else if ((uri_value[0].equals("GET")) && (uri_value[1].matches("(?i).*/ask?.*"))) {
                            HTTP_status = "HTTP/1.1 200 OK\r\n\r\n";
                        } else {
                            HTTP_status = "HTTP/1.1 400 Bad Request\r\n\r\n";
                        }

                        //extract the values from the URI
                        hostname = uri_value[2];
                        port = Integer.parseInt(uri_value[4]);
                        System.out.println("port: " + port);
                        System.out.println("hostnamn: " + hostname);

                        if (wordcount > 6) {
                            inputString = uri_value[6];
                            System.out.println("INPUT-STRING: " + inputString);
                        }
                        status = false;
                    }
                }

                try {
                    //Send the extracted values to the TCPClient and save the answer as ClientAnswer
                    System.out.println("INPUT-STRING: " + inputString);

                    if (inputString != null)
                        clientAnswer = TCPClient.askServer(hostname, port, inputString);
                    else
                        clientAnswer = TCPClient.askServer(hostname, port);

                    System.out.println("TCP SVAR: " + clientAnswer);

                } catch (IOException error) {
                    System.err.println("det är ett error");
                }

                //Make a string from the status and the answer from TCPClient.
                answer.append(HTTP_status);
                answer.append(clientAnswer).append("\r\n");
                String reply = answer.toString();
                System.out.println("THIS IS THE TCPCLient REPLY: " + reply);

                //encode the output-stream and write back to the outputstream, lecture 4.
                byte[] toClient = reply.getBytes(StandardCharsets.UTF_8);
                connectionSocket.getOutputStream().write(toClient);
                connectionSocket.close();
            }

        } catch (Exception e) {
            System.out.print("ERROR");
        }
    }
}

