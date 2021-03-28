<?php

$connect = mysqli_connect("localhost", "id2881934_camplus", "camplus", "id2881934_camplus");
if($connect){
    echo "connection success";
}
else{
    echo "connection failure";
}

$page = $_GET["page"];
$user_Id = $_GET["user_id"];

$start = 0;
$limit = 5;

$total = mysqli_num_rows(mysqli_query($connect, "SELECT DISTINCT post_id FROM comment WHERE
    comment.user_id = $user_Id"));

$page_limit = ceil($total/$limit);

if($page<=$page_limit){

    $start = ($page - 1) * $limit;

	$statement = mysqli_prepare($connect, "SELECT DISTINCT post.*, user.name, user.year FROM comment INNER JOIN post ON comment.post_id = post.post_id INNER JOIN user on user.user_id = post.user_id WHERE comment.user_id = $user_Id ORDER BY post_id ASC limit $start, $limit");
    mysqli_stmt_execute($statement);
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $getPost_Id, $getUser_Id, $getTitle, $getContent, $getImage,
        $getLike_Count, $getComment_Count, $getTimestamp, $getFlag_Count, $getName, $getYear);

	$response = array();

	while(mysqli_stmt_fetch($statement)) {

        $temp = array();

        $statement1 = mysqli_prepare($connect, "SELECT post_interest.post_id, interest.tag
            from post_interest INNER JOIN interest ON interest.interest_id =
            post_interest.interest_id WHERE post_interest.post_id = $getPost_Id ORDER BY post_id ASC");
        mysqli_stmt_execute($statement1);
        mysqli_stmt_store_result($statement1);
        mysqli_stmt_bind_result($statement1, $getPost_Id, $getTag);

        $temp["tags"] = "";

        while(mysqli_stmt_fetch($statement1)) {
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

	echo json_encode($response);
}
else {
    echo "over";
}

?>
