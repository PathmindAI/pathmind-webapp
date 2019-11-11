package io.skymind.pathmind.utils;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.flow.router.Location;
import com.vaadin.flow.server.VaadinRequest;

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
	
	public static Map<String, String> getAdditionalVisitParameters(Location location) {
		Map<String, String> map = new HashMap<>();
		map.put("path", location.getPath());
		map.put("referer", VaadinRequest.getCurrent().getHeader("referer"));
		map.put("user-agent", VaadinRequest.getCurrent().getHeader("user-agent"));
		return map;
	}
}
