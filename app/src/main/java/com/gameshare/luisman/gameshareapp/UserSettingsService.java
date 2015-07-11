package com.gameshare.luisman.gameshareapp;

/**
 * Created by LuisMan on 7/9/2015.
 */
public class UserSettingsService
{
    private final String apiUrl =  "https://gamemateservice.azurewebsites.net/api";

    public class ChangePasswordModel
    {
        String OldPassword;
        String NewPassword;
        String ConfirmPassword;
    }

    public static void ChangePassword(ChangePasswordModel model)
    {
      //  AuthenticatedHttpConnection httpConn = new AuthenticatedHttpConnection(apiUrl + "/accounts/ChangePasswor")
    }
}
