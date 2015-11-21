import java.util.Vector;

import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

	@Test
	public void TestParserConstruct() throws SchedulerException {
		Parser parser = new Parser("ShortExample");
	}
	
	@Test(expected = SchedulerException.class)
	public void TestParserConstructFileDoesntExist() throws SchedulerException {
		Parser parser = new Parser("abc.def");
	}
	
	@Test
	public void TestParse() throws SchedulerException {
		Parser parser = new Parser("ShortExample");
		parser.parse();
		Vector<Class> labsAndCourses =  parser.getLabsAndCourses();
		
		//parser.parse();
	}
	
//	@Test
//	public void TestGetLabsAndCourses() throws SchedulerException {
//		Parser parser = new Parser("ShortExample");
//		parser.parse();
//		
//		Vector<Class> labsAndCourses =  parser.getLabsAndCourses();
//		
//		Assert.assertArrayEquals(, labsAndCourses);
//	}
}
