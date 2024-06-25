package com.example.testzalopaykotlin.Api

import com.example.testzalopaykotlin.Constant.AppInfo
import com.example.testzalopaykotlin.Model.ZaloPayOrder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.RequestBody
import org.json.JSONObject

class CreateOrder {

    @Throws(Exception::class)
    suspend fun createOrder(amount: String): JSONObject {
        return withContext(Dispatchers.IO) {
            val input = ZaloPayOrder(Amount = amount)
            val formBody: RequestBody = FormBody.Builder()
                .add("app_id", input.AppId)
                .add("app_user", input.AppUser)
                .add("app_time", input.AppTime)
                .add("amount", input.Amount)
                .add("app_trans_id", input.AppTransId)
                .add("embed_data", input.EmbedData)
                .add("item", input.Items)
                .add("bank_code", input.BankCode)
                .add("description", input.Description)
                .add("mac", input.Mac)
                .build()

            HttpProvider.sendPost(AppInfo.URL_CREATE_ORDER, formBody)
        } ?: throw Exception("Failed to create order")
    }
}