package com.paipianwang.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class PortalController extends AbstractController{

	@RequestMapping("/index")
	public String portal() {
		
		return "index";
	}
	
	@RequestMapping("/demo")
	public ModelAndView portalView() {
		
		return new ModelAndView("/table/indexDemo");
	}
}
