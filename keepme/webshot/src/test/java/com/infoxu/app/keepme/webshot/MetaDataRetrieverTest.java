package com.infoxu.app.keepme.webshot;

import java.net.MalformedURLException;
import java.net.URL;

import org.testng.annotations.Test;

import com.infoxu.app.keepme.webshot.MetaDataRetrieverFactory.MDRType;

public class MetaDataRetrieverTest {
	private static final String urlStr = "http://www.google.com";
	@Test
	public void testIPMetaDataRetriever() throws MalformedURLException {
		URL url = new URL(urlStr);
		MetaDataRetriever retriever = 
				MetaDataRetrieverFactory.getMetaDataRetriever(MDRType.IP);
		System.out.println(retriever.getMetaData(url));
	}
	
	@Test
	public void testDomainDetailsMetaDataRetriever() throws MalformedURLException {
		URL url = new URL(urlStr);
		MetaDataRetriever retriever = 
				MetaDataRetrieverFactory.getMetaDataRetriever(MDRType.DOMAIN_DETAILS);
		System.out.println(retriever.getMetaData(url));
	}
}
