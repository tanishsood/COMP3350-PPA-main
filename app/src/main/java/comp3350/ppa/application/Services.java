package comp3350.ppa.application;

import comp3350.ppa.business.GlobalTimer;
import comp3350.ppa.business.GlobalTimerAccess;
import comp3350.ppa.persistence.DataAccess;
import comp3350.ppa.persistence.DataAccessObject;

public class Services {
	private static DataAccess dataAccessService = null;
	private static GlobalTimerAccess globalTimerService = null;

	public static DataAccess createDataAccess(String dbName) {
		if (dataAccessService == null) {
			dataAccessService = new DataAccessObject(dbName);
			dataAccessService.open(Main.getDBPathName());
		}
		return dataAccessService;
	}

	public static DataAccess createDataAccess(DataAccess alternateDataAccessService) {
		if (dataAccessService == null) {
			dataAccessService = alternateDataAccessService;
			dataAccessService.open(Main.getDBPathName());
		}
		return dataAccessService;
	}

	public static DataAccess getDataAccess(String dbName) {
		if (dataAccessService == null) {
			System.out.println("Connection to data access has not been established.");
			System.exit(1);
		}
		return dataAccessService;
	}

	public static void closeDataAccess() {
		if (dataAccessService != null)
			dataAccessService.close();
		dataAccessService = null;
	}

	public static GlobalTimerAccess createGlobalTimerAccess() {
		if (globalTimerService == null) {
			globalTimerService = new GlobalTimer();
		}
		return globalTimerService;
	}

	public static GlobalTimerAccess createGlobalTimerAccess(GlobalTimerAccess alternateTimer) {
		if (globalTimerService == null) {
			globalTimerService = alternateTimer;
		}
		return globalTimerService;
	}

	public static GlobalTimerAccess getGlobalTimerAccess() {
		if (dataAccessService == null) {
			System.out.println("Connection to global timer access has not been established.");
			System.exit(1);
		}
		return globalTimerService;
	}

	public static void closeGlobalTimerAccess() {
		if (globalTimerService != null) {
			globalTimerService.stop();
		}
		globalTimerService = null;
	}
}
