<?php

$connect = mysqli_connect("localhost", "id2881934_camplus", "camplus", "id2881934_camplus");

if($connect){
    echo "connection success";
}
else{
    echo "connection failure";
}

$post_Id = $_POST["post_id"];
$user_Id = $_POST["user_id"];
$data = $_POST["data"];

$statement = mysqli_prepare($connect, "INSERT INTO comment (post_id, user_id, data) VALUES (?,?,?)");
mysqli_stmt_bind_param($statement, "iis", $post_Id, $user_Id, $data);
mysqli_stmt_execute($statement);
mysqli_stmt_close($statement);

$statement1 = mysqli_prepare($connect, "UPDATE post SET comment_count = comment_count + 1
    WHERE post_id = $post_Id");
mysqli_stmt_execute($statement1);
mysqli_stmt_close($statement1);

echo "success";

?>
