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

$id = isset($_POST["id"]) ? intval($_POST["id"]) : 0;

if ($id <= 0) {
    http_response_code(400);
    echo json_encode(["success" => false, "message" => "ID đơn hàng không hợp lệ"]);
    $connection->close();
    exit();
}

$statement = $connection->prepare("DELETE FROM `Order` WHERE id = ?");

$statement->bind_param("i", $id);

$isSuccess = $statement->execute();

if ($isSuccess) {
    if ($statement->affected_rows > 0) {
        echo json_encode(["success" => true, "message" => "Xóa đơn hàng thành công"]);
    } else {
        echo json_encode(["success" => false, "message" => "Không tìm thấy đơn hàng có ID = " . $id]);
    }
} else {
    http_response_code(500);
    echo json_encode(["success" => false, "message" => "Xóa thất bại: " . $statement->error]);
}

$statement->close();
$connection->close();
?>
