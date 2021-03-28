<?php

$connect = mysqli_connect("localhost", "id2881934_camplus", "camplus", "id2881934_camplus");
if($connect){
    echo "connection success";
}
else{
    echo "connection failure";
}

$post_Id = $_POST["post_id"];

$statement = mysqli_prepare($connect, "UPDATE post SET like_count = like_count + 1 WHERE post_id = ?");
mysqli_stmt_bind_param($statement, "i", $post_Id);
mysqli_stmt_execute($statement);

?>
