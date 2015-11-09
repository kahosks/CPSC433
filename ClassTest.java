//Test cases for overlaps method in Class.java
import static org.junit.Assert.*;

import org.junit.Test;

import junit.framework.Assert;

public class ClassTest {

	@SuppressWarnings("deprecation")
	@Test
	public void overlapsTest() {
		Class a = new Course("CPSC", "433", "LEC", "01");
		a.setDay("MO");
		a.setTime("8:00");
		Class b = new Course("CPSC", "231", "LEC", "02");
		b.setDay("MO");
		b.setTime("8:00");
		Class c = new Lab("Bob", "bob", "bob", "bob");
		c.setDay("TU");
		c.setTime("8:00");
		Assert.assertEquals("Error 23", true, a.overlaps(b));
		Assert.assertEquals("Error 24",false, a.overlaps(c));
		Assert.assertEquals("Error 25",true, b.overlaps(a));
	
		a.setDay("TU");
		a.setTime("8:30");
		Assert.assertEquals("Error 29",true, c.overlaps(a));
		Assert.assertEquals("Error 28", true, a.overlaps(c));

		
		a.setDay("FR");
		a.setTime("8:00");
		c.setDay("FR");
		c.setTime("10:00");
		b.setDay("FR");
		b.setTime("9:00");
		Assert.assertEquals("Error 35",false, a.overlaps(c));
		Assert.assertEquals("Error 36",false, c.overlaps(a));
		//Assert.assertEquals("Error 37",false, c.overlaps(b));
		Assert.assertEquals("Error 37",false, a.overlaps(b));
		Assert.assertEquals("Error 37",false, b.overlaps(a));
		
	}

}
