package com.example.gk

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gk.model.Order
import com.example.gk.model.RetrofitClient
import com.example.gk.ui.theme.GkTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : ComponentActivity() {

    private val orderList = mutableStateListOf<Order>()

    private var isLoading = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadOrdersFromApi()

        setContent {
            GkTheme {
                MainScreen(
                    orderList       = orderList,
                    isLoading       = isLoading.value,
                    onAddClick      = { navigateToAddNewActivity() },
                    onItemClick     = { selectedOrder -> navigateToUpdateActivity(selectedOrder) }
                )
            }
        }
    }

    //tự động tải lại danh sách sau CRUD
    override fun onResume() {
        super.onResume()
        loadOrdersFromApi()
    }

    private fun loadOrdersFromApi() {
        isLoading.value = true

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.apiService.getAllOrders()

                withContext(Dispatchers.Main) {
                    isLoading.value = false

                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null && body.success) {
                            orderList.clear()
                            if (body.data != null) {
                                orderList.addAll(body.data)
                            }
                        } else {
                            val errorMessage = body?.message ?: "Lỗi không xác định"
                            Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "Lỗi kết nối: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    isLoading.value = false
                    Toast.makeText(this@MainActivity, "Lỗi: ${exception.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun navigateToAddNewActivity() {
        val intent = Intent(this, AddNewActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToUpdateActivity(order: Order) {
        val intent = Intent(this, UpdateActivity::class.java)
        intent.putExtra("order_id", order.id)
        intent.putExtra("order_customer_name", order.customerName)
        intent.putExtra("order_phone_number", order.phoneNumber)
        intent.putExtra("order_total_price", order.totalPrice)
        intent.putExtra("order_status", order.status)
        startActivity(intent)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    orderList: List<Order>,
    isLoading: Boolean,
    onAddClick: () -> Unit,
    onItemClick: (Order) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Danh sách Đơn hàng") }
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

            Button(
                onClick = onAddClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Thêm Đơn Hàng Mới")
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (orderList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Chưa có đơn hàng nào. Hãy thêm mới!")
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(orderList) { order ->
                        OrderItemCard(
                            order = order,
                            onItemClick = onItemClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OrderItemCard(
    order: Order,
    onItemClick: (Order) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(order) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Khách hàng: ${order.customerName}",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "SĐT: ${order.phoneNumber}")
            Spacer(modifier = Modifier.height(4.dp))

            Text(text = "Tổng tiền: ${order.totalPrice} đ")
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Trạng thái: ${order.status}",
                    color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}


@Preview(showBackground = true, name = "Màn hình chính - Có dữ liệu")
@Composable
fun MainScreenPreview() {
    val fakeOrderList = listOf(
        Order(id = 1, customerName = "Nguyễn Văn A", phoneNumber = "0901234567", totalPrice = 150000.0, status = "Pending"),
        Order(id = 2, customerName = "Trần Thị B", phoneNumber = "0912345678", totalPrice = 320000.0, status = "Completed"),
        Order(id = 3, customerName = "Lê Văn C", phoneNumber = "0987654321", totalPrice = 89000.0, status = "Cancelled")
    )
    GkTheme {
        MainScreen(
            orderList  = fakeOrderList,
            isLoading  = false,
            onAddClick = {},
            onItemClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenLoadingPreview() {
    GkTheme {
        MainScreen(
            orderList  = emptyList(),
            isLoading  = true,
            onAddClick = {},
            onItemClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenEmptyPreview() {
    GkTheme {
        MainScreen(
            orderList  = emptyList(),
            isLoading  = false,
            onAddClick = {},
            onItemClick = {}
        )
    }
}