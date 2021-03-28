<?php
$connect = mysqli_connect("localhost", "id2881934_camplus", "camplus", "id2881934_camplus");

if($connect){
    echo "connection success";
}
else{
    echo "connection failure";
}

$search = $_GET["search"];
$json=array();

$result = mysqli_query($connect, "SELECT * FROM user
    WHERE name LIKE '%{$search}%'");

if ($result->num_rows >0) {
    while($row[] = $result->fetch_assoc()) {
        $tem = $row;
        $json = json_encode($tem);
    }
}
else {
    echo "No Results Found.";
}

echo $json;
$connect->close();
?>
