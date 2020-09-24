package com.frezzcoding.musicplayer.common.extensions

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*


fun Long.round(): String = (SimpleDateFormat("mm:ss")).format(Date(this))
