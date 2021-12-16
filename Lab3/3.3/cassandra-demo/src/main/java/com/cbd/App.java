package com.cbd;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public class App {
    static Session session;
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static void main(String[] args) {

        try {

            String serverIP = "127.0.0.1";
            String keyspace = "videos_bd";

            Cluster cluster = Cluster.builder().addContactPoints(serverIP).build();

            session = cluster.connect(keyspace);

            alineaA();
            alineaB();

        } catch (Exception e) {
            System.err.println("ERROR: Connection with DB connection failed.\n" + e.getMessage());
            System.exit(1);
        }
    }
    
    public static void alineaA() {
        //insertData();
        //searchData();
        updateData();
    }

    public static void alineaB() {
        String query = "SELECT * FROM video WHERE autor_partilha = 'antonio123' AND data_upload = '2021-12-06 02:19:59.623000+0000'";
        System.out.println(ANSI_GREEN + "Videos partilhados por um determinado utilizador num determinado periodo de tempo." + ANSI_RESET);
        for (Row r : session.execute(query)) {
            System.out.println(r);
        }

        String query2 = "SELECT * FROM evento WHERE user = 'XjoaoX' and video_id = 8a28dc90-5632-11ec-b1cd-85f5958119b0";
        System.out.println(ANSI_GREEN + "\nOs ultimos 5 eventos de determinado video realizado por um utilizador." + ANSI_RESET);

        for (Row r : session.execute(query2)) {
            System.out.println(r);
        }

        String query3 = "SELECT tag FROM video_pelo_id where id = 8a393040-5632-11ec-b1cd-85f5958119b0";
        System.out.println(ANSI_GREEN + "\nListar as tags de determinado video." + ANSI_RESET);
        for (Row r : session.execute(query3)) {
            System.out.println(r);
        }

        String query4 = "SELECT * FROM comentarios_video LIMIT 3";
        System.out.println(ANSI_GREEN + "\nOs ultimos 3 comentários introduzidos para um video." + ANSI_RESET);
        for (Row r : session.execute(query4)) {
            System.out.println(r);
        }
    }

    public static void insertData() {
        String cqlStatement1 = "INSERT INTO video_pelo_id (id, titulo, descricao, tag, data_upload, autor_partilha) VALUES (now(), 'Fiz um INSERT pela aplicação Java (DEU CERTO?)', 'Como inserir dados em Cassandra', {'cassandra', 'Java'}, dateof(now()), 'antonio123')";
        String cqlStatement2 = "INSERT INTO user (username, nome, email, resgisto_plataforma) VALUES ('joca433', 'Joaquim', 'joaquim@mail.com', dateof(now()))";
        String cqlStatement3 = "INSERT INTO comentarios_user (id, user, video_id, comentario, feito_a) VALUES (now(), 'antonio123',8a28dc90-5632-11ec-b1cd-85f5958119b0 , 'Gostei mesmo muito, mudou a minha vida', dateof(now()))";
        String cqlStatement4 = "INSERT INTO rating (id, id_video, rating) VALUES (now(), 8a32eeb0-5632-11ec-b1cd-85f5958119b0, 5)";    

        session.execute(cqlStatement1);
        session.execute(cqlStatement2);
        session.execute(cqlStatement3);
        session.execute(cqlStatement4);
    }
    
    public static void searchData() {
        String cqlStatement1 = "SELECT * FROM video_pelo_id WHERE id = 75a74210-58ea-11ec-9ce0-67edd99c18ae";
        String cqlStatement2 = "SELECT * FROM user WHERE username = 'joca433'";
        String cqlStatement3 = "SELECT * FROM comentarios_user";
        String cqlStatement4 = "SELECT * FROM video_pelo_id";

        System.out.println("Pesquisa pelo id = 75a74210-58ea-11ec-9ce0-67edd99c18ae");
        for (Row r : session.execute(cqlStatement1)) {
            System.out.println(r.toString());
        }
        System.out.println("\nPesquisa pelo username = joca433");
        for (Row r : session.execute(cqlStatement2)) {
            System.out.println(r.toString());
        }
        System.out.println("\nTabela Completa -> comentarios_user");
        for (Row row : session.execute(cqlStatement3)) {
            System.out.println(row.toString());
        }
        System.out.println("\nTabela Completa -> video_pelo_id");
        for (Row r : session.execute(cqlStatement4)) {
            System.out.println(r.toString());
        }
    }
    
    public static void updateData() {
        String cqlStatement = "UPDATE user SET nome = 'Joao Silva' WHERE username = 'XjoaoX'";
        String cqlStatement1 = "UPDATE user SET nome = 'Rui Silvinha' WHERE username = '__rui__'";
        String cqlStatement2 = "UPDATE comentarios_video SET comentario = 'Afinal video não foi muito bom' WHERE video_id = 8a28dc90-5632-11ec-b1cd-85f5958119b0";

        session.execute(cqlStatement);
        session.execute(cqlStatement1);
        session.execute(cqlStatement2);
        
        for (Row r : session.execute("SELECT * FROM user WHERE username = 'XjoaoX'")) {
            System.out.println(r.toString());
        }
    }
}
