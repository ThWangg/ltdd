<?php
$serverName = "localhost";
$userName   = "root";
$password   = "";
$database   = "OrderDB";

// Tạo kết nối tới MySQL
$connection = new mysqli($serverName, $userName, $password, $database);

if ($connection->connect_error) {
    http_response_code(500);
    echo json_encode(["success" => false, "message" => "Kết nối cơ sở dữ liệu thất bại: " . $connection->connect_error]);
    exit();
}

$connection->set_charset("utf8");
?>
