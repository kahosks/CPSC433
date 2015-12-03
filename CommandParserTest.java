import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests CommandParser class. 
 * @author CPSC433Toshibe
 *
 */
public class CommandParserTest {
	
	@Test
	//Tests that input given is parsed correctly.
	public void testInput() throws SchedulerException { 
		String[] input = "-f bob.txt -m 3 -3 -pr 4 -4 -pa 5 -5 -s 6 -6".split(" ");
		CommandParser cp = new CommandParser(input);
		int[] actual = new int[8];
		actual[0] = cp.getMinfilled();
		actual[1] = cp.getPenMinfilled();
		actual[2] = cp.getPref();
		actual[3] = cp.getPenPref();
		actual[4] = cp.getPair();
		actual[5] = cp.getPenPair();
		actual[6] = cp.getSecdiff();
		actual[7] = cp.getPenSecdiff();
		
		int[] expected = {3, -3, 4, -4, 5, -5,6, -6};

		Assert.assertArrayEquals(expected, actual);
		
	}

}
