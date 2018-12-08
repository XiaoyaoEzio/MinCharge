package com.min.charge.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.min.charge.beans.First;
import com.min.charge.mapping.FirstMapper;

@Controller
public class FirstController {
	@Autowired
	private FirstMapper  firstMapper; 
	
	@RequestMapping("/test/get")
	public  void get(){
		First first = firstMapper.getId(2);
		System.err.println(first.getName());
		
	}
}
