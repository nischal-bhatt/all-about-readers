package com.infybuzz.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.infybuzz.model.StudentResponse;

@Service
public class StudentService {

	List<StudentResponse> ls; 
	
	
	  public List<StudentResponse> restCallToGetStudents()
	  {
		  RestTemplate restTemplate 
		  = new RestTemplate();
		  
		StudentResponse[] studentResponses=  restTemplate.getForObject("http://localhost:8081/api/v1/students", StudentResponse[].class);
		  
		ls 
		= new ArrayList<>();
		
		  for(StudentResponse sr : studentResponses)
		  {
			  ls.add(sr);
		  }
		  
		  return ls;
	  }
	  
	  public StudentResponse getStudent(long dummy1,String dummy2)
	  {
		  System.out.println("dummy1" + dummy1);
		  System.out.println("dummy2" + dummy2);
		  if (ls == null) {
			  restCallToGetStudents();
		  }
		  
		  if (ls != null && !ls.isEmpty())
		  {
			  return ls.remove(0);
		  }
		  
		  return null;
	  }
}
