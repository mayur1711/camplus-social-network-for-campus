<?php
$connect = mysqli_connect("localhost", "id2881934_camplus", "camplus", "id2881934_camplus");

if($connect){
    echo "connection success";
}
else{
    echo "connection failure";
}

$tagId = $_POST["tag_id"];
$tagId = "(" . $tagId . ")";

$response = array();

$statement =  mysqli_prepare($connect, "SELECT DISTINCT post_id FROM post_interest WHERE interest_id
    IN $tagId ORDER BY post_id ASC");
mysqli_stmt_execute($statement);
mysqli_stmt_store_result($statement);
mysqli_stmt_bind_result($statement, $getPost_Id);

while(mysqli_stmt_fetch($statement)) {

    $statement1 = mysqli_prepare($connect, "SELECT post.*, user.name, user.year from post INNER JOIN
        user ON post.user_id = user.user_id WHERE post_id = $getPost_Id ORDER BY post_id ASC");
    mysqli_stmt_execute($statement1);
    mysqli_stmt_store_result($statement1);
    mysqli_stmt_bind_result($statement1, $getPost_Id, $getUser_Id, $getTitle, $getContent, $getImage,
        $getLike_Count, $getComment_Count, $getTimestamp, $getFlag_Count, $getName, $getYear);

    while(mysqli_stmt_fetch($statement1)) {

        $temp = array();

        $statement2 = mysqli_prepare($connect, "SELECT post_interest.post_id, interest.tag
            from post_interest INNER JOIN interest ON interest.interest_id =
            post_interest.interest_id WHERE post_interest.post_id = $getPost_Id ORDER BY post_id ASC");
        mysqli_stmt_execute($statement2);
        mysqli_stmt_store_result($statement2);
        mysqli_stmt_bind_result($statement2, $getPost_Id, $getTag);

        $temp["tags"] = "";

        while(mysqli_stmt_fetch($statement2)) {
            $temp["tags"] .= $getTag . ", ";
        }

        $temp["name"] = $getName;
        $temp["year"] = $getYear;
        $temp["tags"] = substr($temp["tags"], 0, -2);
        $temp["post_id"] = $getPost_Id;
        $temp["user_id"] = $getUser_Id;
        $temp["title"] = $getTitle;
        $temp["content"] = $getContent;
        $temp["image"] = $getImage;
        $temp["like_count"] = $getLike_Count;
        $temp["comment_count"] = $getComment_Count;
        $temp["timestamp"] = date("M d, Y", strtotime($getTimestamp));

        array_push($response, $temp);

    }
}

echo json_encode($response);

?>
