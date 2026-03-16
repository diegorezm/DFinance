package com.diegorezm.dfinance

import androidx.compose.ui.window.ComposeUIViewController
import com.diegorezm.dfinance.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}
