package com.dxyc.markdown

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mikepenz.markdown.sample.App
import com.mikepenz.markdown.sample.aboutlibraries

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
//            App()
            App(aboutlibraries())
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}