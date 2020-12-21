import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class CodeAnalyzerTest {
	
	/***
	 * Private Fields
	 */
	private CodeAnalyzer analyzer;
	

	// Test Methods:
	
	
	/***
	 * Test Method 1 : test method for methodes_LOC that should be successful
	 */
	@Test
	void methodes_LOC_ShouldBeSuccessful() {
		// Arrange
		analyzer = new CodeAnalyzer("files_to_analyze");
		String[] testMethod = Arrays.copyOfRange(analyzer.classes[0], 1, 1);
		int expectedLOC = 33;
		
		// Act
		int testLOC = analyzer.methode_LOC(testMethod);
		
		// Assert
		assertEquals(testLOC, expectedLOC);
	}
	
	/***
	 * Test Method 2 : test method for methodes_LOC that should not be successful
	 */
	@Test
	void methodes_LOC_ShouldNotBeSuccessful() {
		// Arrange
		analyzer = new CodeAnalyzer("files_to_analyze");
		String[] testMethod = Arrays.copyOfRange(analyzer.classes[0], 1, 1);
		int expectedLOC = 30;
		
		// Act
		int testLOC = analyzer.methode_LOC(testMethod);
		
		// Assert
		assertFalse(testLOC == expectedLOC);
	}
	
	/***
	 * Test Method 3 : test method for methodes_CLOC that should be successful
	 */
	@Test
	void methodes_CLOC_ShouldBeSuccessful() {
		// Arrange
		analyzer = new CodeAnalyzer("files_to_analyze");
		String[] testMethod = Arrays.copyOfRange(analyzer.classes[0], 1, 1);
		int expectedCLOC = 12;
		
		// Act
		int testCLOC = analyzer.methode_CLOC(testMethod);
		
		// Assert
		assertEquals(testCLOC, expectedCLOC);
	}
	
	/***
	 * Test Method 4 : test method for methodes_CLOC that should not be successful
	 */
	@Test
	void methodes_CLOC_ShouldNotBeSuccessful() {
		// Arrange
		String[] testMethod = Arrays.copyOfRange(analyzer.classes[0], 1, 1);
		int expectedCLOC = 30;
		
		// Act
		int testCLOC = analyzer.methode_CLOC(testMethod);
		
		// Assert
		assertFalse(testCLOC == expectedCLOC);
	}
	
	/***
	 * Test Method 5 : test method for methodes_DC that should be successful
	 */
	@Test
	void methodes_DC_ShouldBeSuccessful() {
		// Arrange
		analyzer = new CodeAnalyzer("files_to_analyze");
		String[] testMethod = Arrays.copyOfRange(analyzer.classes[0], 1, 1);
		double expectedDC = 0.36363637;
		
		// Act
		double testDC = analyzer.methode_DC(testMethod);
		
		// Assert
		assertEquals(testDC, expectedDC);
	}
	
	/***
	 * Test Method 6 : test method for methodes_DC that should not be successful
	 */
	@Test
	void methodes_DC_ShouldNotBeSuccessful() {
		// Arrange
		analyzer = new CodeAnalyzer("files_to_analyze");
		String[] testMethod = Arrays.copyOfRange(analyzer.classes[0], 1, 1);
		double expectedDC = 0.30;
		
		// Act
		double testDC = analyzer.methode_DC(testMethod);
		
		// Assert
		assertFalse(testDC == expectedDC);
    }
    
    /***
	 * Test Method 7 : test method for classe_LOC that should be successful
	 */
	@Test
	void classe_LOC_ShouldBeSuccessful() {
		// Arrange
		analyzer = new CodeAnalyzer("files_to_analyze");
		String[] testClass = analyzer.classes[0];
		int expectedLOC = 181;
		
		// Act
		int testLOC = analyzer.classe_LOC(testClass);
		
		// Assert
		assertEquals(testLOC, expectedLOC);
	}
	
	/***
	 * Test Method 8 : test method for classe_LOC that should not be successful
	 */
	@Test
	void classe_LOC_ShouldNotBeSuccessful() {
		// Arrange
		analyzer = new CodeAnalyzer("files_to_analyze");
		String[] testClass = analyzer.classes[0];
		int expectedLOC = 70;
		
		// Act
		int testLOC = analyzer.classe_LOC(testClass);
		
		// Assert
		assertFalse(testLOC == expectedLOC);
	}
	
	/***
	 * Test Method 9 : test method for classe_CLOC that should be successful
	 */
	@Test
	void classe_CLOC_ShouldBeSuccessful() {
		// Arrange
		analyzer = new CodeAnalyzer("files_to_analyze");
		String[] testClass = analyzer.classes[0];
		int expectedCLOC = 78;
		
		// Act
		int testCLOC = analyzer.classe_CLOC(testClass);
		
		// Assert
		assertEquals(testCLOC, expectedCLOC);
	}
	
	/***
	 * Test Method 10 : test method for classe_CLOC that should not be successful
	 */
	@Test
	void classe_CLOC_ShouldNotBeSuccessful() {
		// Arrange
		analyzer = new CodeAnalyzer("files_to_analyze");
		String[] testClass = analyzer.classes[0];
		int expectedCLOC = 70;
		
		// Act
		int testCLOC = analyzer.classe_CLOC(testClass);
		
		// Assert
		assertFalse(testCLOC == expectedCLOC);
	}
	
	/***
	 * Test Method 11 : test method for classe_DC that should be successful
	 */
	@Test
	void classe_DC_ShouldBeSuccessful() {
		// Arrange
		analyzer = new CodeAnalyzer("files_to_analyze");
		String[] testClass = analyzer.classes[0];
		double expectedDC = 0.43093923;
		
		// Act
		double testDC = analyzer.classe_DC(testClass);
		
		// Assert
		assertEquals(testDC, expectedDC);
	}
	
	/***
	 * Test Method 12 : test method for classe_DC that should not be successful
	 */
	@Test
	void classe_DC_ShouldNotBeSuccessful() {
		// Arrange
		analyzer = new CodeAnalyzer("files_to_analyze");
		String[] testClass = analyzer.classes[0];
		double expectedDC = 0.4309123;
		
		// Act
		double testDC = analyzer.classe_DC(testClass);
		
		// Assert
		assertFalse(testDC == expectedDC);
	}
	

}
