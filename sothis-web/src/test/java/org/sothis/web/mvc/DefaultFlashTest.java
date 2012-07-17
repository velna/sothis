package org.sothis.web.mvc;

import junit.framework.Assert;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class DefaultFlashTest {

	private Flash flash;

	@BeforeMethod
	public void beforeMethod() {
		flash = new DefaultFlash();
	}

	@AfterMethod
	public void afterMethod() {
		flash = null;
	}

	@Test
	public void test() {
		final String attr = "attr";
		final Object value = "value";
		final String attr2 = "attr2";
		final Object value2 = "value2";
		Assert.assertFalse(flash.containsAttribute(attr));
		Assert.assertNull(flash.getAttribute(attr));
		flash.setAttribute(attr, value);
		Assert.assertTrue(flash.containsAttribute(attr));
		Assert.assertNotNull(flash.getAttribute(attr));
		Assert.assertSame(value, flash.getAttribute(attr));
		flash.flash();
		flash.setAttribute(attr2, value2);
		Assert.assertTrue(flash.containsAttribute(attr));
		Assert.assertNotNull(flash.getAttribute(attr));
		Assert.assertSame(value, flash.getAttribute(attr));
		Assert.assertTrue(flash.containsAttribute(attr2));
		Assert.assertNotNull(flash.getAttribute(attr2));
		Assert.assertSame(value2, flash.getAttribute(attr2));
		flash.flash();
		Assert.assertFalse(flash.containsAttribute(attr));
		Assert.assertNull(flash.getAttribute(attr));
		Assert.assertTrue(flash.containsAttribute(attr2));
		Assert.assertNotNull(flash.getAttribute(attr2));
		Assert.assertSame(value2, flash.getAttribute(attr2));
		flash.flash();
		Assert.assertFalse(flash.containsAttribute(attr2));
		Assert.assertNull(flash.getAttribute(attr2));
	}

}
