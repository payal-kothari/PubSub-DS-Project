/**
 *
 * Server side for Secret Number game
 *
 * @author Payal Kothari    (pak4180@rit.edu)
 * @author Saurabh Gandhele (smg6512@rit.edu)
 *
 * This class handles server side message sending and receiving part
 *
 */
import javax.sound.midi.Soundbank;
import java.io.*;

import java.net.Socket;
import java.util.Random;

public class Server extends Thread {
    static  Socket socket;
    static String winner = null;
    private BufferedReader bufferedReader;
    public Server(Socket socket) {
        this.socket = socket;
    }
    public void run() {
        try {

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("****************Welcome to Guess the Number Game**************");
            out.println("****************You are required to guess a number between 1 - 50***************");
            out.println("****************Secret Number has been generated****************");
            out.println("****************Please enter your name with your guess*****************");


            DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            //System.out.println(randomNumber);
            //Thread.sleep(100);
            String temp = in.readUTF();

            System.out.println("received :"  + temp);
            NumberServer2.cnt++;
            if(NumberServer2.cnt==1)
            {
                //System.out.println("in first");
                NumberServer2.str1 = new String(temp);
                System.out.println(NumberServer2.str1);
                //System.out.println(NumberServer2.str1);
            }
            if(NumberServer2.cnt==2)
            {
                //System.out.println("in second");
                NumberServer2.str2 = new String(temp);
                System.out.println(NumberServer2.str2);
                //System.out.println(NumberServer2.str2);
                NumberServer2.m++;
            }

            //String s1 = new String(temp);
            if(NumberServer2.m==1)
            {

                String res = null;
                String [] tokens1=NumberServer2.str1.split(" ");
                String resname1 = tokens1[0];
                String resnum1 = tokens1[1];

                String [] tokens2=NumberServer2.str2.split(" ");
                String resname2 = tokens2[0];
                String resnum2 = tokens2[1];

                int num1 = Integer.parseInt(resnum1.trim());
                int num2 = Integer.parseInt(resnum2.trim());
                Random rand = new Random();
                int  randomNumber = rand.nextInt(50) + 1;
                int sub1 = Math.abs(randomNumber - num1);
                int sub2 = Math.abs(randomNumber - num2);

                if(sub1 < sub2){
                    winner = resname1;
                }
                else if(sub1 > sub2){
                    winner = resname2;
                }
                else{
                    winner = "Draw";
                }

                if(Server.winner != null)
                    for (Socket s : NumberServer2.listOfSockets) {
                        PrintWriter lat = new PrintWriter(s.getOutputStream(), true);
                        lat.println("-------------Secret number: " + randomNumber+"---------------");
                        lat.println("-------------"+resname1 + " guessed " + num1+"---------------");
                        lat.println("-------------"+resname2 + " guessed " + num2+"---------------");
                        if(winner == "Draw")
                        {
                            s.getOutputStream().write(("--------------It's a DRAW---------------").getBytes());
                        }
                        else
                            lat.println("--------------Winner is " + Server.winner +"-----------------");
                    }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
