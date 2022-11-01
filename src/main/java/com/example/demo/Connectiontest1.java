package com.example.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import java.net.URISyntaxException;
import java.security.GeneralSecurityException;


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
		
		Connectiontest1 ob= new Connectiontest1();
		String s=ob.returnValue();
		System.out.println(s);
		SpringApplication.run(Connectiontest1.class, args);
	}

	@GetMapping("/")
	
	public String method2()
	{
		
		return "welcome to ContainerCheck";
	}
	
	@GetMapping("/testing")
	
	

		
	   
	    //public static void main( String[] args ) throws StorageException, URISyntaxException, GeneralSecurityException, TransformerException, IOException
		public String returnValue()throws StorageException, URISyntaxException, GeneralSecurityException, TransformerException, IOException
	    {
			  final String storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=storagetestpoccsl;AccountKey=SAe7L4MStD0F/FNFgXgsTDzda4aqptsq4lOO1Bl4QpXiTbbiMi78H01CKpVTFPXUsRjNjLJbsAl++AStgB4PLA==;EndpointSuffix=core.windows.net";
	    	File text=File.createTempFile("text", ".txt");
	        	
	    		

	    		CloudStorageAccount storageAccount;
	    		CloudBlobClient blobClient = null;
	    		CloudBlobContainer container=null;

	    		
				storageAccount = CloudStorageAccount.parse(storageConnectionString);
				blobClient = storageAccount.createCloudBlobClient();
				container = blobClient.getContainerReference("test");
				CloudBlockBlob blob2 = container.getBlockBlobReference("welcome.txt");
				FileOutputStream os= new FileOutputStream(text);
				blob2.download(os);
				 BufferedReader br= new BufferedReader(new FileReader(text));
				 String st;
				 String s="";
			        while ((st = br.readLine()) != null)
			        {
			        	 s=st;
			        }  
			        br.close();
				 
		 text.deleteOnExit();
		 //System.out.println(s);
		  return s;
				
				
	    }
	
}	
	