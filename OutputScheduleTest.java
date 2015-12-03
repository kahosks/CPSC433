import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests OutputSchedule class. 
 * DOES NOT CURRENTLY TEST COMPARATOR.  Comparator tested via printing out 
 * the solution.
 * @author CPSC433Toshibe
 *
 */
public class OutputScheduleTest {

	@Test
	/**
	 * Tests convert day method.
	 */
	public void testConvertDay() {
		int[] indexArray1 = {20, 20, 900, 1800, 2700, 3600, 5600};
		String[] stringArray1 = {"", "", "CPSC 433 LEC 01", "SENG 301 LEC 020", "CPSC 433 LEC 01 TUT 01", "CPSC 332 LEC 02", "CPSC 413 LEC 01"};
		OutputSchedule os = new OutputSchedule(stringArray1, indexArray1);
		String[] actual = new String[indexArray1.length-2];
		actual[0] = os.convertDay(indexArray1[2]);
		actual[1] = os.convertDay(indexArray1[3]);
		actual[2] = os.convertDay(indexArray1[4]);
		actual[3] = os.convertDay(indexArray1[5]);
		actual[4] = os.convertDay(indexArray1[6]);
		
		String[] expected = {"MO", "MO", "TU", "TU", "FR"};
		Assert.assertArrayEquals(expected, actual);
		/*for (int i = 0; i <expected.length;i++) {
			System.out.println("S" +expected[i]);
			System.out.println(actual[i]);
		}*/
	}
	
	/**
	 * Tests convert hour method.
	 */
	@Test
	public void testConvertHour() {
		int[] indexArray1 = {20, 20, 900, 1800, 2700, 3600, 5600};
		String[] stringArray1 = {"", "", "CPSC 433 LEC 01", "SENG 301 LEC 020", "CPSC 433 LEC 01 TUT 01", "CPSC 332 LEC 02", "CPSC 413 LEC 01"};
		OutputSchedule os = new OutputSchedule(stringArray1, indexArray1);
		String[] actual = new String[indexArray1.length-2];
		actual[0] = os.convertHour(indexArray1[2]);
		actual[1] = os.convertHour(indexArray1[3]);
		actual[2] = os.convertHour(indexArray1[4]);
		actual[3] = os.convertHour(indexArray1[5]);
		actual[4] = os.convertHour(indexArray1[6]);
		
		String[] expected = {"9:00", "18:00", "3:00", "12:00", "8:00"};
		Assert.assertArrayEquals(expected, actual);
	}
	/**
	 * Not really a test, but prints out output.  If not alphabetized, then comparator is incorrect.
	 */
	@Test
	public void testComparator() {
		int[] indexArray1 = {20, 20, 900, 1800, 2700, 3600, 5600};
		String[] stringArray1 = {"", "", "CPSC 433 LEC 01", "SENG 301 LEC 020", "CPSC 433 LEC 01 TUT 01", "CPSC 332 LEC 02", "CPSC 413 LEC 01"};
		OutputSchedule os = new OutputSchedule(stringArray1, indexArray1);
		os.output();
	}

}
