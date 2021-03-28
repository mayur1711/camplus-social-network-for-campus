<?php

$connect = mysqli_connect("localhost", "id2881934_camplus", "camplus", "id2881934_camplus");
if($connect){
    echo "connection success";
}
else{
    echo "connection failure";
}

$post_Id = $_POST["post_id"];
$page = $_POST["page"];

$start = 0;
$limit = 10;

$total = mysqli_num_rows(mysqli_query($connect, "SELECT * from comment WHERE post_id = $post_Id"));

$page_limit = ceil($total/$limit);

if($page<=$page_limit){

    $start = ($page - 1) * $limit;

    $statement = mysqli_prepare($connect, "SELECT * from comment WHERE post_id = ? limit $start, $limit");
    mysqli_stmt_bind_param($statement, "i", $post_Id);
    mysqli_stmt_execute($statement);
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $getComment_Id, $getPost_Id,  $getUser_Id, $getData);

    $response = array();

    while(mysqli_stmt_fetch($statement)) {

        $temp = array();

        $statement1 = mysqli_prepare($connect, "SELECT * from user WHERE user_id = ?");
        mysqli_stmt_bind_param($statement1, "i", $getUser_Id);
        mysqli_stmt_execute($statement1);
        mysqli_stmt_store_result($statement1);
        mysqli_stmt_bind_result($statement1, $getUser_Id, $getName, $getEmail, $getUsername,
            $getContact_no, $getYear, $getDepartment, $getPassword);

        while(mysqli_stmt_fetch($statement1)) {
            $temp["name"] = $getName;
        }
        $temp["comment_id"] = $getComment_Id;
        $temp["post_id"] = $getPost_Id;
        $temp["user_id"] = $getUser_Id;
        $temp["data"] = $getData;

        array_push($response, $temp);
    }

    echo json_encode($response);
}
else {
    echo "over";
}

?>
