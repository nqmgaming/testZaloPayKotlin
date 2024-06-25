package com.example.testzalopaykotlin.presentation

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.testzalopaykotlin.data.api.CreateOrder
import com.example.testzalopaykotlin.core.constant.AppInfo.APP_ID
import kotlinx.coroutines.launch
import vn.zalopay.sdk.Environment
import vn.zalopay.sdk.ZaloPayError
import vn.zalopay.sdk.ZaloPaySDK
import vn.zalopay.sdk.listeners.PayOrderListener

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        setContent {
            ZaloPayIntegrationScreen()
        }
        try {
            ZaloPaySDK.init(APP_ID, Environment.SANDBOX)
            Log.d("ZaloPayInit", "ZaloPay SDK initialized successfully")
        } catch (e: Exception) {
            Log.e("ZaloPayInitError", "Error initializing ZaloPay SDK: ${e.message}")
            e.printStackTrace()
        }
    }

    @Composable
    fun ZaloPayIntegrationScreen() {
        var paymentResult by remember { mutableStateOf("") }

        var totalString by remember { mutableStateOf("") }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = totalString,
                onValueChange = { totalString = it },
                label = { Text("Nhập số tiền") },
                modifier = Modifier.padding(16.dp)
            )
            Button(
                onClick = {
                    Log.d("ZaloPay", "Button clicked")
                    val orderApi = CreateOrder()

                    lifecycleScope.launch {
                        try {
                            val data = orderApi.createOrder(totalString)
                            Log.d("Amount", totalString)

                            Log.d("ZaloPay", "Order created successfully: $data")
                            val code = data.getString("return_code")
                            Log.d("code", "Order created successfully: $code")

                            if (code == "1") {
                                val token = data.getString("zp_trans_token")
                                ZaloPaySDK.getInstance().payOrder(
                                    this@MainActivity,
                                    token,
                                    "demozpdk://app",
                                    object : PayOrderListener {
                                        override fun onPaymentSucceeded(payUrl: String?, transToken: String?, appTransID: String?) {
                                            paymentResult = "Thanh toán thành công"
                                            Log.d("ZaloPay", "Payment succeeded: payUrl=$payUrl, transToken=$transToken, appTransID=$appTransID")
                                        }

                                        override fun onPaymentCanceled(payUrl: String?, transToken: String?) {
                                            paymentResult = "Hủy thanh toán"
                                            Log.d("ZaloPay", "Payment canceled: payUrl=$payUrl, transToken=$transToken")
                                        }

                                        override fun onPaymentError(error: ZaloPayError?, payUrl: String?, transToken: String?) {
                                            paymentResult = "Lỗi thanh toán"
                                            Log.e("ZaloPayError", "Payment error: payUrl=$payUrl, transToken=$transToken")
                                        }
                                    })

                                paymentResult = "Đã tạo đơn hàng thành công và đang đợi đơn hàng được thanh toán"
                            }else{
                               Toast.makeText(this@MainActivity, "Failed to create order", Toast.LENGTH_SHORT).show()
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                            Log.e("ZaloPayError", "Exception: ${e.message}")
                        }
                    }
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Thanh toán qua ZaloPay")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = paymentResult)
        }
    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        ZaloPaySDK.getInstance().onResult(intent)
    }
}