<?php
require_once PROJECT_ROOT_PATH . "/Model/Database.php";
 
class UserModel extends Database
{
    public function getUsers()
    {
        return $this->select("SELECT * FROM Users ORDER BY userID ");
    }
}
?>