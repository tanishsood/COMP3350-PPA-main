package comp3350.ppa.tests.integration;

import junit.framework.Test;
import junit.framework.TestSuite;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class IntegrationTests {
    public static TestSuite suite;

    public static Test suite() {
        suite = new TestSuite("Integration tests");
        suite.addTestSuite(BusinessPersistenceSeamTest.class);
        suite.addTestSuite(DataAccessHSQLDBTest.class);
        suite.addTestSuite(StatusCalculationsPersistenceTest.class);
        return suite;
    }

    static void copyDbToTestFolder() throws IOException {
        String dbPath = "database";
        String src = (new File("src/main/assets/db/PPA.script")).getAbsolutePath();
        String dst = (new File(dbPath + "/PPA.script")).getAbsolutePath();
        if (!Files.exists(Paths.get(dbPath))) {
            Files.createDirectory(Paths.get(dbPath));
        }
        Files.copy(Paths.get(src),Paths.get(dst), StandardCopyOption.REPLACE_EXISTING);
    }
}
