package com.gifts.login;

import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;

import com.gifts.user.UserService;
import com.gifts.utility.MongoDBUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class LoginService {
	DBCollection usersCollection = MongoDBUtil.getCollection("users");
	JSONObject result = new JSONObject();
	
	public JSONObject createAccount (JSONObject jsonUser) throws JSONException {

		jsonUser.remove("id");
		jsonUser.remove("retypePassword");
		jsonUser.remove("createAccountPasswordError");
		jsonUser.remove("createAccountNameError");
		jsonUser.remove("fieldName");
		jsonUser.remove("createAccountNameExistError");
		jsonUser.remove("success");
		
		Object mongoUser = com.mongodb.util.JSON.parse(jsonUser.toString());
		DBObject userDbObj = (DBObject) mongoUser;
		
		
		String userName = null;
		try {
			userName = (String) jsonUser.get("name");
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		BasicDBObject query = new BasicDBObject("name", userName);

		DBObject user =  usersCollection.findOne(query);
		
		if (user == null) {
			usersCollection.insert(userDbObj);
			ObjectId userId = (ObjectId)userDbObj.get( "_id" );
			result.put("success", 1);
			result.put("userId", userId);
		} else {
			result.put("success", 0);

		}
		return result;
	}

	public JSONObject login(JSONObject jsonUser) throws JSONException {

		String name = (String) jsonUser.get("name");
		String password = (String) jsonUser.get("password");
		BasicDBObject query = new BasicDBObject("name", name).append("password", password);
		DBObject user = usersCollection.findOne(query);
		
		
		
		
		if (user == null) {
			result.put("success", 0);
		} else {
			String loginUserName = (String) user.get("name");
			
			
			ObjectId loginUserId = (ObjectId) user.get("_id");
			UserService userService = new UserService();
			long unreadMessagesCount = userService.getUnreadMessagesCount(loginUserId.toString());
			
			
			result.put("userId", loginUserId);
			result.put("name", loginUserName);
			result.put("unreadMessagesCount", unreadMessagesCount);
			result.put("success", 1);
		}

		return result;
	}
	
	public JSONObject editAccount(JSONObject jsonUser) throws JSONException {
		
		String userId = (String) jsonUser.get("_id");
		String email = (String) jsonUser.get("email");
		String password = (String) jsonUser.get("password");
		String name = (String) jsonUser.get("name");
		//String city = (String) jsonUser.get("city");
		
		BasicDBObject searchQuery = new BasicDBObject().append("_id",
				new ObjectId(userId));
		
		BasicDBObject userUpdateInfo = new BasicDBObject();
		userUpdateInfo.put("email", email);
		userUpdateInfo.put("password", password);
		userUpdateInfo.put("name", name);
		//userUpdateInfo.put("city", city);
		
		usersCollection.update(searchQuery, userUpdateInfo);

		return login(jsonUser);
		
	}


	
}
