package com.cbd;

import redis.clients.jedis.Jedis;
import java.util.Set;
import java.util.List;

public class PostList {
    private Jedis jedis;

    public PostList() {
        this.jedis = new Jedis("localhost");
    }

    public void saveMessage(String user, String username) { jedis.lpush(user, username); }

    public List<String> getMessageSet(String user) { return jedis.lrange(user, 0, -1); }

    public Set<String> getAllKeys() { return jedis.keys("*"); }
}