package com.gifts.view;

import java.util.Iterator;

import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;

import com.gifts.user.UserService;
import com.gifts.utility.MongoDBUtil;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class ViewGiftsService {

	DBCollection giftsCollection = MongoDBUtil.getCollection("gifts");
	
	JSONObject gifts = new JSONObject();
	
	public JSONObject getAllGifts() throws JSONException {
		
		ObjectId giftId = null;
        String giftsKey = "";
		
        DBCursor cursor = giftsCollection.find();
        try {
          while (cursor.hasNext()) {
              DBObject cur = cursor.next();
              int curCity = 0;
              try {
            	  curCity = (int) cur.get( "city" );
              } catch (Exception e) {
				System.out.println("EXceptionnnnnnnnnnnnnnnnnnnnnn: " + e);
              }
              
              String cityName = getCityById(curCity);
              cur.put("cityName", cityName);
              giftId = (ObjectId)cur.get( "_id" );
              giftsKey = "gift_" +  giftId;
              gifts.put(giftsKey, cur);

          }
        } finally {
            cursor.close();
        }
		
		return gifts;
		
	}



	public JSONObject getSingleGifts(String viewGiftId) throws JSONException {
	
		
		DBObject singleGiftQuery = new BasicDBObject("_id", new ObjectId(viewGiftId));
		DBObject giftObject = giftsCollection.findOne(singleGiftQuery);
		
		int curCity = (int) giftObject.get("city");
		
        String cityName = getCityById(curCity);

		giftObject.put("cityName", cityName);
		gifts.put("gifts", giftObject);
		
		return gifts;
	}
	
	public JSONObject getGiftsByUserId(String userId) throws JSONException {
		
		ObjectId giftId = null;
        String giftsKey = "";
        
		DBObject userGiftsQuery = new BasicDBObject("userId", userId);
		
		
		DBCursor cursor = giftsCollection.find(userGiftsQuery);
		try {
			while (cursor.hasNext()) {
				DBObject cur = cursor.next();

				int curCity = (int) cur.get("city");
				String cityName = getCityById(curCity);
				cur.put("cityName", cityName);
				
				BasicDBList messages = ( BasicDBList ) cur.get("messages");
				
				if (messages != null){
					for( Iterator< Object > it = messages.iterator(); it.hasNext(); ) {
						
						BasicDBObject dbo     = ( BasicDBObject ) it.next();
						String fromId = (String) dbo.get("fromId");
						UserService userService = new UserService();
						
						String messageAuthor = userService.getUserNameById(fromId);
						dbo.put("messageAuthor", messageAuthor);
						
					}
				}
				
				giftId = (ObjectId) cur.get("_id");
				giftsKey = "gift_" + giftId;
				gifts.put(giftsKey, cur);

			}
		} finally {
			cursor.close();
		}
		
		return gifts;
	}


	public static String getCityById(int curCity) {
		DBCollection cityCollection = MongoDBUtil.getCollection("city");
		
		DBObject singleGiftQuery = new BasicDBObject("_id", curCity);
		DBObject city = cityCollection.findOne(singleGiftQuery);
		
		String cityName = (String) city.get("name");
		return cityName;
	}



	public JSONObject deleteGift(String giftId) {
		// TODO Auto-generated method stub
		
		System.out.println("FROM DEL ID IS: " + giftId);
		
		DBCollection messagesCollection = MongoDBUtil.getCollection("messages");
		
		
		BasicDBObject removeFromgifts = new BasicDBObject("_id", new ObjectId(giftId));
		BasicDBObject removeFromMessages = new BasicDBObject("giftId", giftId);
		giftsCollection.remove(removeFromgifts);
		messagesCollection.remove(removeFromMessages);
		JSONObject result = new JSONObject();
		try {
			result.put("status", true);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	

}
