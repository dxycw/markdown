package com.markdown.compose

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode


@Composable
internal fun MarkdownContent(
    src: String,
    modifier: Modifier = Modifier
) {
    val root = remember(src) { parseMarkdown(src) }

    Column(
        modifier = modifier.padding(10.dp)
    ) {
        root.children.forEach { node ->
            RenderBlockNode(node, src)
        }
    }
}

@Composable
private fun RenderBlockNode(node: ASTNode, src: String) {
    when {
        MarkdownNodeTypes.isHeading(node.type) -> {
            val level = MarkdownNodeTypes.getHeadingLevel(node.type)
            val text = node.getPlainText(src).trimStart('#').trim()
            Text(
                text = text,
                style = when (level) {
                    1 -> MaterialTheme.typography.headlineLarge
                    2 -> MaterialTheme.typography.headlineMedium
                    3 -> MaterialTheme.typography.headlineSmall
                    4 -> MaterialTheme.typography.titleLarge
                    5 -> MaterialTheme.typography.titleMedium
                    else -> MaterialTheme.typography.titleSmall
                },
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        MarkdownNodeTypes.isParagraph(node.type) -> {
            val annotated = buildAnnotatedString {
                appendInlineNodes(node.children, src)
            }
            Text(
                text = annotated,
                style = MaterialTheme.typography.bodyLarge,
            )
        }

        MarkdownNodeTypes.isCodeBlock(node.type) -> {
            // 提取语言标识和代码内容
            val fenceInfo = node.children.find { it.type == MarkdownTokenTypes.FENCE_LANG }?.getTextInNode(src)?.toString()
            val codeContent = node.children
                .filter { it.type == MarkdownTokenTypes.CODE_FENCE_CONTENT || it.type == MarkdownTokenTypes.CODE_LINE }
                .joinToString("") { it.getTextInNode(src) }

            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                Column(Modifier.padding(12.dp)) {
                    fenceInfo?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.height(4.dp))
                    }
                    Text(
                        text = codeContent,
                        fontFamily = FontFamily.Monospace,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        MarkdownNodeTypes.isBlockQuote(node.type) -> {
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                shape = MaterialTheme.shapes.small,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .padding(start = 8.dp)
            ) {
                Column(Modifier.padding(12.dp)) {
                    node.children.forEach { child ->
                        if (!MarkdownNodeTypes.isText(child.type) || child.getTextInNode(src).toString().trim() != ">") {
                            RenderBlockNode(child, src)
                        }
                    }
                }
            }
        }

        MarkdownNodeTypes.isList(node.type) -> {
            val isOrdered = node.type == MarkdownElementTypes.ORDERED_LIST
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                node.children.filter { MarkdownNodeTypes.isListItem(it.type) }.forEachIndexed { index, item ->
                    Row(
                        modifier = Modifier.padding(vertical = 2.dp)
                    ) {

                        Text(
                            text = if (isOrdered) "${index + 1}. " else "• ",
                            style = MaterialTheme.typography.bodyLarge,
                            fontSize = if (isOrdered) TextUnit.Unspecified else 22.sp
                        )

                        Column {
                            item.children.forEach { child ->
                                if (!MarkdownNodeTypes.isListBullet(child.type) && !MarkdownNodeTypes.isListNumber(child.type)) {
                                    RenderBlockNode(child, src)
                                }
                            }
                        }
                    }
                }
            }
        }

        MarkdownNodeTypes.isHorizontalRule(node.type) -> {
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        }

        MarkdownNodeTypes.isTable(node.type) -> {
            RenderTable(node, src)
        }

        MarkdownNodeTypes.isHtmlBlock(node.type) -> {
            Text(
                text = "[HTML Block]",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }

        else -> {
            // 递归处理未识别的容器节点
            node.children.forEach { RenderBlockNode(it, src) }
        }
    }
}

/**
 * 构建行内元素的 AnnotatedString
 */
@Composable
private fun AnnotatedString.Builder.appendInlineNodes(nodes: List<ASTNode>, src: String) {
    nodes.forEach { node ->
        when {
            MarkdownNodeTypes.isText(node.type) || MarkdownNodeTypes.isWhiteSpace(node.type) -> {
                append(node.getTextInNode(src))
            }

            MarkdownNodeTypes.isStrong(node.type) -> {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    appendInlineNodes(node.children, src)
                }
            }

            MarkdownNodeTypes.isEmphasis(node.type) -> {
                withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                    appendInlineNodes(node.children, src)
                }
            }

            MarkdownNodeTypes.isInlineCode(node.type) -> {
                withStyle(
                    SpanStyle(
                        fontFamily = FontFamily.Monospace,
                        background = Color.LightGray.copy(alpha = 0.3f)
                    )
                ) {
                    // 去掉首尾的反引号
                    val text = node.getTextInNode(src).toString().trim('`')
                    append(text)
                }
            }

            MarkdownNodeTypes.isLink(node.type) -> {
                val linkText = node.children.find { MarkdownNodeTypes.isLinkText(it.type) }
                    ?.getPlainText(src)?.trim('[', ']') ?: ""
                val dest = node.children.find { MarkdownNodeTypes.isLinkDestination(it.type) }
                    ?.getTextInNode(src)?.toString()?.trim('(', ')')
                    ?: node.children.find { it.type == MarkdownElementTypes.LINK_DESTINATION }
                        ?.getTextInNode(src)?.toString()

                withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary, textDecoration = TextDecoration.Underline)) {
                    // 实际项目中可用 ClickableText 或 AnnotatedString 的 link 支持
                    append(linkText)
                }
            }

            MarkdownNodeTypes.isImage(node.type) -> {
                val alt = node.children.find { MarkdownNodeTypes.isLinkText(it.type) }
                    ?.getPlainText(src)?.trim('[', ']') ?: ""
                val url = node.children.find { MarkdownNodeTypes.isLinkDestination(it.type) }
                    ?.getTextInNode(src)?.toString()?.trim('(', ')') ?: ""

                withStyle(SpanStyle(color = MaterialTheme.colorScheme.secondary)) {
                    append("[图片: $alt]")
                }
                // 实际项目中可用 AsyncImage / Coil 加载 url
            }

            MarkdownNodeTypes.isLineBreak(node.type) -> {
                append("\n")
            }

            else -> {
                // 递归处理其他行内容器
                appendInlineNodes(node.children, src)
            }
        }
    }
}

