package com.kohan.shared.armeria.converter.request.parser

import com.kohan.shared.armeria.converter.request.result.AccessDeviceInfo
import ua_parser.Parser

internal class AccessInfoParser {
    fun parse(
        accessIP: String,
        userAgent: String,
    ): AccessDeviceInfo {
        val accessDevice = Parser().parse(userAgent)
        return AccessDeviceInfo(
            ip = accessIP,
            userAgent = accessDevice.userAgent.family + accessDevice.userAgent.major,
            os = accessDevice.os.family + accessDevice.os.major,
            device = accessDevice.device.family,
        )
    }
}
