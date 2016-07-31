package com.pinframework.demo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pinframework.PinResponse;
import com.pinframework.PinServer;
import com.pinframework.PinServerBuilder;
import com.pinframework.response.PinResponseNotFoundJson;
import com.pinframework.response.PinResponseOkJson;
import com.pinframework.response.PinResponseOkText;

public class Main {

	public static void main(String[] args) {
		PinServer pinServer = new PinServerBuilder()
				.appContext("demo")
				.port(9998)
				.build();
		pinServer.onGet("hello", pex -> PinResponseOkText.of("Hello, world!"));
		// http://localhost:9998/demo/hello will show the text Hello, world!, with
		// Content-type:text/plain; charset=utf-8
		Map<String, Object> obj = new HashMap<>();
		obj.put("code", 1000);
		obj.put("message", "ok");
		pinServer.onGet("hello.json", pex -> PinResponseOkJson.of(obj));
		// http://localhost:9999/hello.json will show the object serialized, with
		//Content-type:application/json; charset=utf-8
		
		pinServer.onGet("queryParams", pex -> {
			String firstName = pex.getQueryParams().get("firstName").get(0);
			String lastName = pex.getQueryParams().get("lastName").get(0);
			List<UserDTO> list = UserService.INSTANCE.search(firstName, lastName);
			if(list.size() == 1){
				return PinResponseOkJson.of(list.get(0));
			}else{
				return PinResponseNotFoundJson.of("User not found with firstName:" + firstName + " and/or lastName:" + lastName);
			}
		});
		
		pinServer.start();

	}

}
