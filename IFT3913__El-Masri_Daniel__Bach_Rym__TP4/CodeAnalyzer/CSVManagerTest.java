import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.jupiter.api.Test;

class CSVManagerTest {

	/***
	 * Private Fields
	 */
	private CSVManager manager;
	private CodeAnalyzer analyzer;
	
	/***
	 * Initiation methods pre-testing executions
	 */
	@Before
	public void initTest()
	{
		analyzer = new CodeAnalyzer("files_to_analyze");
		manager = new CSVManager();
	}
	
	// Test Methods
	
	/***
	 * Test Method 1 : Successfully update Classes CSV File
	 */
	@Test
	void updateCSVFileClasses_ShouldBeSuccessful() 
	{
		// Arrange
		analyzer = new CodeAnalyzer("files_to_analyze");
		manager = new CSVManager();
		String[] testData = analyzer.produceClassesData();
		
		try
		{
			// Act
			manager.updateCSVFile(testData, true);
			
			// Assert
			assertTrue(true);
		}
		catch(Exception e)
		{
			// Assert
			fail(e);
		}
	}
	
	/***
	 * Test Method 2 : Successfully update Methods CSV File
	 */
	@Test
	void updateCSVFileMethods_ShouldBeSuccessful() 
	{
		// Arrange
		analyzer = new CodeAnalyzer("files_to_analyze");
		manager = new CSVManager();
		String[] testData = analyzer.produceMethodsData();
		
		try
		{
			// Act
			manager.updateCSVFile(testData, false);
			
			// Assert
			assertTrue(true);
		}
		catch(Exception e)
		{
			// Assert
			fail(e);
		}
	}
	
	/***
	 * Test Method 3 : update Class CSV file with method data
	 */
	@Test
	void updateCSVFileWithWrongData_ShouldBeSuccessful()
	{
		// Arrange
		analyzer = new CodeAnalyzer("files_to_analyze");
		manager = new CSVManager();
		String[] testData = analyzer.produceMethodsData();
		
		try
		{
			// Act
			manager.updateCSVFile(testData, true);
			
			// Assert
			assertTrue(true);
		}
		catch(Exception e)
		{
			// Assert 
			fail(e);
		}
	}

}
