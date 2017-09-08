

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.io.PrintWriter;

public class NumberServer2 {
    static String tokens = new String();
    static String str1 = null;
    static String str2 = null;
    static NumberServer2 serverSocket;
    static List<Socket> listOfSockets = new ArrayList<Socket>();

    static StringBuilder[] res = new StringBuilder[100];
    static int cnt=0;
    static int m=0;

    public static void main(String[] a) throws InterruptedException, IOException {
        ServerSocket serverSocket = new ServerSocket(8048);
        try {
            while (true) {
                Socket s = serverSocket.accept();
                listOfSockets.add(s);
                new Server(s).start();
                PrintWriter out = new PrintWriter(Server.socket.getOutputStream(), true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally
        {
            serverSocket.close();
        }

    }
}
