import java.util.Vector;

import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

	// Test to see if the parser loads a proper file, pass if no error thrown
	@Test
	public void TestParserConstruct() throws SchedulerException {
		Parser parser = new Parser("Tests/ShortExample");
	}
	
	// Test loading a file that doesn't exist, should throw an error
	@Test(expected = SchedulerException.class)
	public void TestParserConstructFileDoesntExist() throws SchedulerException {
		Parser parser = new Parser("Tests/abc.def");
	}
	
	// Test the parser with the example given, pass if no errors are thrown
	@Test
	public void TestParse() throws SchedulerException {
		Parser parser = new Parser("Tests/ShortExample");
		parser.parse();
	}
	
	// Test the parser giving it 2 identical Courses (CPSC 433 LEC 01)
	@Test(expected = SchedulerException.class)
	public void TestDuplicateCourse() throws SchedulerException {
		Parser parser = new Parser("Tests/DuplicateCourse");
		parser.parse();
	}
	
	// Test the parser giving it 2 identical Labs (CPSC 433 LEC  02 LAB   02)
	@Test(expected = SchedulerException.class)
	public void TestDuplicateLab() throws SchedulerException {
		Parser parser = new Parser("Tests/DuplicateLab");
		parser.parse();
	}
	
	// Test the parser giving it the first input file
	@Test
	public void TestFile1() throws SchedulerException {
		Parser parser = new Parser("Tests/deptinst1.txt");
		parser.parse();
	}
	
	// Test the parser giving it the second input file
		@Test
		public void TestFile2() throws SchedulerException {
			Parser parser = new Parser("Tests/deptinst2.txt");
			parser.parse();
		}
}
