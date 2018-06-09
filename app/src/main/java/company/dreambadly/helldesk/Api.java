package company.dreambadly.helldesk;


public class Api {

    private static final String ROOT_URL = "http://165.227.156.66/HeroApi/v1/Api.php?apicall=";
    private static final String ROOT_URL_USERS = "http://165.227.156.66/HeroApi/v1/ApiUsers.php?apicall=";

    public static final String URL_CREATE_HERO = ROOT_URL + "createhero";
    public static final String URL_READ_HEROES = ROOT_URL + "getheroes";
    public static final String URL_UPDATE_HERO = ROOT_URL + "updatehero";
    public static final String URL_DELETE_HERO = ROOT_URL + "deletehero&id=";

    public static final String URL_CREATE_USER = ROOT_URL_USERS + "createuser";
    public static final String URL_READ_USERS = ROOT_URL_USERS + "getusers";
    public static final String URL_UPDATE_USER = ROOT_URL_USERS + "updateuser";
    public static final String URL_DELETE_USER = ROOT_URL_USERS + "deleteuser&id=";

}
