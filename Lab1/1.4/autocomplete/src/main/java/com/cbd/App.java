package com.cbd;

import java.io.*;
import java.util.*;
import redis.clients.jedis.Jedis;


public class App 
{
    public static void main( String[] args )
    {
        PostSet boardSet = new PostSet();

        try (Scanner input = new Scanner(new File("names.txt"))) {

            while (input.hasNext()) {
                String name = input.next();
                boardSet.saveUser(name);
            }

            Scanner sc = new Scanner(System.in);
            while(true) {
                System.out.print("Search for ('Enter for quit'): ");
                String search_name = sc.nextLine();
                if (search_name.length() == 0) {
                    break;
                }

                Set<String> answers = boardSet.getUser(search_name);
                System.out.println(search_name);
                for(String answer : answers) {
                    System.out.println(answer);
                }
                System.out.println();

            }
            
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
            System.exit(0);
        }

        // alinea b)
        PostCsv boardCsv = new PostCsv();
        try (Scanner input2 = new Scanner(new File("nomes-pt-2021.csv"))) {

            while(input2.hasNext()) {
                String[] row = input2.next().split(";");
                boardCsv.saveUser(row[0], Integer.parseInt(row[1]));
            }

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print("Search for ('Enter for quit'): ");
                String search_name = scanner.nextLine();
                if (search_name.length() == 0) {
                    break;
                }

                Set<String> answers = boardCsv.getUser();
                for(String answer : answers) {
                    System.out.println(answer);
                }
                System.out.println();
            }
            
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
            System.exit(0);
        }
    }
}

class PostSet {
    private Jedis jedis;
    public static String USERS = "names";

    public PostSet() {
        this.jedis = new Jedis("localhost");
    }

    public void saveUser(String username) {
        jedis.zadd(USERS, 0, username);
    }

    public Set<String> getUser(String search) {
        return jedis.zrangeByLex(USERS, "[" + search + "*", "[" + search + (char)0xFF);
    }

    public Set<String> getAllKeys() {
        return jedis.keys("*");
    }
}

class PostCsv {
    
    private Jedis jedis;
    public static String USERS = "names_csv";

    public PostCsv() {
        this.jedis = new Jedis("localhost");
    }

    public void saveUser(String username, int score) {
        jedis.zadd(USERS, score,username);
    }

    public Set<String> getUser() {
        return jedis.zrevrange(USERS, 0, -1);
    }

    public Set<String> getAllKeys() {
        return jedis.keys("*");
    }

    public void flushAll() {
        jedis.flushAll();
    }
}