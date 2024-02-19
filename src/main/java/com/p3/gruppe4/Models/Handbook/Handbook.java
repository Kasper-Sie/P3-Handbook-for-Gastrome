package com.p3.gruppe4.Models.Handbook;


import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.jsonSchema;

import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.springframework.web.multipart.MultipartFile;


import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class Handbook {

    private MongoClient mongoClient;
    public Handbook (MongoClient client){
        this.mongoClient = client;
    }

//  TOPIC OPERATIONS
    public HashSet<Document> getAllTopics(){
        HashSet<Document> returnSet = new HashSet<>(Collections.emptySet());
        try {
            MongoDatabase db = this.mongoClient.getDatabase("Gastrome");
            MongoCollection<Document> collection = db.getCollection("Topic");

            FindIterable<Document> iterableCollection = collection.find();
            Iterator iterator = iterableCollection.iterator();

            while (iterator.hasNext()) {
                returnSet.add((Document) iterator.next());
            }
            // Prints a message if any exceptions occur during the operation
        } catch (MongoException me) {
            System.err.println("Unable to insert due to an error: " + me);
        }
        return returnSet;
    }

    public Document getTopic(String id){
        Document returnDocument = new Document();
        try {
            MongoDatabase db = this.mongoClient.getDatabase("Gastrome");
            MongoCollection<Document> collection = db.getCollection("Topic");

            return collection.find(eq("_id", id)).first();
            // Prints a message if any exceptions occur during the operation
        } catch (MongoException me) {
            System.err.println("Unable to insert due to an error: " + me);
        }
        return returnDocument;
    }

    public Document createTopic(Topic topic, MultipartFile file) {
        Document returnDocument = new Document();
        try  {
            MongoDatabase db = this.mongoClient.getDatabase("Gastrome");

            MongoCollection<Document> collection = db.getCollection("Topic");
            topic.setImagePath(file.getOriginalFilename());
            returnDocument = new Document()
                    .append("_id", topic.getId().toString())
                    .append("name", topic.getName())
                    .append("imagePath", topic.getImagePath());
            collection.insertOne(returnDocument);

            SaveFile saveFile = new SaveFile();
            saveFile.store(file);
            // Prints a message if any exceptions occur during the operation
        } catch (MongoException me) {
                System.err.println("Unable to insert due to an error: " + me);
        }
        return returnDocument;
    }

    public Document editTopic(String topicId, Topic topic, MultipartFile file){
        Document returnDoc = new Document();
        try {
            MongoDatabase db = this.mongoClient.getDatabase("Gastrome");
            MongoCollection<Document> collection = db.getCollection("Topic");

            Document oldDoc = collection.find(eq("_id", topicId))
                    .first();

            returnDoc = new Document("$set", new Document()
                    .append("name", topic.getName().isEmpty() ? oldDoc.getString("name") : topic.getName())
                    .append("imagePath", !topic.getImagePath().isEmpty() ? topic.getImagePath() : oldDoc.getString("imagePath"))
            );

            if(file != null) {
                SaveFile saveFile = new SaveFile();
                saveFile.store(file);
            }

            collection.updateOne(new Document().append("_id",  topicId), returnDoc);
        } catch (MongoException me) {
            System.err.println("Unable to insert due to an error: " + me);
        }
        return returnDoc;
    }

    public long deleteTopic(String topicId){
        try {
            MongoDatabase db = this.mongoClient.getDatabase("Gastrome");
            MongoCollection<Document> collection = db.getCollection("Topic");

            DeleteResult result = collection.deleteOne(new Document().append("_id",  topicId));
            return result.getDeletedCount();
        } catch (MongoException me) {
            System.err.println("Unable to insert due to an error: " + me);
        }
        return 0;
    }


//  SUBTOPIC OPERATIONS
    public HashSet<Document> getAllSubTopics(String parentId){
        HashSet<Document> returnSet = new HashSet<>(Collections.emptySet());
        try {
            MongoDatabase db = this.mongoClient.getDatabase("Gastrome");
            MongoCollection<Document> collection = db.getCollection("SubTopic");

            Document query = new Document().append("parentId", parentId);

            FindIterable<Document> iterableCollection = collection.find(query);
            Iterator iterator = iterableCollection.iterator();

            while (iterator.hasNext()) {
                returnSet.add((Document) iterator.next());
            }
            // Prints a message if any exceptions occur during the operation
        } catch (MongoException me) {
            System.err.println("Unable to insert due to an error: " + me);
        }
        return returnSet;
    }

    public Document getSubTopic(String id){
        Document returnDocument = new Document();
        try {
            MongoDatabase db = this.mongoClient.getDatabase("Gastrome");
            MongoCollection<Document> collection = db.getCollection("SubTopic");

            returnDocument = collection.find(eq("_id", id))
                    .first();

            // Prints a message if any exceptions occur during the operation
        } catch (MongoException me) {
            System.err.println("Unable to insert due to an error: " + me);
        }
        return returnDocument;
    }

    public Document createSubTopic(SubTopic subTopic, MultipartFile file){
        Document returnDocument = new Document();
        try  {
            MongoDatabase db = this.mongoClient.getDatabase("Gastrome");

            MongoCollection<Document> collection = db.getCollection("SubTopic");
            returnDocument = new Document()
                    .append("_id", subTopic.getId().toString())
                    .append("name", subTopic.getName())
                    .append("imagePath", file.getOriginalFilename())
                    .append("parentId", subTopic.getParentId())
                    .append("content", subTopic.getContent())
                    .append("summary", subTopic.getSummary());
            collection.insertOne(returnDocument);

            SaveFile saveFile = new SaveFile();
            saveFile.store(file);
            // Prints a message if any exceptions occur during the operation
        } catch (MongoException me) {
            System.err.println("Unable to insert due to an error: " + me);
        }
        return returnDocument;
    }

    public Document editSubTopic(SubTopic subTopic, String subTopicId, MultipartFile file) {
        Document returnDoc = new Document();
        try {
            MongoDatabase db = this.mongoClient.getDatabase("Gastrome");
            MongoCollection<Document> collection = db.getCollection("SubTopic");

            Document oldDoc = collection.find(eq("_id", subTopicId))
                    .first();

            returnDoc = new Document("$set", new Document()
                    .append("name", subTopic.getName())
                    .append("imagePath", subTopic.getImagePath())
                    .append("content", oldDoc.getString("content"))
                    .append("summary", subTopic.getSummary())
            );

            if(file != null) {
                SaveFile saveFile = new SaveFile();
                saveFile.store(file);
            }

            collection.updateOne(new Document().append("_id",  subTopicId), returnDoc);
        } catch (MongoException me) {
            System.err.println("Unable to insert due to an error: " + me);
        }
        return returnDoc;
    }

    public Document editSubTopicContent(String jsonData, String subTopicId, List<MultipartFile> files){
        Document returnDoc = new Document();
        try  {
            MongoDatabase db = this.mongoClient.getDatabase("Gastrome");
            MongoCollection<Document> collection = db.getCollection("SubTopic");

            Document oldDoc = collection.find(eq("_id", subTopicId))
                    .first();

            returnDoc = new Document("$set", new Document()
                    .append("name", oldDoc.getString("name"))
                    .append("imagePath", oldDoc.getString("imagePath"))
                    .append("content", jsonData)
                    .append("summary", oldDoc.getString("summary"))
            );


            // Handle file data
            if (files != null && !files.isEmpty()) {
                for (MultipartFile file : files) {
                    System.out.println("File Name: " + file.getOriginalFilename());
                    // Process each file as needed
                    SaveFile saveFile = new SaveFile();
                    saveFile.store(file);
                }
            }

            collection.updateOne(new Document().append("_id",  subTopicId), returnDoc);
        } catch (MongoException me) {
            System.err.println("Unable to insert due to an error: " + me);
        }
        return returnDoc;
    }

    public long deleteSubTopic(String subTopicId){
        try  {
            MongoDatabase db = this.mongoClient.getDatabase("Gastrome");
            MongoCollection<Document> collection = db.getCollection("SubTopic");

            DeleteResult result = collection.deleteOne(new Document().append("_id",  subTopicId));
            return result.getDeletedCount();
        } catch (MongoException me) {
            System.err.println("Unable to insert due to an error: " + me);
        }
        return 0;
    }
}