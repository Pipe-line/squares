<?php



DEFINE('HOST', 'localhost');
DEFINE('USER', 'awzkxfuc_app'); //USERNAME
DEFINE('PASS', 'app@db12'); //PASSWORD
DEFINE('DDBB', 'awzkxfuc_app'); //DDBB SCHEMA

    
    /* RESPONSE ARRAY */
    $response = array('code' => -1);

    //Faltan datos
    if (!isset($_POST['email']) || !isset($_POST['password'])) {
        $response['code'] = -2;
        die(json_encode($response));
    }
    
    $data = array(
        'email' => $_POST['email'],
        'password' => $_POST['password'],
        );
    
    $conn = open_connection();
    $query = 'SELECT id,email,name,surname,birthday,gender,position,extract,image FROM users WHERE email="' . $data['email'] .'" AND password="' . $data['password'] . '"';
    
    $rs = mysql_query($query);
    
    $user = getResult($rs);

    if ($user[id] != null) {
        //Login OK
        $response['uid'] = $user[id];
        $response['code'] = 0;
        $response['uemail'] = utf8_encode($user[email]);
        $response['uname'] = utf8_encode($user[name]);
        $response['usurname'] = utf8_encode($user[surname]);
        $response['ubirthday'] = utf8_encode($user[birthday]);
        $response['ugender'] = utf8_encode($user[gender]);
        $response['uposition'] = utf8_encode($user[position]);
        $response['uextract'] = utf8_encode($user[extract]);
        
        $response['uimage'] = utf8_encode($user[image]);

        
       /* if($user[image]=="default.jpg"){
        	
        	$response['uimage'] = utf8_encode("default.jpg");

        }else{
        
        $file = "./img/".$user[image];
			if($fp = fopen($file,"rb", 0)){			
			$gambar = fread($fp,filesize($file));
			fclose($fp);
			$base64 = chunk_split(base64_encode($gambar));
			
			$response['uimage'] = $base64;
					
			}
        }*/
        
        close_connection($conn);
        die(json_encode($response));
    } else {
        //Login ERROR
        $response['code'] = -1;
        close_connection($conn);
        die(json_encode($response));
    }
    
    
    //FUNCTIONS
    
    function login($data)
    {
        $conn = open_connection();
        $query = 'SELECT id FROM users WHERE email="' . $data['email'] .
            '" AND password="' . $data['password'] . '"';
        $rs = mysql_query($query);
        var_dump($rs);
    
    
    }
    
    
    function check_email($email)
    {
        $email = trim(strtolower($email));
        $rs = $this->db->query('SELECT email FROM users where email="' . $email . '"')->
            result_array();
        if (count($rs) > 0) {
            return false;
        } else {
            return true;
        }
    }

    
    //DDBB Operations
    
    
    function getResult($result, $type = MYSQL_ASSOC)
    {
        return mysql_fetch_array($result, $type);
    }
    
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