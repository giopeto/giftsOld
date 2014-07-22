package com.gifts.setGifts;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.gifts.utility.JsonUtil;


public class SetGiftsInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SetGiftsInfoServlet() {
        super();
    }
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SetGiftsService setGiftsService = new SetGiftsService();
		request.setCharacterEncoding("UTF-8");
		
		JSONObject jsonGiftInfo = JsonUtil.makeJson(request);
		
		JSONObject result = new JSONObject();	
			
		try {
			result = setGiftsService.setGiftsInfo(jsonGiftInfo);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("jsonGiftInfo");
		System.out.println(jsonGiftInfo);
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.print(result);
		out.flush();
		/*System.out.println("ACTION: " + action);*/

	//	String action = (String) request.getParameter("action");
	}

}
