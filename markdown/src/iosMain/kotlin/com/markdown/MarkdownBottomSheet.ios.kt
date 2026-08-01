package com.markdown

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.ui.platform.LocalWindowInfo

@Composable
actual fun Modifier.markdownBottomSheetHeight(): Modifier =
    this.fillMaxHeight(LocalWindowInfo.current.containerSize.height * 0.8f)