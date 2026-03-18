package ltdd.weekfive.artspace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ltdd.weekfive.artspace.ui.theme.ArtSpaceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ArtSpaceTheme {
                ArtSpaceLayout()
                }
            }
        }
    }

@Composable
fun ImageAndText(imagesource: Int, title: String, year: String, subtitle: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(imagesource),
            contentDescription = null,
            modifier = Modifier
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(30.dp)
                )
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(30.dp)
                )
                .padding(30.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = modifier
                .background(colorResource(R.color.light_gray))
                .padding(25.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = title,
                fontSize = 21.sp,
                modifier = modifier
                    .padding(bottom = 8.dp),

                )
            Text(
                text = subtitle + " (" + year + ")",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Left,
                modifier = modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.Start)
            )
        }
    }
}

@Composable
fun ArtSpaceLayout(modifier: Modifier = Modifier){
    val imageStep = remember { mutableStateOf(1) }
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (imageStep.value) {
            1 -> ImageAndText(
                imagesource = R.drawable.golden_bridge,
                title = "Golden bridge",
                subtitle = "Da nang",
                year = "2017",
            )
            2 -> ImageAndText(
                imagesource = R.drawable.co_do_hue,
                title = "Co do Hue",
                subtitle = "Hue",
                year = "1832",
            )

            3 -> ImageAndText(
                imagesource = R.drawable.pho_co,
                title = "Pho Co",
                subtitle = "Da nang",
                year = "XVII - XVII",
            )
        }

        Row(
            modifier = modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Button(onClick = {
                imageStep.value--
                if (imageStep.value < 1) {
                    imageStep.value = 3
                }
            }) {
                Text(text = "Previous")
            }
            Spacer(modifier = Modifier.height(30.dp))
            Button(onClick = {
                imageStep.value++
                if (imageStep.value > 3) {
                    imageStep.value = 1
                }
            }) {
                Text(text = "Next")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ArtSpacePreview() {
    ArtSpaceTheme {
        ArtSpaceLayout()
    }
}
