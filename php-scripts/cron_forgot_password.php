<?php

$connect = mysqli_connect("localhost", "id2881934_camplus", "camplus", "id2881934_camplus");
if($connect){
    echo "connection success";
}
else{
    echo "connection failure";
}

$statement = mysqli_prepare($connect, "DELETE FROM forgot_password WHERE timestamp < (NOW() - INTERVAL 10 MINUTE)");
mysqli_stmt_execute($statement);

?>
