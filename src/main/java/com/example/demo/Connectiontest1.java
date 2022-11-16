package com.example.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.xml.transform.TransformerException;

import com.itextpdf.text.DocumentException;
import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.blob.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class Connectiontest1 {

	public static void main(String[] args) throws DocumentException, URISyntaxException, StorageException, IOException, GeneralSecurityException, TransformerException  {
		SpringApplication.run(Connectiontest1.class);
		Connectiontest1 ob= new Connectiontest1();
		List<String> s=ob.returnValue();
		System.out.println(s);
		
	}

	@GetMapping("/")
	
	public String method2()
	{
		
		return "welcome to Listcheck";
	}
	
	@GetMapping("/testing")
	
	

		
	   
			public List<String> returnValue()
	    {
		List<String> s=new ArrayList<String>() ;
		s.add("a");
		s.add("b");
		s.add("c");
		return s;	
	    }
	
	    
}	
	