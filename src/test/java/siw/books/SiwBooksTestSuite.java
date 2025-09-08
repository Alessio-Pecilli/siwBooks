package siw.books;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("SiwBooks Test Suite")
@SelectPackages({
    "siw.books.model",
    "siw.books.services", 
    "siw.books.controller",
    "siw.books.repository"
})
public class SiwBooksTestSuite {
    // Questa classe rimane vuota, serve solo per definire la suite di test
}
