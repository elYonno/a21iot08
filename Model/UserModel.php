<?php
/**
* MODEL FOR USER TABLE
*/

require_once PROJECT_ROOT_PATH . "/Model/Database.php";
 
class UserModel extends Database
{
    public function getUsers($limit)
    {
        return $this->select("SELECT * FROM a21iot08.Users ORDER BY userID ASC LIMIT ?", ["i", $limit]);
    }
}