package com.gifts.setGifts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.Part;

import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;

import com.gifts.utility.MongoDBUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.mongodb.gridfs.GridFS;
import com.mongodb.util.JSON;

public class SetGiftsService {

	DBCollection giftsCollection = MongoDBUtil.getCollection("gifts");
	JSONObject result = new JSONObject();
	
	public String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length()-1);
            }
        }
        return "";
	}

	public ObjectId getGiftId () {
		
		BasicDBObject userDbObj = new BasicDBObject(); 
		giftsCollection.insert(userDbObj);

		ObjectId giftId = (ObjectId)userDbObj.get( "_id" );
		
		return giftId;
	}
	
	
	public JSONObject setGiftsInfo(JSONObject jsonGiftInfo) throws JSONException {
		
		String userId = null;
		int city = 0;
		String name = null;
		String giftId = null;
		boolean giftIdCheck = false;
		
		try {
			userId = (String) jsonGiftInfo.get("userId");
			city = (int) jsonGiftInfo.getInt("city");
					
			name = (String) jsonGiftInfo.get("name");
			
			giftIdCheck = jsonGiftInfo.has("giftId");
			if (giftIdCheck) {
				giftId = (String) jsonGiftInfo.get("giftId");
				
				jsonGiftInfo.remove("giftId");
			}
			
			
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		int error = 0;
		if (userId == null) {
			result.put("userIdError", "You must be logged user to make gift");
			error = 1;
		}
		
		if (name == null) {
			result.put("nameError", "Enter name of your gift");
			error = 1;
		}
		

		if (error == 0) {
			
			jsonGiftInfo.remove("error");
			BasicDBObject mongoGiftObj = (BasicDBObject) com.mongodb.util.JSON.parse(jsonGiftInfo.toString());
			mongoGiftObj.append("createdDate", new Date());
			mongoGiftObj.append("city", (int)city);
			DBObject giftDbObj = new BasicDBObject((Map<?, ?>) mongoGiftObj);
			
			if (giftIdCheck) {
				giftDbObj = new BasicDBObject("$set", (DBObject) mongoGiftObj);
				BasicDBObject query = new BasicDBObject();
				query.append("_id", new ObjectId(giftId));	
				giftsCollection.update(query, giftDbObj);
			} else {
				giftDbObj = new BasicDBObject((Map<?, ?>) mongoGiftObj);
				giftsCollection.insert(giftDbObj);
			}
		
			result.put("successGiftInfo", 1);
		}
		
		return result;
		
	}


	public void setGiftImage(String base64String, ObjectId giftId) throws JSONException {
		
		DBObject giftImageDbObj = new BasicDBObject("_id", giftId);
		((BasicDBObject) giftImageDbObj).append("image", base64String);
		
		BasicDBObject query = new BasicDBObject(); 
		query.append("_id", giftId);
		
		giftsCollection.update(query, giftImageDbObj);
		
	}

}
