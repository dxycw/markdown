package com.markdown.compose

import org.intellij.markdown.IElementType
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.flavours.gfm.GFMElementTypes
import org.intellij.markdown.flavours.gfm.GFMTokenTypes

internal object MarkdownNodeTypes {

    // ========== 块级元素 ==========
    fun isHeading(type: IElementType): Boolean = type in setOf(
        MarkdownElementTypes.ATX_1, MarkdownElementTypes.ATX_2,
        MarkdownElementTypes.ATX_3, MarkdownElementTypes.ATX_4,
        MarkdownElementTypes.ATX_5, MarkdownElementTypes.ATX_6,
        MarkdownElementTypes.SETEXT_1, MarkdownElementTypes.SETEXT_2
    )

    fun getHeadingLevel(type: IElementType): Int = when (type) {
        MarkdownElementTypes.ATX_1, MarkdownElementTypes.SETEXT_1 -> 1
        MarkdownElementTypes.ATX_2, MarkdownElementTypes.SETEXT_2 -> 2
        MarkdownElementTypes.ATX_3 -> 3
        MarkdownElementTypes.ATX_4 -> 4
        MarkdownElementTypes.ATX_5 -> 5
        MarkdownElementTypes.ATX_6 -> 6
        else -> 0
    }

    fun isList(type: IElementType): Boolean = type in setOf(
        MarkdownElementTypes.UNORDERED_LIST,
        MarkdownElementTypes.ORDERED_LIST
    )

    fun isListItem(type: IElementType): Boolean = type == MarkdownElementTypes.LIST_ITEM

    fun isParagraph(type: IElementType): Boolean = type == MarkdownElementTypes.PARAGRAPH

    fun isCodeBlock(type: IElementType): Boolean = type in setOf(
        MarkdownElementTypes.CODE_FENCE,
        MarkdownElementTypes.CODE_BLOCK
    )

    fun isBlockQuote(type: IElementType): Boolean = type == MarkdownElementTypes.BLOCK_QUOTE

    fun isHorizontalRule(type: IElementType): Boolean = type == MarkdownTokenTypes.HORIZONTAL_RULE

    fun isHtmlBlock(type: IElementType): Boolean = type == MarkdownElementTypes.HTML_BLOCK

    fun isTable(type: IElementType): Boolean = type == GFMElementTypes.TABLE // GFM

    // ========== 行内元素 ==========
    fun isStrong(type: IElementType): Boolean = type == MarkdownElementTypes.STRONG

    fun isEmphasis(type: IElementType): Boolean = type == MarkdownElementTypes.EMPH

    fun isInlineCode(type: IElementType): Boolean = type == MarkdownElementTypes.CODE_SPAN

    fun isLink(type: IElementType): Boolean = type in setOf(
        MarkdownElementTypes.INLINE_LINK,
        MarkdownElementTypes.FULL_REFERENCE_LINK,
        MarkdownElementTypes.SHORT_REFERENCE_LINK,
        MarkdownElementTypes.AUTOLINK
    )

    fun isImage(type: IElementType): Boolean = type == MarkdownElementTypes.IMAGE

    fun isLineBreak(type: IElementType): Boolean = type == MarkdownTokenTypes.EOL

    fun isText(type: IElementType): Boolean = type == MarkdownTokenTypes.TEXT

    fun isWhiteSpace(type: IElementType): Boolean = type == MarkdownTokenTypes.WHITE_SPACE

    // ========== 链接相关子节点 ==========
    fun isLinkText(type: IElementType): Boolean = type == MarkdownElementTypes.LINK_TEXT

    fun isLinkDestination(type: IElementType): Boolean = type == MarkdownElementTypes.LINK_DESTINATION

    fun isLinkLabel(type: IElementType): Boolean = type == MarkdownElementTypes.LINK_LABEL

    fun isLinkDefinition(type: IElementType): Boolean = type == MarkdownElementTypes.LINK_DEFINITION

    // ========== 代码围栏相关 ==========
    fun isCodeFenceStart(type: IElementType): Boolean = type == MarkdownTokenTypes.CODE_FENCE_START

    fun isCodeFenceEnd(type: IElementType): Boolean = type == MarkdownTokenTypes.CODE_FENCE_END

    fun isCodeFenceContent(type: IElementType): Boolean = type == MarkdownTokenTypes.CODE_FENCE_CONTENT

    // ========== 列表标记 ==========
    fun isListBullet(type: IElementType): Boolean = type == MarkdownTokenTypes.LIST_BULLET

    fun isListNumber(type: IElementType): Boolean = type == MarkdownTokenTypes.LIST_NUMBER

    // ========== 表格相关 (GFM) ==========
    fun isTableHeader(type: IElementType): Boolean = type == GFMElementTypes.TABLE

    fun isTableRow(type: IElementType): Boolean = type == GFMElementTypes.TABLE

    fun isTableCell(type: IElementType): Boolean = type == GFMElementTypes.TABLE

    fun isTableSeparator(type: IElementType): Boolean = type == GFMTokenTypes.TABLE_SEPARATOR
}