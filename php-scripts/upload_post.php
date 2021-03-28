<?php
$connect = mysqli_connect("localhost", "id2881934_camplus", "camplus", "id2881934_camplus");

if($connect){
    echo "connection success";
}
else{
    echo "connection failure";
}

$user_Id = $_POST["user_id"];
$title = $_POST["title"];
$content = $_POST["content"];
$image = $_POST["image"];
$tagId = $_POST["tag_id"];

$tagArray = explode(',', $tagId);

$statement =  mysqli_prepare($connect, "INSERT INTO post (user_id, title, content) VALUES (?, ?, ?)");
mysqli_stmt_bind_param($statement, "iss", $user_Id, $title, $content);
mysqli_stmt_execute($statement);
$DefaultId = mysqli_insert_id($connect);
mysqli_stmt_close($statement);

if ($image != "null") {
    $ImagePath = "images/$DefaultId.png";
    $ServerURL = "https://ajjainaakash.000webhostapp.com/$ImagePath";
    $statement1 = mysqli_prepare($connect, "UPDATE post SET image = '$ServerURL'  WHERE post_id = $DefaultId");
    mysqli_stmt_execute($statement1);
    file_put_contents($ImagePath, base64_decode($image));
    mysqli_stmt_close($statement1);
}

for ($i = 0; $i < count($tagArray); $i++) {
    $statement2 =  mysqli_prepare($connect, "INSERT INTO post_interest (post_id, interest_id) VALUES ($DefaultId, $tagArray[$i])");
    mysqli_stmt_execute($statement2);
    mysqli_stmt_close($statement2);
}

echo "success";

?>
 
