package com.cbd;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;

public class SimplePost {

    public static void main(String[] args) {
        // Set
        SimplePostSet board = new SimplePostSet();// set some users
        String[] users = { "Ana", "Pedro", "Maria", "Luis" };
        for (String user : users)
            board.saveUser(user);
        board.getAllKeys().stream().forEach(System.out::println);
        board.getUser().stream().forEach(System.out::println);

        // List
        SimplePostList boardList = new SimplePostList();
        String[] usersList = { "Aninha", "Pedrinho", "Mariazinha", "Luisinho" };
        for (String user : usersList) {
            boardList.saveUser(user);
        }
        boardList.getAllKeys().stream().forEach(System.out::println);
        boardList.getUser().stream().forEach(System.out::println);

        // HashMap
        SimplePostHashMap boardMap = new SimplePostHashMap();

        Map<String, String> userMap = new HashMap<>();
        userMap.put("chave1", "João");
        userMap.put("chave2", "Tiago");
        userMap.put("chave3", "Serafim");
        userMap.put("chave4", "José");
        
        boardMap.saveUser(userMap);
        
        boardMap.getAllKeys().stream().forEach(System.out::println);

    }

}

class SimplePostSet {
    private Jedis jedis;

    public static String USERS = "usersSet"; // Key set for users' name

    public SimplePostSet() {
        this.jedis = new Jedis("localhost");
    }

    public void saveUser(String username) {
        jedis.sadd(USERS, username);
    }

    public Set<String> getUser() {
        return jedis.smembers(USERS);
    }

    public Set<String> getAllKeys() {
        return jedis.keys("*");
    }

}

class SimplePostList {
    private Jedis jedis;
    public static String USERS = "usersList";

    public SimplePostList() {
        this.jedis = new Jedis("localhost");
    }

    public void saveUser(String username) {
        jedis.rpush(USERS, username);
    }

    public List<String> getUser() {
        return jedis.lrange(USERS, 0, -1);
    }

    public Set<String> getAllKeys() {
        return jedis.keys("*");
    }

}

class SimplePostHashMap {
    private Jedis jedis;
    public static String USERS = "usersMap";

    public SimplePostHashMap() {
        this.jedis = new Jedis("localhost");
    }

    public void saveUser(Map<String, String> username) {
        jedis.hmset(USERS, username);
    }

    public Map<String, String> getUser() {
        return jedis.hgetAll(USERS);
    }

    public Set<String> getAllKeys() {
        return jedis.keys("*");
    }
}
