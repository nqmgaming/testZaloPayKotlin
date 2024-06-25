package com.example.testzalopaykotlin.data.model

import com.example.testzalopaykotlin.core.constant.AppInfo
import com.example.testzalopaykotlin.core.helper.Helpers
import java.util.Date

data class ZaloPayOrder(
    var appId: String = AppInfo.APP_ID.toString(),
    var appUser: String = "Android_Demo",
    var appTime: String = Date().time.toString(),
    var amount: String,
    var appTransId: String = Helpers.getAppTransId(),
    var embedData: String = "{}",
    var items: String = "[]",
    var bankCode: String = "zalopayapp",
    var description: String = "Merchant pay for order #${Helpers.getAppTransId()}",
    var mac: String = Helpers.getMac(
        AppInfo.MAC_KEY,
        data = "$appId|$appTransId|$appUser|$amount|$appTime|$embedData|$items"
    )
)
