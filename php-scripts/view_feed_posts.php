<?php

$connect = mysqli_connect("localhost", "id2881934_camplus", "camplus", "id2881934_camplus");
if($connect){
    echo "connection success";
}
else{
    echo "connection failure";
}

//Getting the page number which is to be displayed
$page = $_GET["page"];

//Initially we show the data from 1st row that means the 0th row
$start = 0;

//Limit is 3 that means we will show 3 items at once
$limit = 5;

//Counting the total item available in the database
$total = mysqli_num_rows(mysqli_query($connect, "SELECT post_id from post "));

//We can go atmost to page number total/limit
$page_limit = ceil($total/$limit);

//If the page number is more than the limit we cannot show anything
if($page<=$page_limit){


    //Calculating start for every given page number
	$start = ($page - 1) * $limit;

	//SQL query to fetch data of a range
	$statement = mysqli_prepare($connect, "SELECT post.*, user.name, user.year
        from post INNER JOIN user ON post.user_id = user.user_id ORDER BY post_id
        ASC limit $start, $limit");
    //Getting result
    mysqli_stmt_execute($statement);
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $getPost_Id, $getUser_Id, $getTitle, $getContent, $getImage,
        $getLike_Count, $getComment_Count, $getTimestamp, $getFlag_Count, $getName, $getYear);

	//Adding results to an array
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

	//Displaying the array in json format
	echo json_encode($response);
}
else {
    echo "over";
}

?>
