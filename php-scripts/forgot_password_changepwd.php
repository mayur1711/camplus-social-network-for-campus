<?php

require("password.php");

$connect = mysqli_connect("localhost", "id2881934_camplus", "camplus", "id2881934_camplus");
if($connect){
    echo "connection success";
}
else{
    echo "connection failure";
}

$email = $_POST["email"];
$password = $_POST["password"];
$passwordHash = password_hash($password, PASSWORD_DEFAULT);

$statement = mysqli_prepare($connect, "UPDATE user SET password = ? WHERE email = ?");
mysqli_stmt_bind_param($statement, 'ss', $passwordHash, $email);
mysqli_stmt_execute($statement);

$response = array();
$response["success"] = true;

echo json_encode($response);

?>
