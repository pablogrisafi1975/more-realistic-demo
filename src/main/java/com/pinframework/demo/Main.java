package com.pinframework.demo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pinframework.PinResponses;
import com.pinframework.PinServer;
import com.pinframework.PinServerBuilder;
import com.pinframework.httphandler.PinWebjarsHttpHandler;
import com.pinframework.response.PinResponseSse;
import com.pinframework.upload.FileParam;

public class Main {
	
	private static final Logger LOG = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		PinServer pinServer = new PinServerBuilder().appContext("demo")
				// .externalFolder("C:/git/MiWeb")
				.webjarsAutoMinimize(true).port(9998).build();
		pinServer.onGet("hello", pex -> PinResponses.okText("Hello, world!"));
		// http://localhost:9998/demo/hello will show the text Hello, world!,
		// with
		// Content-type:text/plain; charset=utf-8
		Map<String, Object> obj = new HashMap<>();
		obj.put("code", 1000);
		obj.put("message", "ok");
		pinServer.onGet("hello.json", pex -> PinResponses.okJson(obj));
		// http://localhost:9999/hello.json will show the object serialized,
		// with
		// Content-type:application/json; charset=utf-8

		pinServer.onGet("queryParams", pex -> {
			String firstName = pex.getQueryParams().get("firstName").get(0);
			String lastName = pex.getQueryParams().get("lastName").get(0);
			List<UserDTO> list = UserService.INSTANCE.search(firstName, lastName);
			if (list.size() == 1) {
				return PinResponses.okJson(list.get(0));
			} else {
				return PinResponses
						.notFoundJson("User not found with firstName:" + firstName + " and/or lastName:" + lastName);
			}
		});
		pinServer.onGet("pathParams/:id", pex -> {
			String id = pex.getPathParams().get("id");

			// just to check getPostParams is not null
			pex.getPostParams().isEmpty();
			// just to check getFileParams is not null
			pex.getFileParams().isEmpty();
			// just to check getQueryParams is not null
			pex.getQueryParams().isEmpty();

			Optional<UserDTO> userOpt = UserService.INSTANCE.find(Long.parseLong(id));
			if (userOpt.isPresent()) {
				return PinResponses.okJson(userOpt.get());
			} else {
				return PinResponses.notFoundJson("User not found with id:" + id);
			}
		});
		pinServer.onPost("postParamsClassic", pex -> {
			// just to check getPathParams is not null
			pex.getPathParams().isEmpty();
			// just to check getFileParams is not null
			pex.getFileParams().isEmpty();
			// just to check getQueryParams is not null
			pex.getQueryParams().isEmpty();

			String firstName = ((List<String>) pex.getPostParams().get("firstName")).get(0);
			String lastName = ((List<String>) pex.getPostParams().get("lastName")).get(0);
			Long id = Long.parseLong(((List<String>) pex.getPostParams().get("id")).get(0));
			UserDTO saved = UserService.INSTANCE.save(new UserDTO(id, firstName, lastName));
			return PinResponses.okJson(saved);
		});
		pinServer.onPost("postParamsAngular", pex -> {
			String firstName = (String) pex.getPostParams().get("firstName");
			String lastName = (String) pex.getPostParams().get("lastName");
			Long id = Long.parseLong((String) pex.getPostParams().get("id"));
			UserDTO saved = UserService.INSTANCE.save(new UserDTO(id, firstName, lastName));
			return PinResponses.okJson(saved);
		});
		pinServer.onPost("uploadFileAngular", pex -> {
			String firstName = (String) pex.getPostParams().get("firstName");
			String lastName = (String) pex.getPostParams().get("lastName");
			Long id = Long.parseLong((String) pex.getPostParams().get("id"));
			Map<String, FileParam> fileParams = pex.getFileParams();
			System.out.println(fileParams);
			UserDTO saved = UserService.INSTANCE.save(new UserDTO(id, firstName, lastName));
			return PinResponses.okJson(saved);
		});
		pinServer.onGet("sample.html", pex -> {
			Map<String, String> m = new HashMap<>();
			m.put("user", "jhon smith");
			return PinResponses.okJsut(m, "sample.html.ut");
		});

		pinServer.onGet("sse", pex -> {
			PinResponseSse sse = PinResponses.okSse(pex);
			Executors.newSingleThreadExecutor().execute(() -> {
				for (int i = 0; i < 3; i++) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					sse.comment("comment for " + i);
					sse.send("data" + i);
				}

				sse.send("this is a multiline\nserver sent event\nwith 3 lines");
				sse.close();
				boolean sent = sse.send("after close");
				LOG.info("sent after close? {}", sent);
			});
			return sse;
		});
		pinServer.onGet("sse-obj", pex -> {
			PinResponseSse sse = PinResponses.okSse(pex);
			Executors.newSingleThreadExecutor().execute(() -> {
				for (int i = 0; i < 3; i++) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					sse.comment("comment for " + i);
					sse.send("data" + i);
				}
				UserDTO userDTO = new UserDTO(33L, "Peter", "Parker");
				sse.sendObject(userDTO);
			});
			return sse;
		});
		pinServer.onGet("sse-type", pex -> {
			PinResponseSse sse = PinResponses.okSse(pex);
			Executors.newSingleThreadExecutor().execute(() -> {
				for (int i = 0; i < 10; i++) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					sse.send("event" + (i % 2), "data" + i);
				}
				sse.send("end sse type");
			});
			return sse;
		});

		pinServer.start();

	}

}
