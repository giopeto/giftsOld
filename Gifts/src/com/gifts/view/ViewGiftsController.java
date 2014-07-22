package com.gifts.view;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.gifts.utility.JsonUtil;

public class ViewGiftsController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ViewGiftsController() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ViewGiftsService viewGiftsService = new ViewGiftsService();
		JSONObject gifts = new JSONObject();
		String action = null;
		
		JSONObject viewGiftJson = JsonUtil.makeJson(request);
		try {
			action = viewGiftJson.getString("action");
		
			if (action.equalsIgnoreCase("getAllGifts")) {
				gifts = viewGiftsService.getAllGifts();
			}
			
			if (action.equalsIgnoreCase("getSingleGifts")) {
				String viewGiftId = viewGiftJson.getString("id");
				gifts = viewGiftsService.getSingleGifts(viewGiftId);
			}
			
			if (action.equalsIgnoreCase("getGiftsByUserId")) {
				String userId = viewGiftJson.getString("userId");
				gifts = viewGiftsService.getGiftsByUserId(userId);
			}
			
			if (action.equalsIgnoreCase("deleteGift")) {
				String giftId = viewGiftJson.getString("giftId");
				gifts = viewGiftsService.deleteGift(giftId);
			}
			
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.print(gifts);
		out.flush();
		
	}

}
