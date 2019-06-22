package com.smookcreative.icare.DatabasesManagers;

public class RemoteServer {

    public String  RemoteAdmin, RemotePassword, RemotDataBase, Remotetable;

    //public static String ServerURL = "http://91.216.107.162";
    public static String ServerURL = "https://smookcreative.com";

   // public static String icareServerURL = "http://icare.smookcreative.com";
    public static String icareServerURL = "https://smookcreative.com/I_Care/PHP_Scripts/";

    private static final String path = "/I_Care/PHP_Scripts/Agents/";


    private static final String global_request_path =icareServerURL + "/wp-json/wp/v2/";


    //public static String ConnectionScript = ServerURL + path + "connect.php";



    //public static String PlacesScript = ServerURL + path + "centers.php";
    public static String PlacesScript = ServerURL + path + "booking.php";
    public static String CitiesScript = ServerURL + path + "cities.php";


    // Json article and media infos paths
    //public static String ILearn_News_Script = global_request_path+"posts/";
    public static String ILearn_News_Script = icareServerURL+"posts.php/";
    public static String ILearn_Alerts_Script = icareServerURL+"alerts.php/";
    public static String ILearn_Comments_Script = icareServerURL+"comments.php/";
    public static String ILearn_Categories_Script = icareServerURL+"categories.php/";
    public static String ILearn_New_Comment_Script = icareServerURL+"post_comments.php";
    public static String ILearn_Update_Comment_Script = icareServerURL+"comment_update.php";
    public static String ILearn_Delete_Comment_Script = icareServerURL+"comment_delete.php";
    public static String ILearn_Rate_Article_Script = icareServerURL+"rate_article.php";
    public static String ISearch_Update_Like_Booking_Script = icareServerURL+"isearch_update_likes.php";
    public static String ISearch_Booking_Comments_Script = icareServerURL+"booking_comments.php";
    public static String ISearch_Booking_Insert_Comments_Script = icareServerURL+"booking_insert_comments.php";

   //Connexion scripts
   public static String ConnectionScript = icareServerURL + "Sign_in.php";
   public static String CheckEmailcript = icareServerURL + "check_email.php";

   // Create user
    public static String Create_User_Script = icareServerURL+"new_user.php";
    //Update user
    public static String Update_User_Script = icareServerURL+"update_user.php";

    //delete user
    public static String Delete_User_Script = icareServerURL+"delete_user.php";

    //user profile picture
    public static String User_Script_Picture = icareServerURL+"upload_profilePicture.php";

    //report crashes
    public static String Report_Crashes_Script = icareServerURL+"report_crashes.php";



    public RemoteServer() {
        /*ServerURL = "http://91.216.107.162";
        RemoteAdmin = ;
        RemotePassword = ;
        RemotDataBase = ;
        Remotetable = ;*/
    }

}