/**
 * 渲染 GFM 表格
 */
@Composable
private fun RenderTable(node: ASTNode, src: String) {
    val rows = node.children.filter {
        MarkdownNodeTypes.isTableHeader(it.type) || MarkdownNodeTypes.isTableRow(it.type)
    }

    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .horizontalScroll(rememberScrollState())
    ) {
        rows.forEachIndexed { rowIndex, row ->
            val cells = row.children.filter { MarkdownNodeTypes.isTableCell(it.type) }
            Row {
                cells.forEach { cell ->
                    val cellText = cell.getPlainText(src).trim('|').trim()
                    val isHeader = rowIndex == 0
                    Surface(
                        color = if (isHeader) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                        modifier = Modifier.padding(1.dp)
                    ) {
                        Text(
                            text = cellText,
                            style = if (isHeader) MaterialTheme.typography.labelLarge else MaterialTheme.typography.bodyMedium,
                            fontWeight = if (isHeader) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

//@Composable
//fun MarkdownContent(
//    modifier: Modifier = Modifier,
//    src: String,
//    root: ASTNode
//) {
//    Column(modifier = modifier) {
//        renderNode(src, root)
//    }
//}
//
//@Suppress("ComposableNaming")
//@Composable
//internal fun renderNode(src: String, node: ASTNode) {
//    when (node.type) {
//        // 根节点
//        MarkdownElementTypes.MARKDOWN_FILE -> {
//            node.children.forEach { renderNode(src, it) }
//        }
//        // 标题1
//        MarkdownElementTypes.ATX_1 -> MarkdownHeader(src, node, 1)
//        // 标题2
//        MarkdownElementTypes.ATX_2 -> MarkdownHeader(src, node, 2)
//        // 标题3
//        MarkdownElementTypes.ATX_3 -> MarkdownHeader(src, node, 3)
//        // 标题4
//        MarkdownElementTypes.ATX_4 -> MarkdownHeader(src, node, 4)
//        // 标题5
//        MarkdownElementTypes.ATX_5 -> MarkdownHeader(src, node, 5)
//        // 标题6
//        MarkdownElementTypes.ATX_6 -> MarkdownHeader(src, node, 6)
//        // 无序列表
//        MarkdownElementTypes.UNORDERED_LIST -> MarkdownUnorderedList(src, node)
//        // 有序列表
//        MarkdownElementTypes.ORDERED_LIST -> {
//            Text(
//                text = node.getTextInNode(src).toString(),
//                style = MaterialTheme.typography.bodyLarge,
//                modifier = Modifier.padding(vertical = 2.dp)
//            )
//        }//MarkdownUnorderedList(src, node)
//        // 段落
//        MarkdownElementTypes.PARAGRAPH -> {
//            Text(
//                text = node.getTextInNode(src).toString(),
//                style = MaterialTheme.typography.bodyLarge,
//                modifier = Modifier.padding(vertical = 2.dp)
//            )
//        }
////        // 继续扩展：ATX_3~6、UNORDERED_LIST、ORDERED_LIST、CODE_FENCE 等
////        else -> {
////            // 默认直接输出文本
////            Text(text = node.getTextInNode(src).toString())
////        }
//    }
//}