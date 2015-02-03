package org.sothis.core.util;

import java.security.SignatureException;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

public class SignatureUtils {

	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

	public static String sign(String secretKey, Map<String, String[]> requestParams) throws SignatureException {
		Map<String, String[]> sortedRequestParams = new TreeMap<String, String[]>(requestParams);
		StringBuilder dataString = new StringBuilder();
		for (Map.Entry<String, String[]> entry : sortedRequestParams.entrySet()) {
			dataString.append(entry.getKey());
			String[] sortedValues;
			if (entry.getValue().length <= 1) {
				sortedValues = entry.getValue();
			} else {
				sortedValues = new String[entry.getValue().length];
				System.arraycopy(entry.getValue(), 0, sortedValues, 0, sortedValues.length);
				Arrays.sort(sortedValues);
			}
			for (String value : sortedValues) {
				dataString.append(value);
			}
		}
		return sign(secretKey, dataString.toString());
	}

	public static String sign(String secretKey, String data) throws SignatureException {
		try {
			SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(), HMAC_SHA1_ALGORITHM);
			Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
			mac.init(signingKey);
			byte[] rawHmac = mac.doFinal(data.getBytes());
			return Hex.encodeHexString(rawHmac);
		} catch (Exception e) {
			throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
		}
	}

}
