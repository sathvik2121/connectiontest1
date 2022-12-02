package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;

import org.apache.commons.io.FilenameUtils;
import java.io.FileOutputStream;
import java.io.FileWriter;

import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.blob.*;


import java.util.ArrayList;
import java.util.Base64;
import java.util.List;




import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfReader;

import com.itextpdf.signatures.BouncyCastleDigest;
import com.itextpdf.signatures.DigestAlgorithms;
import com.itextpdf.signatures.IExternalDigest;
import com.itextpdf.signatures.IExternalSignature;
import com.itextpdf.signatures.PdfSignatureAppearance;
import com.itextpdf.signatures.PdfSigner;
import com.itextpdf.signatures.PrivateKeySignature;
//import com.itextpdf.text.DocumentException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;


import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyStore;

import java.security.PrivateKey;
import java.security.Security;

import java.security.cert.Certificate;
@RestController
@SpringBootApplication
public class SignatureGeneration {

	public static void main(String[] args) throws StorageException, URISyntaxException, GeneralSecurityException, MalformedURLException, UnsupportedEncodingException   {
		SignatureGeneration ob= new SignatureGeneration();
		String s=ob.run("data2_2022_12_02.pdf");
	
		System.out.println(s);

		SpringApplication.run(SignatureGeneration.class, args);
	}

	@GetMapping("/")
	
	public String method2()
	{
		
		return "welcome to signature generation";
	}
	
	@GetMapping("/testing")
	
	public String run(@RequestParam( name="fileName") String name)throws StorageException, URISyntaxException,  GeneralSecurityException
	{
		final String storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=mqbawblobstorage01;AccountKey=4eEEA1jiy/kEpf9PvN8ikjQeXGFODXXH33G+VPhUiyhqzF7K7RrwFg/0CDEBJpkaYzWArR1bW2XD+AStaWP6zg==;EndpointSuffix=core.windows.net";
	    final char[] PASSWORD = "EliLilly".toCharArray();
		CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer container=null;
		String encode = null;
		String encodedFileName = null;
		try {    
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			blobClient = storageAccount.createCloudBlobClient();
			container = blobClient.getContainerReference("pdffiles");
			CloudBlobContainer container2=blobClient.getContainerReference("fileaccess");
			CloudBlobContainer buffercontainer=blobClient.getContainerReference("buffercontainer");
			CloudBlobContainer xmlcontainer=blobClient.getContainerReference("xmlcontainer");
			CloudBlobContainer encodecontainer=blobClient.getContainerReference("encodedfiledata");
			container.createIfNotExists(BlobContainerPublicAccessType.CONTAINER, new BlobRequestOptions(), new OperationContext());		 
                   File finalFile=File.createTempFile("final", ".pdf");
	               File outputFile=File.createTempFile("output", ".pdf");
	               File keyFile=File.createTempFile("keystore",".pfx");
	               File encodeFile=File.createTempFile("data",".txt");
	               CloudBlockBlob blob2 = buffercontainer.getBlockBlobReference(name);
	               FileOutputStream xsloutput= new FileOutputStream(outputFile);
	               blob2.download(xsloutput);
	               CloudBlockBlob blob4 = container2.getBlockBlobReference("nvlap.pfx");
	               FileOutputStream keyoutput= new FileOutputStream(keyFile);
	               blob4.download(keyoutput);
	               InputStream in=new FileInputStream(keyFile);
	               BouncyCastleProvider provider = new BouncyCastleProvider();
	               Security.addProvider(provider);
	               KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
	               ks.load(in, PASSWORD);
	               String alias = ks.aliases().nextElement();
	               PrivateKey pk = (PrivateKey) ks.getKey(alias, PASSWORD);
	               Certificate[] chain = ks.getCertificateChain(alias);
	               SignatureGeneration app = new SignatureGeneration();
	               app.sign(outputFile.getAbsolutePath(), finalFile.getAbsolutePath() , chain, pk, DigestAlgorithms.SHA256, provider.getName(),
	               PdfSigner.CryptoStandard.CMS, "Approved", "India");
	               String fileNameWithOutExt = FilenameUtils.removeExtension(name);
	               CloudBlockBlob blob = container.getBlockBlobReference(fileNameWithOutExt+".pdf");
	               blob.uploadFromFile(finalFile.getAbsolutePath());
	               byte[] b= Files.readAllBytes(finalFile.toPath());
	                encode=Base64.getEncoder().encodeToString(b);
	                try (PrintWriter out = new PrintWriter(encodeFile)) {
	                    out.println(encode);
	                }
	               
	               CloudBlockBlob encodeblob = encodecontainer.getBlockBlobReference(fileNameWithOutExt+".txt");
	               encodedFileName=fileNameWithOutExt+".txt";
	               encodeblob.uploadFromFile(encodeFile.getAbsolutePath());
	               CloudBlockBlob xmlblob = xmlcontainer.getBlockBlobReference(fileNameWithOutExt.substring(0,fileNameWithOutExt.length()-11)+".xml");
	               CloudBlockBlob bufferblob = buffercontainer.getBlockBlobReference(name);
	               keyoutput.close();
	               xsloutput.close();
	               in.close();
	               //fWriter.close();
	               finalFile.deleteOnExit();
	               outputFile.deleteOnExit();
	               keyFile.deleteOnExit();
	               encodeFile.deleteOnExit();
	               blob2.deleteIfExists();
	               xmlblob.deleteIfExists();
               bufferblob.deleteIfExists();
	               
    			}
		
	catch (IOException e) {
       e.printStackTrace();
	}
		return encodedFileName;
		
   }

public void sign(String src, String dest, Certificate[] chain, PrivateKey pk, String digestAlgorithm,
        String provider, PdfSigner.CryptoStandard signatureType, String reason, String location)
        throws GeneralSecurityException, IOException {

    PdfReader reader = new PdfReader(src);
    PdfSigner signer = new PdfSigner(reader, new FileOutputStream(dest),false);
    Rectangle rect = new Rectangle(70, 105, 400, 400);
    PdfSignatureAppearance appearance = signer.getSignatureAppearance();
    appearance.setReason(reason).setLocation(location).setReuseAppearance(false).setPageRect(rect).setPageNumber(5);
    signer.setFieldName("sig");
    IExternalSignature pks = new PrivateKeySignature(pk, digestAlgorithm, provider);
    IExternalDigest digest = new BouncyCastleDigest();
    signer.signDetached(digest, pks, chain, null, null, null, 0, signatureType);
	}
	}

