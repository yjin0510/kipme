package com.infoxu.app.keepme.data;

import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.Test;

public class DataUtilTest {
	@Test
	public void testIdKeyConversion() {
		Random rand = new Random();
		for (int i = 0; i < 10; i++) {
			long id = Math.abs(rand.nextLong());
			System.out.println(id + " => " + DataUtil.getKeyFromId(id));
			Assert.assertEquals(id, DataUtil.getIdFromKey(DataUtil.getKeyFromId(id)));
		}
	}
}
