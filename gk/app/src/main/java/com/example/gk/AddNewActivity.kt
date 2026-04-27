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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.gk.model.RetrofitClient
import com.example.gk.ui.theme.GkTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddNewActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GkTheme {
                AddNewScreen(
                    onSave = { customerName, phoneNumber, totalPrice, status ->
                        handleSaveOrder(customerName, phoneNumber, totalPrice, status)
                    },
                    onCancel = {
                        finish()
                    }
                )
            }
        }
    }

    private fun handleSaveOrder(
        customerName: String,
        phoneNumber: String,
        totalPrice: Double,
        status: String
    ) {
        if (customerName.isBlank()) {
            Toast.makeText(this, "Vui lòng nhập tên khách hàng", Toast.LENGTH_SHORT).show()
            return
        }

        if (phoneNumber.isBlank()) {
            Toast.makeText(this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show()
            return
        }

        if (totalPrice <= 0) {
            Toast.makeText(this, "Tổng tiền phải lớn hơn 0", Toast.LENGTH_SHORT).show()
            return
        }


        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.apiService.addOrder(
                    customerName = customerName,
                    phoneNumber  = phoneNumber,
                    totalPrice   = totalPrice,
                    status       = status
                )

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null && body.success) {
                            Toast.makeText(this@AddNewActivity, "Thêm đơn hàng thành công", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            val errorMessage = body?.message ?: "Thêm thất bại"
                            Toast.makeText(this@AddNewActivity, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@AddNewActivity, "Lỗi kết nối: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AddNewActivity, "Lỗi: ${exception.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewScreen(
    onSave: (customerName: String, phoneNumber: String, totalPrice: Double, status: String) -> Unit,
    onCancel: () -> Unit
) {
    var customerNameInput by remember { mutableStateOf("") }
    var phoneNumberInput by remember { mutableStateOf("") }
    var totalPriceInput by remember { mutableStateOf("") }

    val statusOptions = listOf("Hoàn thành", "Đang giao", "Đã hủy")
    var selectedStatus by remember { mutableStateOf(statusOptions[0]) }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    var totalPriceError by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Thêm Đơn Hàng Mới") },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Quay lại"
                        )
                    }
                }
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
                        onSave(customerNameInput, phoneNumberInput, parsedPrice, selectedStatus)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Lưu")
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(text = "Hủy")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AddNewScreenPreview() {
    GkTheme {
        AddNewScreen(
            onSave   = { _, _, _, _ -> },
            onCancel = {}
        )
    }
}
