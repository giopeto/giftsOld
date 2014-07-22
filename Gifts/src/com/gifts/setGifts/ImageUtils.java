package com.gifts.setGifts;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;




import java.io.IOException;

import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class ImageUtils {

    /**
     * Decode string to image
     * @param imageString The string to decode
     * @return decoded image
     */
    public static BufferedImage decodeToImage(String imageString) {

        BufferedImage image = null;
        byte[] imageByte;
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            imageByte = decoder.decodeBuffer(imageString);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * Encode image to string
     * @param image The image to encode
     * @param type jpeg, bmp, ...
     * @return encoded string
     */
    public static String encodeToString(BufferedImage image, String type) {
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();

            BASE64Encoder encoder = new BASE64Encoder();
            imageString = encoder.encode(imageBytes);

            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageString;
    }

    public static void main (String args[]) throws IOException {
        /* Test image to string and string to image start */
        BufferedImage img = ImageIO.read(new File("files/img/TestImage.png"));
        BufferedImage newImg;
        String imgstr;
        imgstr = encodeToString(img, "png");
        System.out.println(imgstr);
        newImg = decodeToImage(imgstr);
        ImageIO.write(newImg, "png", new File("files/img/CopyOfTestImage.png"));
        /* Test image to string and string to image finish */
    }
    }



/*public class Base64 {
	public JSONObject setGiftsInfo(JSONObject jsonGiftInfo) throws JSONException {
		BASE64Decoder decoder = new BASE64Decoder();
		BASE64Encoder encoder = new BASE64Encoder();
		try {
			String encodedBytes = encoder.encodeBuffer("JavaTips.net"
					.getBytes());
			System.out.println("encodedBytes " + encodedBytes);
			byte[] decodedBytes = decoder.decodeBuffer(encodedBytes);
			System.out.println("decodedBytes " + new String(decodedBytes));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonGiftInfo;
	}
}*/
