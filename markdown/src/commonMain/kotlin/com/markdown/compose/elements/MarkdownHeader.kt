package com.markdown.compose.elements

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode

@Composable
internal fun MarkdownHeader(
    src: String,
    node: ASTNode,
    level: Int,
) {
    Text(
        text = extractText(src, node),
        style = when (level) {
            1 -> MaterialTheme.typography.displayLarge
            2 -> MaterialTheme.typography.displayMedium
            3 -> MaterialTheme.typography.displaySmall
            4 -> MaterialTheme.typography.headlineLarge
            5 -> MaterialTheme.typography.headlineMedium
            else -> MaterialTheme.typography.headlineSmall
        },
        modifier = Modifier.padding(
            vertical = when (level) {
                1 -> 8.dp
                2 -> 7.dp
                3 -> 6.dp
                4 -> 5.dp
                5 -> 4.dp
                else -> 3.dp
            }
        )
    )
}
/** 提取标题文本（去掉 # 符号） */
private fun extractText(src: String, node: ASTNode): String {
    return node.getTextInNode(src).toString()
        .replace(Regex("^#+\\s*"), "")  // 去掉开头的 # 和空格
        .trim()
}