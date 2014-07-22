package com.gifts.user;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.gifts.utility.JsonUtil;

/**
 * Servlet implementation class UserServlet
 */

public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		UserService userService = new UserService();
		JSONObject jsonObject = JsonUtil.makeJson(request);
		
		
		JSONObject result = new JSONObject();
		long  unreadMessagesCount = 0;
		
		String action = null;
		try {
			action = (String) jsonObject.get("action");
			jsonObject.remove("action");

			/*if (action.equalsIgnoreCase("getCity")) {
				result = userService.getCity();
			}*/

			if (action.equalsIgnoreCase("getUserInfo")) {
				String userId = (String) jsonObject.get("id");
				result = userService.getUserInfo(userId);
			}
			
			if (action.equalsIgnoreCase("saveMessage")) {
				result = userService.saveMessage(jsonObject);
			}		
			
			if (action.equalsIgnoreCase("viewMessage")) {
				result = userService.viewMessage(jsonObject);
			}
			
			if (action.equalsIgnoreCase("getUnreadMessagesCount")) {
				String userId = (String) jsonObject.get("userId");
				unreadMessagesCount = userService.getUnreadMessagesCount(userId);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		if (!action.equalsIgnoreCase("getUnreadMessagesCount")) {
			out.print(result);
		} else {
			out.print(unreadMessagesCount);
		}
		
		out.flush();
		
	}

}
