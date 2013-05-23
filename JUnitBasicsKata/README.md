JUnitBasicsKata
===============

 1. Przekształć testy z klasy FileWriterIntegrationTest tak aby były bardziej czytelne. Podpowiedź: użyj @Before i @After.
 2. Przekształć testy z klasy IntegerParsingTest tak aby były bardziej czytelne. 
 	W pierszym przypadku użyj @Test(expectedException=..., w drugim użyj @Rule.
 3. Przekształć testy z klasy FizzBuzzTest tak aby używały runnera Parameterized.
 4. Przyspiesz testy w FizzBuzzServletIntegrationTest. Podpowiedź: użyj @BeforeClass i @AfterClass.
 5. Podziel wcześniejsze testy z tego ćwiczenia na szybkie testy jednostkowe UnitTestsSuite: {IntegerParsingTest, FizzBuzzTest} 
 	i wolne testy integracyjne IntegrationTestsSuite: {FizzBuzzServetIntegrationTest, FileWriterTest}. 
 	Użyj standardowego mechanizmu JUnit (runner Suite), a poźniej dodatku do JUnit (runner ClasspathSuite).  