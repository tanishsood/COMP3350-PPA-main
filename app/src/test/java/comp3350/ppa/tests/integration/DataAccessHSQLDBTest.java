package comp3350.ppa.tests.integration;

import junit.framework.TestCase;
import java.io.IOException;

import comp3350.ppa.application.Services;
import comp3350.ppa.application.Main;

import comp3350.ppa.persistence.DataAccess;
import comp3350.ppa.tests.persistence.DataAccessTest;

public class DataAccessHSQLDBTest extends TestCase
{
    private static String dbName = Main.dbName;

    public DataAccessHSQLDBTest(String arg0)
    {
        super(arg0);
    }

    public void testDataAccess()
    {
        try{
            IntegrationTests.copyDbToTestFolder();
        } catch (IOException e){
            fail("Could not copy database PPA.script file");
        }


        DataAccess dataAccess;

        Services.closeDataAccess();

        System.out.println("\nStarting Integration test DataAccess (using default DB)");

        // Use the following two statements to run with the real database
        Services.createDataAccess(dbName);
        dataAccess = Services.getDataAccess(dbName);


        DataAccessTest.dataAccessTest(dataAccess);

        Services.closeDataAccess();

        System.out.println("Finished Integration test DataAccess (using default DB)");
    }
}