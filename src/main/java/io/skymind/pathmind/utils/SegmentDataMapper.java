package io.skymind.pathmind.utils;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.flow.router.Location;
import com.vaadin.flow.server.VaadinRequest;

import elemental.json.Json;
import elemental.json.JsonObject;
import io.skymind.pathmind.security.PathmindUserDetails;

/**
 * Prepares the optional data that is going to be sent to segment.io
 */
public class SegmentDataMapper {

	public static JsonObject getUser(PathmindUserDetails user){
		JsonObject json = Json.createObject();
		json.put("id", Long.toString(user.getId()));
		json.put("name", user.getName());
		json.put("email", user.getEmail());
		return json;
	}
	
	public static Map<String, String> getAdditionalVisitParameters(Location location) {
		Map<String, String> map = new HashMap<>();
		map.put("path", location.getPath());
		map.put("referer", VaadinRequest.getCurrent().getHeader("referer"));
		map.put("user-agent", VaadinRequest.getCurrent().getHeader("user-agent"));
		return map;
	}
}
