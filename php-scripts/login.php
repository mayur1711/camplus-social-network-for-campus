<?php

require("password.php");

$connect = mysqli_connect("localhost", "id2881934_camplus", "camplus", "id2881934_camplus");
if($connect){
    echo "connection success";
}
else{
    echo "connection failure";
}

$username = $_POST["username"];
$password = $_POST["password"];

$statement = mysqli_prepare($connect, "SELECT * FROM user WHERE username = ?");
mysqli_stmt_bind_param($statement, "s", $username);
mysqli_stmt_execute($statement);
mysqli_stmt_store_result($statement);
mysqli_stmt_bind_result($statement, $getUser_Id, $getName, $getEmail, $getUsername,
    $getContact_no, $getYear, $getDepartment, $getPassword);

$reponse = array();
$response["success"] = false;

while (mysqli_stmt_fetch($statement)) {
    if (password_verify($password, $getPassword)){
        $response["success"] = true;
        $response["user_id"] = $getUser_Id;
        $response["name"] = $getName;
        $response["email"] = $getEmail;
        $response["username"] = $getUsername;
        $response["contact_no"] = $getContact_no;
        $response["year"] = $getYear;
        $response["department"] = $getDepartment;
    }
}

echo json_encode($response);

?>
