
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * Client side for Secret Number game
 *
 * @author Payal Kothari    (pak4180@rit.edu)
 * @author Saurabh Gandhele (smg6512@rit.edu)
 *
 * This class handles client side
 *
 */


public class Client extends Thread {
    public static void main(String[] a) {
        Socket socket = null;
        BufferedReader in;
        DataOutputStream out  ;
        int cnt=0;

        try {
            socket = new Socket("localhost", 8048);
            InputStream inputStream = socket.getInputStream();

            while (true) {

                byte[] bytes = new byte[1000];
                inputStream.read(bytes);					// reads data from server
                String s = new String(bytes);
                System.out.println(s);
                cnt++;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(cnt == 1){
                    System.out.println("Write name and number");
                    Scanner scanner = new Scanner(System.in);
                    String name = scanner.nextLine();
                    String num = scanner.nextLine();
                    String result = name + " " + num;
                    System.out.println("Result : " + result);
                    out = new DataOutputStream(socket.getOutputStream());
                    out.writeUTF(result);

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
