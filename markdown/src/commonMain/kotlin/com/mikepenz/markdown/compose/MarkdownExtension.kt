package com.mikepenz.markdown.compose

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.mikepenz.markdown.compose.components.MarkdownComponentModel
import com.mikepenz.markdown.compose.components.MarkdownComponents
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.flavours.gfm.GFMElementTypes

/**
 * Handles the rendering of a markdown element based on its [ASTNode.type].
 *
 * This function is responsible for determining the appropriate component to use for rendering
 * It does handle rendering of children recursively.
 *
 * @param node The ASTNode representing the markdown element.
 * @param components The [MarkdownComponents] instance containing the components to use.
 * @param content The original markdown content string.
 * @param includeSpacer Whether to include a spacer before rendering the element.
 */
@Composable
fun MarkdownElement(
    node: ASTNode,
    components: MarkdownComponents,
    content: String,
    includeSpacer: Boolean = true,
) = MarkdownElementInternal(node, components, content, includeSpacer)

@Composable
internal fun MarkdownElementInternal(
    node: ASTNode,
    components: MarkdownComponents,
    content: CharSequence,
    includeSpacer: Boolean = true,
) {
    val typography = LocalMarkdownTypography.current
    val model = remember(node, content, typography) {
        // It's safe to pass `CharSequence` and its `toString` here.
        // Reason: It's guaranteed that even the source `StringBuilder` changes, The render result is not dirty.
        // So it's fine to remember it.
        MarkdownComponentModel(
            content = content.toString(),
            node = node,
            typography = typography,
        )
    }
    var handled = true
    if (includeSpacer) Spacer(Modifier.height(LocalMarkdownPadding.current.block))
    when (node.type) {
        MarkdownTokenTypes.TEXT -> components.text(model)
        MarkdownTokenTypes.EOL -> components.eol(model)
        MarkdownElementTypes.CODE_FENCE -> components.codeFence(model)
        MarkdownElementTypes.CODE_BLOCK -> components.codeBlock(model)
        MarkdownElementTypes.ATX_1 -> components.heading1(model)
        MarkdownElementTypes.ATX_2 -> components.heading2(model)
        MarkdownElementTypes.ATX_3 -> components.heading3(model)
        MarkdownElementTypes.ATX_4 -> components.heading4(model)
        MarkdownElementTypes.ATX_5 -> components.heading5(model)
        MarkdownElementTypes.ATX_6 -> components.heading6(model)
        MarkdownElementTypes.SETEXT_1 -> components.setextHeading1(model)
        MarkdownElementTypes. SETEXT_2 -> components.setextHeading2(model)
        MarkdownElementTypes.BLOCK_QUOTE -> components.blockQuote(model)
        MarkdownElementTypes.PARAGRAPH -> components.paragraph(model)
        MarkdownElementTypes.ORDERED_LIST -> components.orderedList(model)
        MarkdownElementTypes.UNORDERED_LIST -> components.unorderedList(model)
        MarkdownElementTypes.IMAGE -> components.image(model)
        MarkdownTokenTypes.HORIZONTAL_RULE -> components.horizontalRule(model)
        GFMElementTypes.TABLE -> components.table(model)
        else -> {
            handled = components.custom?.invoke(node.type, model) != null
        }
    }

    if (!handled) {
        node.children.forEach { child ->
            MarkdownElementInternal(child, components, content, includeSpacer)
        }
    }
}
