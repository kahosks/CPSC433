import java.util.Vector;

import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

	// Test to see if the parser loads a proper file, pass if no error thrown
	@Test
	public void TestParserConstruct() throws SchedulerException {
		Parser parser = new Parser("ShortExample");
	}
	
	// Test loading a file that doesn't exist, should throw an error
	@Test(expected = SchedulerException.class)
	public void TestParserConstructFileDoesntExist() throws SchedulerException {
		Parser parser = new Parser("abc.def");
	}
	
	// Test the parser with the example given, pass if no errors are thrown
	@Test
	public void TestParse() throws SchedulerException {
		Parser parser = new Parser("ShortExample");
		parser.parse();
		Vector<Class> labsAndCourses =  parser.getLabsAndCourses();
	}
}
