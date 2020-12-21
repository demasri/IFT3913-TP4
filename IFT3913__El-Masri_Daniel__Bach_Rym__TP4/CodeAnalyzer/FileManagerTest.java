import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.jupiter.api.Test;

class FileManagerTest {
	
	// Private Fields
	private FileManager manager;
	
	/***
	 * Initialising Method for the Test Cases
	 */
	@Before
	public void initTest() 
	{
		manager = new FileManager("files_to_analyze");
	}
	
	// Test Case Methods

	/***
	 * Test Method 1 : Test method for getClassesArray should be functional
	 */
	@Test
	void getClassesArray_ShouldBeSuccessful() 
	{
		// Arrange
		manager = new FileManager("files_to_analyze");
		int expectedNumberOfClasses =  9;
		
		// Act
		String[][] testClassesArray = manager.getClassesArray();
		
		// Assert
		assertEquals(testClassesArray.length, expectedNumberOfClasses);
	}
	
	/***
	 * Test Method 2 : Test method for getClassesArray should not be functional
	 */
	@Test
	void getClassesArray_ShouldNotBeSuccessful() 
	{
		// Arrange
		manager = new FileManager("files_to_analyze");
		int expectedNumberOfClasses =  5;
		
		// Act
		String[][] testClassesArray = manager.getClassesArray();
		
		// Assert
		assertEquals(testClassesArray.length, expectedNumberOfClasses);
	}
	
	/***
	 * Test Method 3 : Test method for getClassNameArray should be functional
	 */
	@Test
	void getClassNamesArray_ShouldBeSuccessful() 
	{
		// Arrange
		manager = new FileManager("files_to_analyze");
		String[] expectedClassNameArray = {"CodeAnalyzer.java", "Member.java", "DataBase.java", "Interface.java", "personne.java", "Rapport.java", "Service.java", "Tests.java", "Vues.java"};
		
		// Act
		String[] actualClassNameArray = manager.getClassNamesArray();
		
		// Assert
		assertTrue(actualClassNameArray.equals(expectedClassNameArray));
	}
	
	/***
	 * Test Method 4 : Test method for getClassNameArray but should return a fail 
	 */
	@Test
	void getClassNamesArray_ShouldNotBeSuccessful() 
	{
		// Arrange
		manager = new FileManager("files_to_analyze");
		String[] expectedClassNameArray = {"DataBase.java", "Interface.java", "personne.java", "Rapport.java", "Service.java", "Tests.java", "Vues.java"};
		
		// Act
		String[] actualClassNameArray = manager.getClassNamesArray();
		
		// Assert
		assertFalse(actualClassNameArray.equals(expectedClassNameArray));
	}

}
