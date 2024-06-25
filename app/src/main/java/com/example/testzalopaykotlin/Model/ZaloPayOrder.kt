package com.example.testzalopaykotlin.Model

import com.example.testzalopaykotlin.Constant.AppInfo
import com.example.testzalopaykotlin.Helper.HMac.Helpers
import java.util.Date

data class ZaloPayOrder(
    var AppId: String = AppInfo.APP_ID.toString(),
    var AppUser: String = "Android_Demo",
    var AppTime: String = Date().time.toString(),
    var Amount: String,
    var AppTransId: String = Helpers.getAppTransId(),
    var EmbedData: String = "{}",
    var Items: String = "[]",
    var BankCode: String = "zalopayapp",
    var Description: String = "Merchant pay for order #${Helpers.getAppTransId()}",
    var Mac: String = Helpers.getMac(
        AppInfo.MAC_KEY,
        data = "$AppId|$AppTransId|$AppUser|$Amount|$AppTime|$EmbedData|$Items"
    )
)
