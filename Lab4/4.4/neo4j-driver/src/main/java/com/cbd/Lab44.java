package com.cbd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.neo4j.driver.Record;

public class Lab44 {

    public static void main(String... args) throws Exception {

        List<String> queriesList = new ArrayList<>();
        List<String> questionsList = new ArrayList<>();

        questionsList.add("1 - Listar todos os jogadores que jogam no Manchester United");
        questionsList.add("2 - Conte o total de golos que o Chelsea marcou");
        questionsList.add("3 - Liste o jogador com mais golos por clube");
        questionsList.add("4 - Liste o jogador espanhol com mais assitências");
        questionsList.add("5 - Liste o jogador com menos cartões amarelos");
        questionsList.add("6 - Liste o jogador com mais passes efetuados com sucesso");
        questionsList.add("7 - ");
        questionsList.add("8 - ");
        questionsList.add("9 - ");
        questionsList.add("10 - ");
        questionsList.add("11 - ");

        queriesList.add("match(j:Player)-[:PLAYS_IN]-(c:Club{name: \"Manchester United\"}) return j.Name as nome, j.Age as idade, j.Matches as jogos, j.Goals as golos");
        queriesList.add("match(j:Player)-[:PLAYS_IN]-(c:Club{name: \"Chelsea\"}) return sum(toInteger(j.Goals)) as total_de_golos");
        queriesList.add("MATCH (p:Player{Name: \"Alex Telles\"}) RETURN p.Name AS name, p.Age AS age");
        queriesList.add("match(j:Player)-[:FROM]-(n:Country{nacionality:\"ESP\"})with j,  max(j.Goals) as golos return j.Name as nome, golos");
        queriesList.add("MATCH (p:Player{Name: \"Alex Telles\"}) RETURN p.Name AS name, p.Age AS age");
        queriesList.add("MATCH (p:Player{Name: \"Alex Telles\"}) RETURN p.Name AS name, p.Age AS age");
        queriesList.add("MATCH (p:Player{Name: \"Alex Telles\"}) RETURN p.Name AS name, p.Age AS age");
        queriesList.add("MATCH (p:Player{Name: \"Alex Telles\"}) RETURN p.Name AS name, p.Age AS age");
        queriesList.add("MATCH (p:Player{Name: \"Alex Telles\"}) RETURN p.Name AS name, p.Age AS age");
        queriesList.add("MATCH (p:Player{Name: \"Alex Telles\"}) RETURN p.Name AS name, p.Age AS age");
        queriesList.add("MATCH (p:Player{Name: \"Alex Telles\"}) RETURN p.Name AS name, p.Age AS age");

        try (DriverNeo4j greeter = new DriverNeo4j("bolt://localhost:7687", "neo4j", "123")) {
            try (PrintWriter out = new PrintWriter(new File("CBD_L44c_output.txt"))) {
                int i = 0;
                for (String query : queriesList) {
                    System.out.println(questionsList.get(i));
                    out.println(questionsList.get(i));
                    for (Record result : greeter.printGreeting(query)) {
                        System.out.println(result);
                        out.println(result);
                    }
                    System.out.println();
                    out.println();
                    i++;
                }
            } catch (FileNotFoundException e) {
                System.err.println("ERROR: " + e.getMessage());
                System.exit(0);
            }
        } catch (Exception e) {
            System.err.println("ERROR: Connecting to Neo4j " + e.getMessage());
            System.exit(1);
        }
    }
}
