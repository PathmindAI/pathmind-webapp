package io.skymind.pathmind.utils;

import java.util.HashMap;
import java.util.Map;

import io.skymind.pathmind.data.PathmindUser;

/**
 * Prepares the optional data that is going to be sent to segment.io
 */
public class SegmentDataMapper {

	public static Map<String, String> getUserInfoAsMap(PathmindUser user){
		Map<String, String> map = new HashMap<>();
		map.put("name", user.getName());
		map.put("email", user.getEmail());
		map.put("city", user.getCity());
		map.put("country", user.getCountry());
		return map;
	}
}
