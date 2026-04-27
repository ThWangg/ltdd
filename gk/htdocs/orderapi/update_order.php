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

$id           = isset($_POST["id"]) ? intval($_POST["id"]) : 0;
$customerName = isset($_POST["customer_name"]) ? trim($_POST["customer_name"]) : "";
$phoneNumber  = isset($_POST["phone_number"]) ? trim($_POST["phone_number"]) : "";
$totalPrice   = isset($_POST["total_price"]) ? $_POST["total_price"] : "";
$status       = isset($_POST["status"]) ? trim($_POST["status"]) : "";

if ($id <= 0) {
    http_response_code(400);
    echo json_encode(["success" => false, "message" => "ID đơn hàng không hợp lệ"]);
    $connection->close();
    exit();
}

if ($customerName === "" || $phoneNumber === "" || $totalPrice === "" || $status === "") {
    http_response_code(400);
    echo json_encode(["success" => false, "message" => "Vui lòng điền đầy đủ tất cả các trường"]);
    $connection->close();
    exit();
}

$statement = $connection->prepare(
    "UPDATE `Order` SET customer_name = ?, phone_number = ?, total_price = ?, status = ? WHERE id = ?"
);

$statement->bind_param("ssdsi", $customerName, $phoneNumber, $totalPrice, $status, $id);

$isSuccess = $statement->execute();

if ($isSuccess) {
    if ($statement->affected_rows > 0) {
        echo json_encode(["success" => true, "message" => "Cập nhật đơn hàng thành công"]);
    } else {
        echo json_encode(["success" => false, "message" => "Không tìm thấy đơn hàng có ID = " . $id]);
    }
} else {
    http_response_code(500);
    echo json_encode(["success" => false, "message" => "Cập nhật thất bại: " . $statement->error]);
}

$statement->close();
$connection->close();
?>
