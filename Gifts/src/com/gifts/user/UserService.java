package com.gifts.user;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import org.bson.BSONObject;
import org.bson.types.ObjectId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gifts.utility.MongoDBUtil;
import com.gifts.view.ViewGiftsService;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

public class UserService {
	
	DBCollection usersCollection = MongoDBUtil.getCollection("users");
	DBCollection giftsCollection = MongoDBUtil.getCollection("gifts");
	DBCollection messagesCollection = MongoDBUtil.getCollection("messages");
	int BREAK_RECURSION_COUNT = 0;

	public JSONObject getUserInfo(String userId) {
		
		JSONObject user = new JSONObject();
		
		DBObject userQuery = new BasicDBObject("_id", new ObjectId(userId));
		DBObject userObject = usersCollection.findOne(userQuery);
		
		
		
	/*	
		
		//////////////////////////////////////
		

        db.messages.count({
           "$or": [
               {"toId" : "53b825e1da8ae1454b4d57cc"},
               {"fromId" : "53b825e1da8ae1454b4d57cc"}
           ],
           "messages": {
               "$elemMatch": {
                    "unread": 1,
                      "author": {"$ne": "53b825e1da8ae1454b4d57cc"}
                }     
            }    
		})      

		
		BasicDBList or = new BasicDBList();
		DBObject messageQueryUserId = new BasicDBObject("fromId", userId);
		DBObject messageQueryFromId = new BasicDBObject("toId", userId);
        or.add(messageQueryUserId);
        or.add(messageQueryFromId);
     
        
        BasicDBObject unreadQuery = new BasicDBObject();
        BasicDBList userExistsQuery = new BasicDBList();
        userExistsQuery.add(messageQueryUserId);
       
        
 
        
        unreadQuery.append("$or", or);
       
        BasicDBObject messageQuery = new BasicDBObject();
        
        BasicDBObject messagePomQuery = new BasicDBObject("unread", 1);
        messagePomQuery.append("author", new BasicDBObject("$ne", userId));
        
        messageQuery.append("$elemMatch",messagePomQuery);
        
        unreadQuery.append("messages", messageQuery);
        
        long count = messagesCollection.count(unreadQuery);
  */      

		try {
			user.put("user", userObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return user;
	}

	
	public String getUserNameById (String curFromId) {
		DBCollection userCollection = MongoDBUtil.getCollection("users");
		
		DBObject singleUserQuery = new BasicDBObject("_id", new ObjectId(curFromId));
		DBObject userNameObject = userCollection.findOne(singleUserQuery);
		String userName = (String) userNameObject.get("name");
		
		return userName;
	}
	
	public JSONObject saveMessage(JSONObject jsonObject) throws JSONException {
		
		String message = (String) jsonObject.get("message");
		String fromId = (String) jsonObject.get("fromId");
		String giftId = (String) jsonObject.get("giftId");
		String toId = (String) jsonObject.get("toId");
		
		java.util.Date date = new java.util.Date();
		/*String messageCreatedDate = date.toString();
		messageCreatedDate = messageCreatedDate.substring(0, 19);*/
		String pattern = "dd-MM-yy 'във' HH:mm ";
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		String messageCreatedDate = sdf.format(date);
		
		BasicDBObject messageObject = new BasicDBObject();
		messageObject.append("giftId", giftId);
		messageObject.append("fromId", fromId);
		messageObject.append("toId", toId);
		
		
		
		BasicDBObject checkForMessageQuery = new BasicDBObject("giftId", giftId);
		checkForMessageQuery.append("fromId", fromId);
		checkForMessageQuery.append("toId", toId);
		/*BasicDBList orUserQuery = new BasicDBList();
		orUserQuery.add(new BasicDBObject("fromId", fromId));
        orUserQuery.add(new BasicDBObject("toId", toId));
        checkForMessageQuery.append("$or", orUserQuery);*/
       
        DBObject checkForMessage = messagesCollection.findOne(checkForMessageQuery);
		ObjectId checkForMessageId = null;
		if (checkForMessage != null) {
			checkForMessageId = (ObjectId) checkForMessage.get("_id");
		}
		
		if (jsonObject.has("messageId")) {
			BasicDBObject messageInfoObject = new BasicDBObject();
			String messageIdStr = (String) jsonObject.get("messageId");
			ObjectId messageId = new ObjectId(messageIdStr);
			messageInfoObject.append("message", message).append("messageCreatedDate", messageCreatedDate).append("unread", 1).append("author", fromId);
			
			WriteResult result = messagesCollection.update(new BasicDBObject("_id", messageId),
	                new BasicDBObject("$push", new BasicDBObject("messages", messageInfoObject)), false, false);
			
		} else if (checkForMessageId != null) { 
			BasicDBObject messageInfoObject = new BasicDBObject();
			messageInfoObject.append("message", message).append("messageCreatedDate", messageCreatedDate).append("unread", 1).append("author", fromId);
			WriteResult result = messagesCollection.update(new BasicDBObject("_id", checkForMessageId),
	                new BasicDBObject("$push", new BasicDBObject("messages", messageInfoObject)), false, false);
		} else {
			List<BasicDBObject> messageInfoObject = new ArrayList<>();
			messageInfoObject.add(new BasicDBObject("message", message).append("messageCreatedDate", messageCreatedDate).
					append("unread", 1).append("author", fromId));
			
			messageObject.append("messages", messageInfoObject);
			messagesCollection.insert(messageObject);
		}
		
		return null;
	}

	public JSONObject viewMessage(JSONObject jsonObject) throws JSONException {
		
		JSONObject viewMessage = new JSONObject();
		
		String userId = (String) jsonObject.get("userId");
		
		BasicDBList or = new BasicDBList();
		DBObject messageQueryUserId = new BasicDBObject("fromId", userId);
		DBObject messageQueryFromId = new BasicDBObject("toId", userId);
        or.add(messageQueryUserId);
        or.add(messageQueryFromId);
     
        DBObject messageQuery = new BasicDBObject("$or", or);
        
        
		/*DBCursor cursor = giftsCollection.find(messageQuery, new BasicDBObject("messages", true)
		.append("name", true).append("image", true));*/
		
        DBCursor cursor = messagesCollection.find(messageQuery);

		try {
			while (cursor.hasNext()) {
				DBObject cur = cursor.next();

				BasicDBList messageObj = (BasicDBList) (cur.get("messages"));
				
				for (Object message: messageObj){
					JSONObject messageJson  = new JSONObject(message.toString());
					String authorId = (String) messageJson.get("author");
					String authorName = getUserNameById(authorId);
					((BSONObject) message).put( "author", authorName );
				}
				
				if (cur.get("messages") != null){
					
					updateUreadMessages(userId, new ObjectId((String)cur.get("_id").toString()));
					
					if (viewMessage.has("_id")) {
						
						viewMessage.put("message_" + cur.get("_id") + ".messages", cur.get("messages"));

					} else {
						viewMessage.put("message_" + cur.get("_id"), cur);

						ViewGiftsService viewGiftsService= new ViewGiftsService();
						JSONObject giftsData = viewGiftsService.getSingleGifts((String) cur.get("giftId"));
				
						BSONObject gift = (BSONObject) giftsData.get("gifts");
						gift.removeField("_id");
						cur.putAll(gift);
						
						viewMessage.put("message_" + cur.get("_id"), cur);
						
						if (giftsData != null) {
							
							
							
						}
					}
					
				}
				
			}
		} finally {
			cursor.close();
		}
		
		return viewMessage;
	}
	
	
	
	
	
	private void updateUreadMessages(String userId, ObjectId messageId) {
		
		BasicDBObject unreadMessageQuery = new BasicDBObject();
		BasicDBList orUserQuery = new BasicDBList();
		//BasicDBObject  messageQuery = new BasicDBObject();
		BasicDBObject  elemMatchMessageQuery = new BasicDBObject();
		BasicDBObject setQuery = new BasicDBObject();
	//	BasicDBObject  multiQuery = new BasicDBObject();
		
		unreadMessageQuery.append("_id", messageId);
		unreadMessageQuery.append("messages.unread", 1);
		DBObject messageQueryUserId = new BasicDBObject("fromId", userId);
		DBObject messageQueryFromId = new BasicDBObject("toId", userId);
		orUserQuery.add(messageQueryUserId);
        orUserQuery.add(messageQueryFromId);
        
       
        elemMatchMessageQuery.append("author", new BasicDBObject("$ne", userId));
        elemMatchMessageQuery.append("unread", 1);
        
      //  messageQuery.append("messages", new BasicDBObject("$elemMatch", elemMatchMessageQuery));
        
        unreadMessageQuery.append("messages", new BasicDBObject("$elemMatch", elemMatchMessageQuery));
        
        unreadMessageQuery.append("$or", orUserQuery);
        
        setQuery.append("$set", new BasicDBObject("messages.$.unread", 0));
       // setQuery.append("multi", true);
       // multiQuery.append("multi", true);
        
/*        System.out.println(unreadMessageQuery);
        System.out.println(setQuery);
*/        
        
        
        messagesCollection.update(unreadMessageQuery, setQuery, false, false);
        
        long count = messagesCollection.count(unreadMessageQuery);
        
        if (count != 0){
        	updateMessageCollectionRecursively(unreadMessageQuery, setQuery);
        }
        
	}


	private void updateMessageCollectionRecursively(BasicDBObject unreadMessageQuery, BasicDBObject setQuery) {
		
		if (BREAK_RECURSION_COUNT < 10) {
			messagesCollection.update(unreadMessageQuery, setQuery, false, false);
	        long count = messagesCollection.count(unreadMessageQuery);
	        
	        if (count != 0){
	        	BREAK_RECURSION_COUNT++;
	        	updateMessageCollectionRecursively(unreadMessageQuery, setQuery);
	        }
		}
        
	}


	public long getUnreadMessagesCount(String userId) {

		BasicDBList or = new BasicDBList();
		DBObject messageQueryUserId = new BasicDBObject("fromId", userId);
		DBObject messageQueryFromId = new BasicDBObject("toId", userId);
		or.add(messageQueryUserId);
		or.add(messageQueryFromId);

		BasicDBObject unreadQuery = new BasicDBObject();
		BasicDBList userExistsQuery = new BasicDBList();
		userExistsQuery.add(messageQueryUserId);

		unreadQuery.append("$or", or);

		BasicDBObject messageQuery = new BasicDBObject();

		BasicDBObject messagePomQuery = new BasicDBObject("unread", 1);
		messagePomQuery.append("author", new BasicDBObject("$ne", userId));

		messageQuery.append("$elemMatch", messagePomQuery);

		unreadQuery.append("messages", messageQuery);

		long count = messagesCollection.count(unreadQuery);

		return count;
	}

}



/*public JSONObject getCity() throws JSONException {

DBCollection cityCollection = MongoDBUtil.getCollection("city");

DBCursor cursor = cityCollection.find();
JSONObject cities = new JSONObject();
 try {
      while (cursor.hasNext()) {
          DBObject cur = cursor.next();
          cities.put((String) cur.get("_id"), cur);
      }
    } finally {
        cursor.close();
    }

return cities;
}*/