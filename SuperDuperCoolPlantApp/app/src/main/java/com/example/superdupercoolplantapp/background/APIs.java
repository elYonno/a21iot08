package com.example.superdupercoolplantapp.background;

public class APIs {
    public final static String LOG_IN = "https://studev.groept.be/api/a21iot08/App_LogIn/"; // username/password
    public final static String UPDATE_LOGIN = "https://studev.groept.be/api/a21iot08/App_UpdateLogIn"; // realName/phoneNumber/emailAddress/password/ID

    public final static String GET_NEXT_SCANS = "https://studev.groept.be/api/a21iot08/App_GetNextScans/"; // userID

    public final static String RECENT_READINGS = "https://studev.groept.be/api/a21iot08/App_GetRecentReadings/"; // plantID
    public final static String GET_PLANTS = "https://studev.groept.be/api/a21iot08/App_GetPlants/"; // userID

    public final static String GET_PLANT_PARAMETERS = "https://studev.groept.be/api/a21iot08/App_GetPlantParameters";
    public final static String GET_POT_NUMBERS = "https://studev.groept.be/api/a21iot08/App_GetPotNumbers";
}
