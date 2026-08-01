package com.markdown.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.parser.MarkdownParser


@Composable
internal fun Markdown(
    src: String,
    modifier: Modifier = Modifier,
) {
    MarkdownContent(
        modifier = modifier,
        src = src,
    )
}


/**
 * 解析 Markdown 为 AST，并递归访问所有节点
 */
internal fun parseMarkdown(src: String, useGfm: Boolean = true): ASTNode {
    val flavour = if (useGfm) GFMFlavourDescriptor() else CommonMarkFlavourDescriptor()
    return MarkdownParser(flavour).buildMarkdownTreeFromString(src)
}

/**
 * 递归遍历 AST，对每个节点执行 action
 */
internal fun ASTNode.visitAll(action: (ASTNode, Int) -> Unit, depth: Int = 0) {
    action(this, depth)
    children.forEach { it.visitAll(action, depth + 1) }
}

/**
 * 获取节点在原始文本中的内容
 */
internal fun ASTNode.getTextIn(src: String): String {
    return src.substring(startOffset, endOffset)
}

/**
 * 获取节点的"纯文本"内容（过滤掉子节点中的标记符号）
 */
internal fun ASTNode.getPlainText(src: String): String {
    return if (children.isEmpty()) {
        getTextIn(src)
    } else {
        children.joinToString("") { it.getPlainText(src) }
    }
}