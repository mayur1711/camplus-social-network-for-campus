<?php

$connect = mysqli_connect("localhost", "id2881934_camplus", "camplus", "id2881934_camplus");
if($connect){
    echo "connection success";
}
else{
    echo "connection failure";
}

$user_Id = $_POST["user_id"];
$name = $_POST["name"];
$contact_no = $_POST["contact_no"];
$year = $_POST["year"];
$department = $_POST["department"];

$statement = mysqli_prepare($connect, "UPDATE user SET name = '$name', contact_no = '$contact_no',
    year = '$year', department = '$department' WHERE user_id = $user_Id");
mysqli_stmt_execute($statement);
if ($statement) {
    echo "Update Successful";
}
else {
    echo "Update Failed";
}
mysqli_stmt_close($statement);

?>
