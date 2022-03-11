<?php

// https://a21iot08.studev.groept.be/index.php/{MODULE}/{METHOD}?param1=val1&param2=val2...

require __DIR__ . "/inc/bootstrap.php";
 
$uri = parse_url($_SERVER['REQUEST_URI'], PHP_URL_PATH);                    
$uri = explode( '/', $uri );                                            // split on / into array
 
if (isset($uri[2]) && $uri[2] == 'user' && isset($uri[3])) {            // module name user
    require PROJECT_ROOT_PATH . "/Controller/Api/UserController.php";
 
    $objFeedController = new UserController();
    $strMethodName = $uri[3] . 'Action';                                // append uri[3] to Action (method)
    $objFeedController->{$strMethodName}();                             // call uri[3]Action
} else {
    header("HTTP/1.1 404 Not Found");
    exit();
}
 

?>