package com.markdown

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * 修复 com.mikepenz.markdown 的m2或m3在使用BottomSheet时先全屏后闪一下半屏的问题
 *
 * 注意：此函数建议在 [androidx.compose.foundation.layout.Column]、[androidx.compose.foundation.layout.Row] 、
 * [androidx.compose.foundation.layout.Box]等布局中使用，要包括[com.mikepenz.markdown.m2.Markdown]
 * 或 [com.mikepenz.markdown.m3.Markdown]组件。
 */
@Composable
expect fun Modifier.markdownBottomSheetHeight(): Modifier

