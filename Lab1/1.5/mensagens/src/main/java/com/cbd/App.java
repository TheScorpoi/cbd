package com.cbd;

import redis.clients.jedis.Jedis;
import java.io.*;
import java.util.*;

public class App {
    
    static PostList postList = new PostList();
    static PostHash postHash = new PostHash();
    static PostSet postSet = new PostSet();
    
    static Map<String, String> subscriptionMap = new HashMap<String, String>();
    static Map<String, String> userMap = new HashMap<String, String>();
    static List<String> followeList= new ArrayList<String>();
    
    static int messages_user_count = 1;
    static int user_count = 1;
    
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);

        do {
            String menu = "Welcome! \n" +
                    "1 - Create User \n" +
                    "2 - Send Message \n" +
                    "3 - Subscribe User \n" +
                    "4 - Check User Messages\n" +
                    "5 - Exit";
            
                System.out.println(menu);

            int input = sc.nextInt();

            switch (input) {
                case 1:
                    adicionarUtilizadores();
                    break;
                case 2:
                    associacaoUtilizadores();
                    break;
                case 3:
                    enviarMensagens();
                    break;

                case 4:
                    lerMensagens();
                    break;
            }
        } while (sc.nextInt() != 5);
    }

    public static void adicionarUtilizadores() {
        System.out.println("Username?");
        
        Scanner sc = new Scanner(System.in);
        String username = sc.nextLine();

        if (!userMap.containsKey(username)) {
            userMap.put("user" + String.valueOf(user_count), username);
            postHash.saveUser(userMap);
            user_count++;
        }
        System.out.println(postHash.getUserSet());
    }

    public static void associacaoUtilizadores() {
        Scanner sc1 = new Scanner(System.in);
        Scanner sc2 = new Scanner(System.in);

        System.out.println(postHash.getUserSet());

        System.out.println("User?");
        String user = sc1.nextLine();

        System.out.println("Message?");
        String message = sc2.nextLine();

        postList.saveMessage(user, message);
        System.out.println(postList.getMessageSet(user));
    }


    public static void enviarMensagens() {
        Scanner sc = new Scanner(System.in);

        System.out.println(postHash.getUserSet());

        System.out.printf("Which user are you? ");
        String user = sc.nextLine();

        System.out.println(postHash.getUserSet());

        System.out.println("User to subscribe? ");
        String userToSubscribe = sc.nextLine();

        postSet.saveFollower(user, userToSubscribe);   
    }

    public static void lerMensagens() {
        Scanner sc = new Scanner(System.in);

        System.out.printf("Which user are you? ");
        String user = sc.nextLine();

        Set<String> following = postSet.getFollowerSet(user);
        if (following.size() == 0)
            System.out.println("You don't follow anyone");
        else {
            for (String s: following) {
                System.out.println("Message from " + s + ":");
                List<String> msgs = postList.getMessageSet(s);
                for(String m: msgs)
                    System.out.println(m);
            }
        }
    }
}