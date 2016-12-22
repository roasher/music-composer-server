package ru.pavelyurkin.musiccomposer.core.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StreamApi {

	@RequestMapping( path = "", method = RequestMethod.GET )
	public String test() {
		return "Hello World";
	}
}
