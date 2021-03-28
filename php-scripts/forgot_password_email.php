<?php

$connect = mysqli_connect("localhost", "id2881934_camplus", "camplus", "id2881934_camplus");
if($connect){
    echo "connection success";
}
else{
    echo "connection failure";
}

$email = $_POST["email"];
$otpGenerated = mt_rand(1000,9999);

function updateForgotPasswordTable() {
    global $connect, $email, $otpGenerated;
    $statement = mysqli_prepare($connect, "INSERT INTO forgot_password (email, otp) VALUES (?, ?) ON DUPLICATE KEY UPDATE otp = $otpGenerated");
    mysqli_stmt_bind_param($statement, "si", $email, $otpGenerated);
    mysqli_stmt_execute($statement);
    mysqli_stmt_close($statement);
}

function emailValid() {
    global $connect, $email;
    $statement = mysqli_prepare($connect, "SELECT * FROM user WHERE email = ?");
    mysqli_stmt_bind_param($statement, "s", $email);
    mysqli_stmt_execute($statement);
    mysqli_stmt_store_result($statement);
    $count = mysqli_stmt_num_rows($statement);
    mysqli_stmt_close($statement);
    return $count;
}

$response = array();
$response["otpGenerated"] = $otpGenerated;
$response["success"] = false;

if (emailValid() >= 1) {
    updateForgotPasswordTable();
    $response["success"] = true;
}

echo json_encode($response);

?>
