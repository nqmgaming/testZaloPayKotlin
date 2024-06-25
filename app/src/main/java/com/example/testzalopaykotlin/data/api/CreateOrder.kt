package com.example.testzalopaykotlin.data.api

import com.example.testzalopaykotlin.core.constant.AppInfo
import com.example.testzalopaykotlin.data.model.ZaloPayOrder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.RequestBody
import org.json.JSONObject

class CreateOrder {

    @Throws(Exception::class)
    suspend fun createOrder(amount: String): JSONObject {
        return withContext(Dispatchers.IO) {
            val input = ZaloPayOrder(amount = amount)
            val formBody: RequestBody = FormBody.Builder()
                .add("app_id", input.appId)
                .add("app_user", input.appUser)
                .add("app_time", input.appTime)
                .add("amount", input.amount)
                .add("app_trans_id", input.appTransId)
                .add("embed_data", input.embedData)
                .add("item", input.items)
                .add("bank_code", input.bankCode)
                .add("description", input.description)
                .add("mac", input.mac)
                .build()

            HttpProvider.sendPost(AppInfo.URL_CREATE_ORDER, formBody)
        } ?: throw Exception("Failed to create order")
    }
}