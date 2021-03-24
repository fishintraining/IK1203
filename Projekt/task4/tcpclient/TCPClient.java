package tcpclient;
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class TCPClient {

    private static int BUFFERSIZE = 1024; // initiate the buffersize, as seen in lecture 4 pg.10
    private static int tcp_timeout = 2000; // initiate timeout for the tcp connection
    private static int TOTAL_STOP = 10000; //a max limit for large amount of data

    public static String askServer(String hostname, int port, String toServer) throws IOException {

        Socket tcp_client = new Socket(hostname, port);   //initiate the socket
        byte[] answer_buffer = new byte[BUFFERSIZE];      //set up a byte array as a buffer for the answer FROM the server
        byte[] question_buffer = new byte[BUFFERSIZE];    //set up a byte array as a buffer for the question TO the server
        StringBuilder bygg_text = new StringBuilder();    // SB for encoding


        //set the timer, found in Socket Class - https://docs.oracle.com/javase/7/docs/api/java/net/Socket.html
        tcp_client.setSoTimeout(tcp_timeout);

        //Encode the question from the buffer as seen in lecture 4 pg.19
        byte[] question = (toServer + "\r\n\"").getBytes(StandardCharsets.UTF_8);
        //Get client-outputstream and ask the server the question, as seen in lecture 4 pg.10
        tcp_client.getOutputStream().write(question, 0, question.length);

        try {
            int length;
            //Read the answer from the server, as seen in lecture 4 pg.10, while there is still input to read, or until our
            //choosen max-stop, keep adding the input bytes to the stringbuilder.
            while((length = tcp_client.getInputStream().read(answer_buffer)) != -1 && bygg_text.length() < TOTAL_STOP){
                //Decode the answer from the buffer as seen in lecture 4, and add the input to a stringbuilder.
                bygg_text.append(new String(answer_buffer, 0, length, StandardCharsets.UTF_8));
            }
        }
        catch (java.net.SocketTimeoutException e) {
            System.out.println("TIMEOUT");
        }

        tcp_client.close();                                                      //close the socket
        String the_answer = bygg_text.toString().trim();
        return the_answer;
    }

    public static String askServer(String hostname, int port) throws IOException {


        Socket tcp_client = new Socket(hostname, port);   //initiate the socket
        byte[] answer_buffer = new byte[BUFFERSIZE];      //set up a byte array as a buffer for the answer from the server
        StringBuilder bygg_text = new StringBuilder();    // SB for encoding

        //set the timer, found in Socket Class - https://docs.oracle.com/javase/7/docs/api/java/net/Socket.html
        tcp_client.setSoTimeout(tcp_timeout);

        try {
            int length;
            //Read the answer from the server, as seen in lecture 4 pg.10, while there is still input to read, or until our
            //choosen max-stop, keep adding the input bytes to the stringbuilder.
            while((length = tcp_client.getInputStream().read(answer_buffer)) != -1 && bygg_text.length() < TOTAL_STOP){
                //Decode the answer from the buffer as seen in lecture 4, and add the input to a stringbuilder.
                bygg_text.append(new String(answer_buffer, 0, length, StandardCharsets.UTF_8));
            }
        }
            catch (java.net.SocketTimeoutException e) {
                System.out.println("TIMEOUT");
            }

            tcp_client.close();                                                      //close the socket
            String the_answer = bygg_text.toString().trim();
            return the_answer;
    }
}
