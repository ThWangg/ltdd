<?php
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST");

if ($_SERVER["REQUEST_METHOD"] !== "POST") {
    http_response_code(405);
    echo json_encode(["success" => false, "message" => "Chỉ chấp nhận phương thức POST"]);
    exit();
}

require_once "db_connect.php";

$customerName = isset($_POST["customer_name"]) ? trim($_POST["customer_name"]) : "";
$phoneNumber  = isset($_POST["phone_number"]) ? trim($_POST["phone_number"]) : "";
$totalPrice   = isset($_POST["total_price"]) ? $_POST["total_price"] : "";
$status       = isset($_POST["status"]) ? trim($_POST["status"]) : "";

if ($customerName === "" || $phoneNumber === "" || $totalPrice === "" || $status === "") {
    http_response_code(400);
    echo json_encode(["success" => false, "message" => "Vui lòng điền đầy đủ tất cả các trường"]);
    $connection->close();
    exit();
}

$statement = $connection->prepare(
    "INSERT INTO `Order` (customer_name, phone_number, total_price, status) VALUES (?, ?, ?, ?)"
);

$statement->bind_param("ssds", $customerName, $phoneNumber, $totalPrice, $status);

$isSuccess = $statement->execute();

if ($isSuccess) {
    echo json_encode(["success" => true, "message" => "Thêm đơn hàng thành công", "inserted_id" => $connection->insert_id]);
} else {
    http_response_code(500);
    echo json_encode(["success" => false, "message" => "Thêm đơn hàng thất bại: " . $statement->error]);
}

$statement->close();
$connection->close();
?>
