<?php

$connect = mysqli_connect("localhost", "id2881934_camplus", "camplus", "id2881934_camplus");
if($connect){
    echo "connection success";
}
else{
    echo "connection failure";
}

$otpEntered = $_POST["otpEntered"];
$email = $_POST["email"];

function otpCheck() {
    global $otpEntered, $connect, $email;
    $statement = mysqli_prepare($connect, "SELECT * FROM forgot_password WHERE email = ?");
    mysqli_stmt_bind_param($statement, 's', $email);
    mysqli_stmt_execute($statement);
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $getEmail, $getOtp, $getTimestamp);
    while (mysqli_stmt_fetch($statement)) {
        $timestamp = strtotime($getTimestamp);
        $otpGenerated = $getOtp;
        if ($otpEntered == $otpGenerated) {
            $currentTime = time();
            $difference = $currentTime - $timestamp;
            echo "$difference";
            if(($difference) < 300) {
                return true;
            }
            else {
                return false;
            }
        }
    }
}

$response = array();
$response["success"] = false;

if (otpCheck()) {
  $response["success"] = true;
}

echo json_encode($response);

?>
