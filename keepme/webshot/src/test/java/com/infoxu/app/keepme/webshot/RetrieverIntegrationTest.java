package com.infoxu.app.keepme.webshot;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.testng.annotations.Test;

public class RetrieverIntegrationTest {
	@Test(enabled = false)
	public void testSnapshotRetriever() throws IOException {
		SeleniumSnapshotRetriever retriever = new SeleniumSnapshotRetriever(
				SeleniumSnapshotRetriever.SSRType.SELENIUM_FIREFOX);
		byte[] image = retriever.getSnapshot(new URL("http://www.google.com"));
		writeImage(image, "/home/yujin/workspace/tmp/image_google.png");
		writeMetaData(retriever.getSource(), retriever.getCurrentURL(), 
				"/home/yujin/workspace/tmp/image_google.info");
//		
//		image = retriever.getSnapshot(new URL("http://www.cs.umn.edu"));
//		writeImage(image, "/home/yujin/workspace/tmp/image_cs_umn.png");
//		writeMetaData(retriever.getSource(), retriever.getCurrentURL(), 
//				"/home/yujin/workspace/tmp/image_cs_umn.info");
//		
//		image = retriever.getSnapshot(new URL("http://www.turn.com"));
//		writeImage(image, "/home/yujin/workspace/tmp/image_turn.png");
//		writeMetaData(retriever.getSource(), retriever.getCurrentURL(), 
//				"/home/yujin/workspace/tmp/image_turn.info");
		
		retriever.close();
	}
	
	private void writeImage(byte[] image, String fileName) throws IOException {
		FileOutputStream out = new FileOutputStream(fileName);
		out.write(image);
		out.close();
	}
	
	private void writeMetaData(String source, String url, String fileName) throws IOException {
		FileWriter out = new FileWriter(fileName);
		out.write("# URL\n");
		out.write(url);
		out.write("\n# Page Source\n");
		out.write(source);
		out.close();
	}
}
