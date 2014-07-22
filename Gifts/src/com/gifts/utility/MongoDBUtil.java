package com.gifts.utility;

import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class MongoDBUtil {
	   private static  DB database;
	    static{
	        try {
	//connect to the MongoDB
	            Mongo mongo=new Mongo("localhost" , 27017 );
	//get database
	            database=mongo.getDB("gifts");
	        } catch (UnknownHostException ex) {
	            Logger.getLogger(MongoDBUtil.class.getName()).log(Level.SEVERE, null, ex);
	        } catch (MongoException ex) {
	            Logger.getLogger(MongoDBUtil.class.getName()).log(Level.SEVERE, null, ex);
	        }
	    }
	//get collection from database
	    public static DBCollection getCollection(String collectionName){
	        return database.getCollection(collectionName);
	    }
	    public static DB getDatabase() {
			
	    	return database;

	    }
}
