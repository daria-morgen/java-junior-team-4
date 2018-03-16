package com.edu.jet.client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Aleksey Bondarenko
 */
//TODO имплементировать runble
public class Client {
    private final String IP_ADDRESS;
    private final int PORT;

    public Client() {
        IP_ADDRESS = "localhost";
        PORT = 7778;
    }

    public Client(String ipAddress, int port) {
        IP_ADDRESS = ipAddress;
        PORT = port;
    }

    //TODO постараться сделать метод компактнее
    public void startRunning() {
        String userInputMessageLine;
        String messageToServer;

        try (Socket clientSocket = new Socket(IP_ADDRESS, PORT);
             ObjectOutputStream clientOutputStream =
                     new ObjectOutputStream(clientSocket.getOutputStream());
//              ObjectInputStream clinentInputStream =
//                     new ObjectInputStream(clientSocket.getInputStream())
            ) {

            try (BufferedReader clientConsoleReader = new BufferedReader(new InputStreamReader(System.in))) {

                while (!(userInputMessageLine = clientConsoleReader.readLine()).equals("/quit")) {
                    System.out.println("in while != quit");
                    if (userInputMessageLine.length() > 150) {
                        System.out.println("Your message must be less than 150 characters");
                        continue;
                    }

                    //TODO make check with regexp
                    if (!(userInputMessageLine.toLowerCase().contains("/snd")) ||
                            userInputMessageLine.toLowerCase().contains("/hist")) {
                        System.out.println("Illegal command. Try again please");
                        continue;
                    }

                    //TODO сделать обработку попытки отправить пустое сообщение
                    //TODO вынести reader writer за клиента? возложить обязанность на конекшн

                    messageToServer = getTime() + userInputMessageLine.substring(4);
                    clientOutputStream.writeObject(messageToServer);
                  //  System.out.println(clinentInputStream.readObject().toString());
                   // System.out.println(clinentInputStream.readObject().toString());


                    //TODO обсудить еще раз протокол передачи. Возможно стоит передавать через writeUTF()
                   // getaVoid(messageToServer, clientOutputStream);

                    //TODO убрать перед пушем в мастер
                   // System.out.println(messageToServer);
                }

                System.out.println("You have left the chat");
            } catch (IOException e) {
                //e.printStackTrace();
                System.out.println("IO error has occurred");
            }
        } catch (UnknownHostException e) {
            System.out.println("Unknown Host. Connection failed");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IO error has occurred");
        }
//        } catch (ClassNotFoundException e) {
//            System.out.println("Object for pribt not found");
//           // e.printStackTrace();
//        }
    }

//    private synchronized void getaVoid(String messageToServer, ObjectOutputStream clientOutputStream) throws IOException {
//        clientOutputStream.writeObject(messageToServer);
//    }

    //TODO обсудить с заказчиком формат даты
    private String getTime() {
        SimpleDateFormat date = new SimpleDateFormat("'['dd.MM.yyyy HH:mm']'");
//        DateFormat date = DateFormat.getDateInstance(DateFormat.FULL, Locale.getDefault());
        return date.format(new Date());
    }
}