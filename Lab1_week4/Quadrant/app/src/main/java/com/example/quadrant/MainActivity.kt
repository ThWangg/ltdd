package com.example.quadrant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quadrant.ui.theme.QuadrantTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuadrantTheme {
            }
        }
    }
}

@Composable
fun FirstQuadrant(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize().background(Color(0xFFEADDFF)).padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.first_quadrant_title),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = stringResource(R.string.first_quadrant_description),
            textAlign = TextAlign.Justify
        )
    }
}

@Composable
fun SecondQuadrant(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize().background(Color(0xFFD0BCFF)).padding(16.dp)

    ) {
        Text(
            text = stringResource(R.string.second_quadrant_title),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = stringResource(R.string.second_quadrant_description),
            textAlign = TextAlign.Justify
        )
    }
}

@Composable
fun ThirdQuadrant(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize().background(Color(0xFFB69DF8)).padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.third_quadrant_title),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = stringResource(R.string.third_quadrant_description),
            textAlign = TextAlign.Justify
        )
    }
}

@Composable
fun FourthQuadrant(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize().background(Color(0xFFF6EDFF)).padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.fourth_quadrant_title),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
            )
        Text(
            text = stringResource(R.string.fourth_quadrant_description),
            textAlign = TextAlign.Justify
        )
    }
}

@Composable
fun QuadrantApp() {
    Column(Modifier.fillMaxSize()) {
        Row(Modifier.weight(1f)) {
            FirstQuadrant(Modifier.weight(1f))
            SecondQuadrant(Modifier.weight(1f))
        }

        Row(Modifier.weight(1f)) {
            ThirdQuadrant(Modifier.weight(1f))
            FourthQuadrant(Modifier.weight(1f))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuadrantPreview() {
    QuadrantTheme {
       QuadrantApp()
    }
}