<?php
require_once("Rest.inc.php");

class API extends REST 
{
public $data = "";
const DB_SERVER = "localhost";
const DB_USER = "awzkxfuc_app";
const DB_PASSWORD = "app@db12";
const DB = "awzkxfuc_app";

private $db = NULL;

public function __construct()
{
parent::__construct();// Init parent contructor
$this->dbConnect();// Initiate Database connection
}

//Database connection
private function dbConnect()
{
$this->db = mysql_connect(self::DB_SERVER,self::DB_USER,self::DB_PASSWORD);
if($this->db)
mysql_select_db(self::DB,$this->db);
}

//Public method for access api.
//This method dynmically call the method based on the query string
public function processApi()
{
$func = strtolower(trim(str_replace("/","",$_REQUEST['rquest'])));
if((int)method_exists($this,$func) > 0)
$this->$func();
else
$this->response('',404); 
// If the method not exist with in this class, response would be "Page not found".
}

private function login()
{
  echo "Pablin ye tonto";
}

private function users()
{

}

private function deleteUser()
{

}

//Encode array into JSON
private function json($data)
{
if(is_array($data)){
return json_encode($data);
}
}
}

// Initiiate Library
$api = new API;
$api->processApi();
?>