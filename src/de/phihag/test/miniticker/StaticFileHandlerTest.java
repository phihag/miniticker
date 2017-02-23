package de.phihag.test.miniticker;

import static org.junit.Assert.*;

import org.junit.Test;

import de.phihag.miniticker.StaticFileHandler;

public class StaticFileHandlerTest {

	@Test
	public void testGetExt() {
		assertEquals(StaticFileHandler.getExt("foo.html"), "html");
		assertEquals(StaticFileHandler.getExt("html"), "");
		assertEquals(StaticFileHandler.getExt("/foo/bar.html.gz"), "gz");
		assertEquals(StaticFileHandler.getExt("/fo.o/a.b/c.d/bar"), "");
		assertEquals(StaticFileHandler.getExt("/fo.o/a.b/c.d/bar.y0.x"), "x");
	}

	@Test
	public void testLookupMime() {
		assertEquals(StaticFileHandler.lookupMime("f.html"), "text/html");
		assertEquals(StaticFileHandler.lookupMime("f.xml"), "application/xml");
		assertEquals(StaticFileHandler.lookupMime("f.png"), "image/png");
		assertEquals(StaticFileHandler.lookupMime("f.data"), "application/octet-stream");
	}

}
