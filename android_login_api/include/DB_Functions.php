<?php

class DB_Functions {

    private $db;

    //put your code here
    // constructor
    function __construct() {
        require_once 'DB_Connect.php';
        // connecting to database
        $this->db = new DB_Connect();
        $this->db->connect();
    }

    // destructor
    function __destruct() {
        
    }
    
    
    public function storeSongs($unique_id, $singer, $albm, $song_track,$latit,$longit) {
	
        $result = mysql_query("INSERT INTO songs_details(uid, artist_band, name,album_movie,time_stamp,latitude,longitude) VALUES('$unique_id', '$singer', '$song_track', '$albm', NOW() ,'$latit', '$longit' )");
        // check for successful store
//	echo $result;
        if ($result) {
            $response['error']="true";
            return mysql_fetch_array($response);
        } 
	else {
            return false;
        }
    }

    

    /**
     * Storing new user
     * returns user details
     */
    public function storeUser($name, $email, $password) {
        $uuid = uniqid('', true);
        $hash = $this->hashSSHA($password);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"]; // salt
        $result = mysql_query("INSERT INTO users(uid, name, email, encrypted_password, salt, created_at) VALUES('$uuid', '$name', '$email', '$encrypted_password', '$salt', NOW())");
        // check for successful store
        //echo "INSERT INTO users(unique_id, name, email, encrypted_password, salt, created_at) VALUES('$uuid', '$name', '$email', '$encrypted_password', '$salt', NOW())";
        if ($result) {
            // get user details 
            $uid = mysql_insert_id(); // last inserted id
            $result = mysql_query("SELECT * FROM users WHERE uid = $uid");
            // return user details
            return mysql_fetch_array($result);
        } else {
            return false;
        }
    }
    public function fetch_songs($userid, $latitude, $longitude){
    	//$today = date("Y-m-d H:i:s");
    	
        $result_fetch =mysql_query("SELECT name, artist_band, album_movie, ((
ACOS( SIN( $latitude * PI( ) /180 ) * SIN( songs_details.latitude * PI( ) /180 ) + COS( $latitude * PI( ) /180 ) * COS( songs_details.latitude * PI( ) /180 ) * COS( ( $longitude - songs_details.longitude )
 * PI() /180 ) ) *180 / PI()) *60 * 1.1515) AS distance FROM songs_details
WHERE TIMEDIFF( NOW( ) , time_stamp ) <  '00:50:00' and uid!='$userid'
HAVING distance <100
ORDER BY distance
")  or die(mysql_error());

	if(mysql_num_rows($result_fetch)>0){
		
	    return mysql_fetch_array($result_fetch);
	}else{
	    return false;
	}

}



    /**
     * Get user by email and password
     */
    public function getUserByEmailAndPassword($email, $password) {

        $result = mysql_query("SELECT * FROM users WHERE email = '$email'") or die(mysql_error());
        // check for result 
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            $result = mysql_fetch_array($result);
            $salt = $result['salt'];
            $encrypted_password = $result['encrypted_password'];
            $hash = $this->checkhashSSHA($salt, $password);
            // check for password equality
            if ($encrypted_password == $hash) {
                // user authentication details are correct
                return $result;
            }
        } else {
            // user not found
            return false;
        }
    }

    /**
     * Check user is existed or not
     */
    public function isUserExisted($email) {
        $result = mysql_query("SELECT email from users WHERE email = '$email'");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            // user existed 
            return true;
        } else {
            // user not existed
            return false;
        }
    }

    /**
     * Encrypting password
     * @param password
     * returns salt and encrypted password
     */
    public function hashSSHA($password) {

        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    }

    /**
     * Decrypting password
     * @param salt, password
     * returns hash string
     */
    public function checkhashSSHA($salt, $password) {

        $hash = base64_encode(sha1($password . $salt, true) . $salt);

        return $hash;
    }

}

?>
