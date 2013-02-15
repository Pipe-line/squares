 <?php
 
DEFINE('HOST','localhost'); 
DEFINE('USER','awzkxfuc_app'); //USERNAME
DEFINE('PASS','app@db12'); //PASSWORD 
DEFINE('DDBB','awzkxfuc_app'); //DDBB SCHEMA



	
		/* RESPONSE ARRAY */
		$response = array( 
			'code' => -1
			);
				
  		if(!isset($_POST['userid']) || !isset($_POST['place_name']) || !isset($_POST['place_latitude']) || !isset($_POST['place_longitude']))
		{
			$response['code'] = -1;
			die(json_encode($response));
		}
        
	
		$data = array(
					'userid' => $_POST['userid'],
					'place_name' => $_POST['place_name'],
					'place_latitude'	   => $_POST['place_latitude'],
		 			'place_longitude'	   => $_POST['place_longitude'],
					);	
					
        
        $query = "INSERT INTO checkins (userid,place_name,place_latitude,place_longitude, datetime) 
                VALUES (
                    '".$data['userid']."',
                    '".$data['place_name']."',
                    '".$data['place_latitude']."',
                    '".$data['place_longitude']."',CURRENT_TIMESTAMP
                
                );";
                
        $conn = open_connection();
        $rs = mysql_query($query);// var_dump($array);// echo $query.'<br>';  echo 'insertinto <br>';
            		
		//1 - ERROR: Username already exists
		if($rs)
		{
			$response['code'] = 1;
			die(json_encode($response));

		}
		//2 - ERROR: Mail already in use
		else {
            $response['code'] = -1;
			die(json_encode($response));
		}
        close_connection($conn);


    
    
     function open_connection() {
        
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
    
    function close_connection($connection){
		mysql_close($connection);
	}
    
    ?>