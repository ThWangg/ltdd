package com.example.lemonade

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lemonade.ui.theme.LemonadeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LemonadeTheme {
                LemonCard()
            }
        }
    }
}


var step = mutableStateOf(1)


@Composable
fun Heading(modifier: Modifier = Modifier) {
    Box(
        modifier
            .fillMaxWidth()
            .background(Color.Yellow)
            .padding(8.dp)
    ) {
        Text(
            text = "Lemonade",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.background(Color.Yellow).align(Alignment.Center)
        )
    }

    Spacer(Modifier.height(200.dp))
}

@Composable
fun CardInfo(image_id: Int, text_id: Int, modifier: Modifier = Modifier){
    Image(
        painter = painterResource(image_id),
        contentDescription = null,
        modifier = modifier
            .border(2.dp, Color(red = 105, green = 216, blue = 205), shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(color = Color(red = 105, green = 216, blue = 205))
            .clickable {
                if (step.value < 4) {
                    step.value++
                } else {
                    step.value = 1
                }
            }
    )

    Text(
        text = stringResource(text_id),
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier.padding(top = 18.dp)
    )
}
@Composable
fun LemonCard(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Heading()

        when (step.value) {
            1 -> CardInfo(R.drawable.lemon_tree, R.string.step_1)
            2 -> CardInfo(R.drawable.lemon_squeeze, R.string.step_2)
            3 -> CardInfo(R.drawable.lemon_drink, R.string.step_3)
            4 -> CardInfo(R.drawable.lemon_restart, R.string.step_4 )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LemonadeTheme {
        LemonCard()
    }
}