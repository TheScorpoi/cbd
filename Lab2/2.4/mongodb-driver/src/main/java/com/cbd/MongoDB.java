package com.cbd;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

public class MongoDB {

    static MongoCollection<Document> mCollection;

    public static void main(String[] args) {

        double start, stop, deltaBeforeIndex, deltaAfterIndex, deltaAfterIndexText;

        try (MongoClient client = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = client.getDatabase("cbd");
            mCollection = database.getCollection("restaurants");

            //alinea a)
            Document doc1 = new Document("_id", new ObjectId())
                    .append("address",
                            new Document().append("building", "123").append("coord", asList(123.021, 143.021))
                                    .append("rua", "Rua de Cima em Baixo").append("zipcode", "12345"))
                    .append("localidade", "Aveiro").append("gastronomia", "Italiana")
                    .append("grades", asList(new Document("date", new Date()).append("grade", "A").append("score", 10)))
                    .append("nome", "Restaurante de Teste").append("restaurant_id", "12345");

            //insert(doc1);
            //search();

            //alinea b)
            
            start = System.nanoTime();
            //searchWithFilterWithoutPrints(and(eq("localidade", "Brooklyn"), eq("gastronomia", "Chinese")));
            stop = System.nanoTime();
            deltaBeforeIndex = (stop - start) / 1e6;

            createIndex("localidade");
            createIndex("gastronomia");
            
            
            start = System.nanoTime();
            //searchWithFilterWithoutPrints(and(eq("localidade", "Brooklyn"), eq("gastronomia", "Chinese")));
            stop = System.nanoTime();
            deltaAfterIndex = (stop - start) / 1e6;
            
            createIndexByText("nome");

            start = System.nanoTime();
            //searchWithFilter(regex("nome", "^Wil"));
            stop = System.nanoTime();
            deltaAfterIndexText = (stop - start) / 1e6;

            System.out.println("Tempo de execução antes da criação do índice: " + deltaBeforeIndex + " ms");
            System.out.println("Tempo de execução depois do index: " + deltaAfterIndex + " ms");
            System.out.println("Tempo de execução depois do index: " + deltaAfterIndexText + " ms");
            
            //alinea c)
            // 8
            //searchWithFilter(lt("address.coord.0", -95.7));
            
            // 9({gastronomia: "American", "grades.score": {$gt: 70}
            //searchWithFilter(and(eq("gastronomia", "American"), gt("grades.score", 70)));
            
            // 11
            //searchWithFilter(and(eq("localidade", "Bronx"), in("gastronomia", "American", "Chinese")));

            //12
            //searchWithFilter(and(in("localidade", "State Island", "Queens", "Brooklyn")));

            //13
            //searchWithFilter(and(gt("address.coord.1", 42), lte("address.coord.1", 52)));

            //alinea d)
            System.out.printf("Numero de localidae distintas: %d\n", countLocalidades());

            Map<String, Integer> restLocalMap =  countRestByLocalidade();
            System.out.println("Numero de restaurants por localidade:");
            for (String key : restLocalMap.keySet()) {
                System.out.println(" -> " + key + " - " + restLocalMap.get(key));
            }

            List<String> restNameCloserTo = getRestWithNameCloserTo("Park");
            System.out.println("Numero de restaurants contendo 'Park' no nome:");
            for (String restaurant : restNameCloserTo) {
                System.out.println(" -> " + restaurant);
            }



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

    public static void searchWithFilterWithoutPrints(Bson filter) {
        try {
            FindIterable<Document> documents = mCollection.find(filter);
        } catch (Exception e) {
            System.err.println("ERROR: Searching on database!");
        }
    }

    public static void createIndex(String filedName) {
        try {
            mCollection.createIndex(Indexes.ascending(filedName));
        } catch (Exception e) {
            System.err.println("ERROR: Creating index!");
        }
    }

    public static void createIndexByText(String text) {
        try {
            mCollection.createIndex(Indexes.text((text)));
        } catch (Exception e) {
            System.err.println("ERROR: Creating text index!");
        }
    }

    public static int countLocalidades() {
        Map<String, Integer> localidadesMap = new HashMap<String, Integer>();
        try {
            FindIterable<Document> documents = mCollection.find();
            for (Document doc : documents) {
                String localidades = doc.getString("localidade");
                if (localidadesMap.containsKey(localidades)) {
                    localidadesMap.put(localidades, localidadesMap.get(localidades) + 1);
                } else {
                    localidadesMap.put(localidades, 1);
                }
            }
            return localidadesMap.size();
        } catch (Exception e) {
            System.err.println("ERROR: Counting localidades!");
        }
        return 0;
    }

    public static Map<String, Integer> countRestByLocalidade() {
        Map<String, Integer> restLocalidadeMap = new HashMap<String, Integer>();
        try {
            FindIterable<Document> documents = mCollection.find();
            for (Document doc : documents) {
                String localidades = doc.getString("localidade");
                if (restLocalidadeMap.containsKey(localidades)) {
                    restLocalidadeMap.put(localidades, restLocalidadeMap.get(localidades) + 1);
                } else {
                    restLocalidadeMap.put(localidades, 1);
                }
            }
            return restLocalidadeMap;
        } catch (Exception e) {
            System.err.println("ERROR: Counting localidades!");
        }
        return null;
    }

    public static List<String> getRestWithNameCloserTo(String name) {
        List<String> restaurantList = new ArrayList<String>();
        try {
            FindIterable<Document> documents = mCollection.find();
            for (Document doc : documents) {
                String restName = doc.getString("nome");
                if (restName.contains(name)) {
                    restaurantList.add(restName);
                }
            }
            return restaurantList;
        } catch (Exception e) {
            System.err.println("ERROR: Getting restaurants with name closer to!");
        }
        return null;
    }
}
