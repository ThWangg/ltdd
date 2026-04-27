package com.example.gk

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gk.model.Order
import com.example.gk.model.RetrofitClient
import com.example.gk.ui.theme.GkTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UpdateActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val receivedOrderId           = intent.getIntExtra("order_id", 0)
        val receivedCustomerName      = intent.getStringExtra("order_customer_name") ?: ""
        val receivedPhoneNumber       = intent.getStringExtra("order_phone_number") ?: ""
        val receivedTotalPrice        = intent.getDoubleExtra("order_total_price", 0.0)
        val receivedStatus            = intent.getStringExtra("order_status") ?: ""

        val receivedOrder = Order(
            id           = receivedOrderId,
            customerName = receivedCustomerName,
            phoneNumber  = receivedPhoneNumber,
            totalPrice   = receivedTotalPrice,
            status       = receivedStatus
        )

        setContent {
            GkTheme {
                UpdateScreen(
                    initialOrder = receivedOrder,
                    onUpdate = { updatedOrder ->
                        handleUpdateOrder(updatedOrder)
                    },
                    onDelete = { orderId ->
                        handleDeleteOrder(orderId)
                    },
                    onCancel = {
                        finish()
                    }
                )
            }
        }
    }

    private fun handleUpdateOrder(order: Order) {
        if (order.customerName.isBlank()) {
            Toast.makeText(this, "Vui lòng nhập tên khách hàng", Toast.LENGTH_SHORT).show()
            return
        }

        if (order.phoneNumber.isBlank()) {
            Toast.makeText(this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show()
            return
        }

        if (order.totalPrice <= 0) {
            Toast.makeText(this, "Tổng tiền phải lớn hơn 0", Toast.LENGTH_SHORT).show()
            return
        }


        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.apiService.updateOrder(
                    id           = order.id,
                    customerName = order.customerName,
                    phoneNumber  = order.phoneNumber,
                    totalPrice   = order.totalPrice,
                    status       = order.status
                )

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null && body.success) {
                            Toast.makeText(this@UpdateActivity, "Cập nhật thành công!", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            val errorMessage = body?.message ?: "Cập nhật thất bại"
                            Toast.makeText(this@UpdateActivity, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@UpdateActivity, "Lỗi kết nối: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@UpdateActivity, "Lỗi: ${exception.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun handleDeleteOrder(orderId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.apiService.deleteOrder(id = orderId)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null && body.success) {
                            Toast.makeText(this@UpdateActivity, "Xóa đơn hàng thành công!", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            val errorMessage = body?.message ?: "Xóa thất bại"
                            Toast.makeText(this@UpdateActivity, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@UpdateActivity, "Lỗi kết nối: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@UpdateActivity, "Lỗi: ${exception.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateScreen(
    initialOrder: Order,
    onUpdate: (Order) -> Unit,
    onDelete: (Int) -> Unit,
    onCancel: () -> Unit
) {
    var customerNameInput by remember { mutableStateOf(initialOrder.customerName) }
    var phoneNumberInput by remember { mutableStateOf(initialOrder.phoneNumber) }
    var totalPriceInput by remember { mutableStateOf(initialOrder.totalPrice.toString()) }

    val statusOptions = listOf("Hoàn thành", "Đang giao", "Đã hủy")
    var selectedStatus by remember {
        val matchedOption = statusOptions.find { option -> option == initialOrder.status }
        if (matchedOption != null) {
            mutableStateOf(matchedOption)
        } else {
            mutableStateOf(statusOptions[0])
        }
    }
    // Biến kiểm soát trạng thái đóng/mở của dropdown
    var isDropdownExpanded by remember { mutableStateOf(false) }

    var totalPriceError by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Cập nhật Đơn Hàng #${initialOrder.id}") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = customerNameInput,
                onValueChange = { newValue -> customerNameInput = newValue },
                label = { Text(text = "Tên khách hàng") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = phoneNumberInput,
                onValueChange = { newValue -> phoneNumberInput = newValue },
                label = { Text(text = "Số điện thoại") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = totalPriceInput,
                onValueChange = { newValue ->
                    totalPriceInput = newValue
                    totalPriceError = ""
                },
                label = { Text(text = "Tổng tiền (đ)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = totalPriceError.isNotEmpty(),
                supportingText = {
                    if (totalPriceError.isNotEmpty()) {
                        Text(
                            text = totalPriceError,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            ExposedDropdownMenuBox(
                expanded = isDropdownExpanded,
                onExpandedChange = { newExpandedState -> isDropdownExpanded = newExpandedState },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedStatus,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(text = "Trạng thái") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false }
                ) {
                    statusOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(text = option) },
                            onClick = {
                                selectedStatus = option
                                isDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val parsedPrice = totalPriceInput.toDoubleOrNull()

                    if (parsedPrice == null) {
                        totalPriceError = "Tổng tiền phải là số hợp lệ"
                    } else {
                        val updatedOrder = Order(
                            id           = initialOrder.id,
                            customerName = customerNameInput,
                            phoneNumber  = phoneNumberInput,
                            totalPrice   = parsedPrice,
                            status       = selectedStatus
                        )
                        onUpdate(updatedOrder)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Cập nhật")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { onDelete(initialOrder.id) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(text = "Xóa đơn hàng này")
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Hủy")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun UpdateScreenPreview() {
    val fakeOrder = Order(
        id           = 1,
        customerName = "Nguyễn Văn A",
        phoneNumber  = "0901234567",
        totalPrice   = 150000.0,
        status       = "Pending"
    )
    GkTheme {
        UpdateScreen(
            initialOrder = fakeOrder,
            onUpdate     = {},
            onDelete     = {},
            onCancel     = {}
        )
    }
}
