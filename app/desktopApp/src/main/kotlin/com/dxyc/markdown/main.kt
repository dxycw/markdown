package com.dxyc.markdown

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.mikepenz.markdown.sample.App
import com.mikepenz.markdown.sample.aboutlibraries

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Markdown",
    ) {
        App(aboutlibraries())
    }
}