package com.practicum.playlistmaker.util

import android.os.Bundle

interface ArgsTransfer {

    fun postArgs(args:Bundle?)

    fun getArgs():Bundle?
}