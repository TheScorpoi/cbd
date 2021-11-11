package com.cbd;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;

import com.mongodb.ServerAddress;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.FindIterable;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.Block;

import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.result.DeleteResult;
import static com.mongodb.client.model.Updates.*;
import com.mongodb.client.result.UpdateResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collection;
import java.util.Date;
import static java.util.Arrays.asList;

public class MongoDB {

    static MongoCollection<Document> mCollection;

    public static void main(String[] args) {

        try (MongoClient client = MongoClients.create("mongodb://localhost:27017")) {

            MongoDatabase database = client.getDatabase("cbd");
            MongoCollection<Document> collection = database.getCollection("restaurants");

            

            System.out.println("Start inserting a new restaurant on the database");
            
            

            Document doc1 = new Document("_id", new ObjectId())
                    .append("address", new Document()
                        .append("building", "123")
                        .append("coord", asList(123.021, 143.021))
                        .append("rua", "Rua de Cima em Baixo")
                            .append("zipcode", "12345")
                    )
                    .append("localidade", "Aveiro")
                    .append("gastronomia", "Italiana")
                    .append("grades", asList(
                            new Document("date", new Date())
                            .append("grade", "A")
                            .append("score", 10)
                        )
                    )
                    .append("nome", "Restaurante de Teste")
                    .append("restaurant_id", "12345");
            
            insert(doc1);

            
        } catch (Exception e) {
            System.err.println("ERROR: Connection not established!");
        }

    }

    public static void insert(Document doc) {
        try {
            mCollection.insertOne(doc);
            System.out.println("Inserted");
        } catch (Exception e) {
            System.err.println("ERROR: Inserting into the databse!");
            
        }
    }

    public static void update(Bson filter, Bson updateArgs) {
        try {
            mCollection.updateOne(filter, updateArgs);
        } catch (Exception e) {
            System.err.println("ERROR: Update on database!");
        }
    }

    public static void search() {
        try {
            FindIterable<Document> documents = mCollection.find();
            for (Document doc : documents) {
                System.out.println(doc.toJson());
            }
        } catch (Exception e) {
            System.err.println("ERROR: Searching on database!");
        }
    }

    public static void searchWithFilter(Bson filter) {
        try {
            FindIterable<Document> documents = mCollection.find(filter);
            for (Document doc : documents) {
                System.out.println(doc.toJson());
            }
        } catch (Exception e) {
            System.err.println("ERROR: Searching on database!");
        }
    }

}
