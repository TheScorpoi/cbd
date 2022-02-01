package com.cbd;

import java.util.List;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

public class DriverNeo4j implements AutoCloseable {
    private final Driver driver;

    public DriverNeo4j(String uri, String user, String password) {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    public List<Record> runQuery(String query) {
        // https://neo4j.com/docs/api/java-driver/current/org/neo4j/driver/Result.html
        try (Session session = driver.session()) {
            List<Record> greeting = session.writeTransaction((tx -> {
                Result result = tx.run(query);
                return result.list();
            }));
            return greeting;
        }
    }

    @Override
    public void close() throws Exception {
        driver.close();
    }
}