package com.paipianwang.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PortalController extends AbstractController{

	@RequestMapping("/index")
	public String portal() {
		
		return "index";
	}
}
