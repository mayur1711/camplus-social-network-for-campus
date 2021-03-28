<?php

require("password.php");

$connect = mysqli_connect("localhost", "id2881934_camplus", "camplus", "id2881934_camplus");
if($connect){
    echo "connection success";
}
else{
    echo "connection failure";
}

$name = $_POST["name"];
$email = $_POST["email"];
$username = $_POST["username"];
$contact_no = $_POST["contact_no"];
$year = $_POST["year"];
$department = $_POST["department"];
$password = $_POST["password"];

function registerUser() {
    global $connect, $name, $email, $username, $contact_no, $year, $department,
        $password;
    $passwordHash = password_hash($password, PASSWORD_DEFAULT);
    $statement = mysqli_prepare($connect, "INSERT INTO user (name, email, username,
        contact_no, year, department, password) VALUES (?, ?, ?, ?, ?, ?, ?)");
    mysqli_stmt_bind_param($statement, "sssssss", $name, $email, $username, $contact_no,
        $year, $department, $passwordHash);
    mysqli_stmt_execute($statement);
    mysqli_stmt_close($statement);
}

function usernameAvailable() {
    global $connect, $username;
    $statement = mysqli_prepare($connect, "SELECT * FROM user WHERE username = ?");
    mysqli_stmt_bind_param($statement, 's', $username);
    mysqli_stmt_execute($statement);
    mysqli_stmt_store_result($statement);
    $count = mysqli_stmt_num_rows($statement);
    mysqli_stmt_close($statement);
    if ($count < 1){
       return true;
    }else {
       return false;
    }
}

$response = array();
$response["success"] = false;

if (usernameAvailable()){
    registerUser();
    $response["success"] = true;
}

echo json_encode($response);

?>
