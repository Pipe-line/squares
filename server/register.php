<?php


/*

ERROR CODES:

-1 Problem with database
-2 Params empty
-3 Problem creating image from base64
-4 Problem creating image at disk
-5 Duplicate email
*/


DEFINE('HOST', 'localhost');
DEFINE('USER', 'awzkxfuc_app'); //USERNAME
DEFINE('PASS', 'app@db12'); //PASSWORD
DEFINE('DDBB', 'awzkxfuc_app'); //DDBB SCHEMA

    
    /* RESPONSE ARRAY */
    $response = array('code' => -1);
    
    //Faltan datos
    if (!isset($_POST['email']) || !isset($_POST['password']) || !isset($_POST['name']) || !isset($_POST['surname'])
    || !isset($_POST['birthday']) || !isset($_POST['gender']) || !isset($_POST['position'])) {
        $response['code'] = -2;
        die(json_encode($response));
    }
    
    
    
    
    
    
    $data = array(
       'email' => $_POST['email'],
        'password' => $_POST['password'],
         'name' => $_POST['name'],
          'surname' => $_POST['surname'],
           'birthday' => $_POST['birthday'],
            'gender' => $_POST['gender'],
             'position' => $_POST['position'],
              'image' => $_POST['image'],
        );
    
    
    $conn = open_connection();
        
    //COMMIT Y ROLLBACK NECESARIO     
        
    mysql_query("BEGIN");   
        
         
    if(!isset($_POST['image'])){
    	
    	$_POST['image'] = "default.jpg";
    	$imageName = "default";
    }else{
    
    	$_POST['image'] = str_replace(" ", "+", $_POST['image']);
		$imageName = md5(utf8_decode($_POST[email]));
    }
    
         
         
    $query='INSERT INTO users (email, password, name, surname, birthday, gender, position, image, registration_date) VALUES ("'.utf8_decode($_POST[email]).'","'.utf8_decode($_POST[password]).'","'.utf8_decode($_POST[name]).'","'.utf8_decode($_POST[surname]).'","'.utf8_decode($_POST[birthday]).'","'.utf8_decode($_POST[gender]).'","'.utf8_decode($_POST[position]).'","'.$imageName.'.jpg",CURRENT_TIMESTAMP)';
    
    $result = mysql_query($query,$conn);
    
  	if (!$result)
  	{
  	 
  	 $error = mysql_error();
  	 
  	 if(strpos($error, "Duplicate entry") == 0){
  	 	
  	 	$response['code'] = -5;
  	 	
  	 }else{
  	    
  	  	$response['code'] = -1;
     
  	  }
  	   mysql_query("ROLLBACK");
  	   close_connection($conn);
      die(json_encode($response)); 		
  	  
     }else{        
      
     if( $_POST['image'] != "default.jpg"){
    
          $img = imagecreatefromstring(base64_decode($_POST['image'])); 
	          if($img != false){ 
	            $resImage = imagejpeg($img, './img/'.md5($_POST['email']).'.jpg'); 
	            if($resImage == false){
	                mysql_query("ROLLBACK");
	                $response['code'] = -4;
	                close_connection($conn);
	                die(json_encode($response));
	            }          
	          }else{
	            mysql_query("ROLLBACK");
	            $response['code'] = -3;
	            close_connection($conn);
	            die(json_encode($response));      
	          } 
      }
              
      $id = mysql_insert_id();
      //$response['debug'] = $_POST['surname']; 
      $response['uid'] = $id;   
 	  $response['code'] = 0;
      mysql_query("COMMIT");
      close_connection($conn);
      die(json_encode($response));			
 	
     }
  
        
    //FUNCTIONS
        
    function open_connection()
    {
    
        $connection = mysql_connect(HOST, USER, PASS);
    
        if (!$connection) {
            die("Database connection failed: " . mysql_error());
        } else {
            $db_select = mysql_select_db(DDBB, $connection);
            if (!$db_select) {
                die("Database selection failed: " . mysql_error());
            }
        }
    
        return $connection;
    
    }
    
    function close_connection($connection)
    {
        mysql_close($connection);
    }
    
    ?>