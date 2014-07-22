package com.gifts.setGifts;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.DatatypeConverter;

import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;

@MultipartConfig(fileSizeThreshold=1024*1024*2, // 2MB
maxFileSize=1024*1024*10,      // 10MB
maxRequestSize=1024*1024*50)   // 50MB

public class SetGiftPicturesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String SAVE_DIR = "uploadFiles";
	
    public SetGiftPicturesServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


		JSONObject result = new JSONObject();

		SetGiftsService setGiftsService = new SetGiftsService();
		
		ObjectId giftId = setGiftsService.getGiftId();
		
		// gets absolute path of the web application
		
		//String appPath = getServletConfig().getServletContext().getRealPath("WEB-INF/giftsImg");
		
		String appPath = request.getServletContext().getRealPath("");
		// constructs path of the directory to save uploaded file
		
		// creates the save directory if it does not exists
		File fileSaveDir = new File(appPath);

		if (!fileSaveDir.exists()) {
			fileSaveDir.mkdir();
		}
		
/*		System.out.println("realAppPath: " + appPath);
		System.out.println("PATH IS: " + savePath);
*/		ByteArrayOutputStream baos = new ByteArrayOutputStream(1000);
		BufferedImage img = null; 
	
		
		File file = null;
		
		for (Part part : request.getParts()) {
			if (part != null) {
				String fileName = setGiftsService.extractFileName(part);

				String header = part.getHeader("Content-Disposition");
				System.out.println("header: " + part.toString());
				
				/*for (String x: part.toString()) {
					
				}*/
				
				
				Long size = part.getSize();				
				System.out.println("Size:" + size);
				String name = part.getName();	
				System.out.println("Name:" + name);
				
				System.out.println("NO  IMAGE BUT STILL HEREE");
				part.write(appPath + File.separator + fileName);
				
				
				file = new File(appPath + File.separator + fileName);
				 
	    		
				
				System.out.println("PATH: " + appPath + File.separator + fileName);
				
				img = ImageIO.read(new File(appPath,fileName));
			} else {
				System.out.println("UAAAAAAAAAAAA");
			}
			file.delete();
			
	        

			
		}
		
		
		ImageIO.write(img, "jpg", baos);
        baos.flush();
        
        
       
		
         //Store the image as a string
        String base64String = DatatypeConverter.printBase64Binary(baos.toByteArray());
        baos.close();
        
        
        	
        	
        	
        	
			try {
				if (base64String != "") {
					System.out.println("IMAGEEEE");
					setGiftsService.setGiftImage(base64String, giftId);
				} else {
					System.out.println("NOOOOOOOOOOOOO  IMAGEEEE");
					setGiftsService.setGiftImage("noImage", giftId);
				}
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
       
        
		try {
			result.put("giftId", giftId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
		
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.print(result);
		out.flush();

		/* } */
			
			
			
	}
}




/*
package com.gifts.setGifts;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.WriteResult;

@MultipartConfig(fileSizeThreshold=1024*1024*2, // 2MB
maxFileSize=1024*1024*10,      // 10MB
maxRequestSize=1024*1024*50)   // 50MB
@WebServlet("/SetGiftsServlet")
public class SetGiftPicturesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String SAVE_DIR = "uploadFiles";
	
    public SetGiftPicturesServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		JSONObject result = new JSONObject();

		SetGiftsService setGiftsService = new SetGiftsService();
		
		
		System.out.println("HEREEEEEEEEEEE");
		
		
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);

	    if (isMultipart) {
	        FileItemFactory factory = new DiskFileItemFactory();
	        ServletFileUpload upload = new ServletFileUpload(factory);

	    try {
	        List items = upload.parseRequest(request);
	        Iterator iterator = items.iterator();
	        while (iterator.hasNext()) {
	            FileItem item = (FileItem) iterator.next();

	            if (!item.isFormField()) {
	                String fileName = item.getName();

	                String root = getServletContext().getRealPath("/");
	                File path = new File(root + "/uploads");
	                if (!path.exists()) {
	                    boolean status = path.mkdirs();
	                }

	                File uploadedFile = new File(path + "/" + fileName);
	                System.out.println(uploadedFile.getAbsolutePath());
	                item.write(uploadedFile);
	            }
	        }
	    } catch (FileUploadException e) {
	        e.printStackTrace();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
		
		
		
		
		
		
		
		
		
		
		
    		//	setGiftsService.setGiftImage (base64String, giftId);
    		
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.print(result);
		out.flush();

		
			
			
			
	}
}
*/