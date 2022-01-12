package code.util;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Base64.Encoder;


//Password encryption
public class Encryption {
	//md5 encryption
	public static String encrypt(String password) {
		try {
			Encoder encoder = Base64.getEncoder();
			password = encoder.encodeToString(password.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return password;
	}
}
