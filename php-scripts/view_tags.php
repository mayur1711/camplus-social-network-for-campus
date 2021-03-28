<?php

$connect = mysqli_connect("localhost", "id2881934_camplus", "camplus", "id2881934_camplus");
if($connect){
    echo "connection success";
}
else{
    echo "connection failure";
}
$statement = mysqli_prepare($connect, "SELECT * from interest");
mysqli_stmt_execute($statement);
mysqli_stmt_store_result($statement);
mysqli_stmt_bind_result($statement, $getInterest_Id, $getTag);

$response = array();

while(mysqli_stmt_fetch($statement)) {

    $temp = array();
    $temp["interest_id"] = $getInterest_Id;
    $temp["tag"] = $getTag;
    array_push($response, $temp);
}

echo json_encode($response);
?>
