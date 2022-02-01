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
        questionsList.add("7 - Liste os jogadores do Crystal Palace que não tem amarelos");
        questionsList.add("8 - Liste todos os defesas, que tenham mais de 2 golos marcados");
        questionsList.add("9 - Liste os jogadores Portugueses com mais de 10 golos e mais de 10 assistencias");
        questionsList.add("10 - Liste o clube com mais jogadores Portugueses");
        questionsList.add("11 - Qual o jogador com o melho ratio Assistencias/Golos (desde que tenham marcado mais que 1 golo e feito mais que uma assistencia)");

        queriesList.add("match (j:Player)-[:PLAYS_IN]-(c:Club{name: \"Manchester United\"}) return j.Name as nome, j.Age as idade, j.Matches as jogos, j.Goals as golos");
        queriesList.add("match (j:Player)-[:PLAYS_IN]-(c:Club{name: \"Chelsea\"}) return sum(toInteger(j.Goals)) as total_de_golos");
        queriesList.add("match (j:Player)-[:PLAYS_IN]->(c:Club) with c.name as clube, max(toInteger(j.Goals)) as maximo match (j:Player)-[:PLAYS_IN]->(c:Club) with toInteger(j.Goals) as golos, c.name as clube, j.Name as jogador where golos = maximo return clube, golos, jogador");
        queriesList.add("match (j:Player)-[:FROM]-(n:Country{nacionality:\"ESP\"})return j.Name as nome, toInteger(j.Assists) as assistencias order by assistencias desc limit 1");
        queriesList.add("match (j:Player) with j, toInteger(j.Yellow_Cards) as amarelos_jog return j.Name as nome, amarelos_jog order by amarelos_jog asc limit 1");
        queriesList.add("match (j:Player) with j, toInteger(j.Perc_Passes_Completed) as perc_passes return j.Name as nome, perc_passes order by perc_passes desc limit 1");
        queriesList.add("match (j:Player)-[:PLAYS_IN]->(c:Club{name: \"Crystal Palace\"}) with j, toInteger(j.Yellow_Cards) as amarelos_jog where amarelos_jog = 0 return j.Name as nome, j.Matches as jogos, j.Goals as golos, amarelos_jog");
        queriesList.add("match (j:Player)-[:ROLE]->(r:Position{name: \"DF\"}) where toInteger(j.Goals) >= 3 return j.Name, r.name");
        queriesList.add("match (p:Player)-[:FROM]->(c:Country{nacionality: \"POR\"}) where toInteger(p.Goals) > 10 and toInteger(p.Assists) > 10 return p.Name as nome, c.nacionality as nacionalidade, toInteger(p.Goals) as golos, toInteger(p.Assists) as assistencias");
        queriesList.add("match (q:Club)<-[:PLAYS_IN]-(p:Player)-[:FROM]->(c:Country{nacionality:\"POR\"}) with q, count(p.Name) as portugueses_clube return q.name as clube, portugueses_clube order by portugueses_clube desc limit 1");
        queriesList.add("match (p:Player) where toInteger(p.Goals) > 1 and toInteger(p.Assists) > 1 with p,(toInteger(p.Assists)/toInteger(p.Goals)) as ratio return p.Name, toInteger(p.Assists) as assistencias, toInteger(p.Goals) as golos , ratio order by ratio desc limit 1");
        
        try (DriverNeo4j greeter = new DriverNeo4j("bolt://localhost:7687", "neo4j", "123")) {
            //insert data on database
            String insert = "LOAD CSV WITH HEADERS FROM \"file:///EPL_20_21.csv\" AS row " +
            "MERGE (p:Player {Name: row.Name, Age: row.Age, Matches: row.Matches, Starts: row.Starts, Goals: row. Goals, Assists: row.Assists, Passes_Attempted: row.Passes_Attempted, Perc_Passes_Completed: row.Perc_Passes_Completed, Penalty_Goals: row.Penalty_Goals, xG: row.xG, xA: row.xA, Yellow_Cards: row.Yellow_Cards, Red_Cards: row.Red_Cards}) " +
            "MERGE (c:Club {name: row.Club}) " + 
            "MERGE (n:Country {nacionality: row.Nationality}) " +
            "MERGE (q:Position {name: row.Position}) " +
            "MERGE (p)-[:PLAYS_IN]->(c) " +
            "MERGE (p)-[:FROM]->(n) " +
            "MERGE (p)-[:ROLE]->(q);";
            greeter.runQuery(insert);

            try (PrintWriter out = new PrintWriter(new File("CBD_L44c_output.txt"))) {
                int i = 0;
                for (String query : queriesList) {
                    System.out.println(questionsList.get(i));
                    out.println(questionsList.get(i));
                    for (Record result : greeter.runQuery(query)) {
                        System.out.println(result);
                        out.println(result);
                    }
                    System.out.println();
                    out.println();
                    i++;
                }
                greeter.close();
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