package com.markdown

import android.annotation.SuppressLint
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
actual fun Modifier.markdownBottomSheetHeight(): Modifier =
    this.fillMaxHeight(LocalConfiguration.current.screenHeightDp * 0.8f)