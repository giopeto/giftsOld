package com.gifts.login;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.gifts.utility.JsonUtil;

public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public LoginServlet() {
        super();
        
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LoginService loginService = new LoginService();

		request.setCharacterEncoding("UTF-8");
		
		JSONObject jsonUser = JsonUtil.makeJson(request);
		JSONObject result = new JSONObject();

		String action;
		try {
			action = (String) jsonUser.get("action");
			jsonUser.remove("action");

			if (action.equalsIgnoreCase("login")) {
				result = loginService.login(jsonUser);
			}

			if (action.equalsIgnoreCase("createAccount")) {
				result = loginService.createAccount(jsonUser);
			}
			
			if (action.equalsIgnoreCase("editAccount")) {
				
				System.out.println(jsonUser);
				
				result = loginService.editAccount(jsonUser);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.print(result);
		out.flush();
		
	}

}
