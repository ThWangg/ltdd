<?php
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");

require_once "db_connect.php";

$sql = "SELECT id, customer_name, phone_number, total_price, status FROM `Order` ORDER BY id DESC";

$result = $connection->query($sql);

if ($result === false) {
    http_response_code(500);
    echo json_encode(["success" => false, "message" => "Lỗi truy vấn: " . $connection->error]);
    $connection->close();
    exit();
}

$orderList = [];
while ($row = $result->fetch_assoc()) {
    $orderList[] = $row;
}

echo json_encode(["success" => true, "data" => $orderList]);

$connection->close();
?>
