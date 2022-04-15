package com.sachet.tipapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sachet.tipapp.ui.theme.TipAppTheme
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                var counter by remember {
                    mutableStateOf(0.0f)
                }
                Column() {
                    TopHeader(counter)
                    BodyCalculator(){
                        counter = it
                    }
                }
            }
        }
    }
}

/**
 * This is a container app
 */
@Composable
fun MyApp(content: @Composable () -> Unit) {
    TipAppTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            content()
        }
    }
}

@Composable
fun TopHeader(counter:Float){
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.UP
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(15.dp)
            .clip(shape = RoundedCornerShape(corner = CornerSize(12.dp))),
        color = Color.Magenta
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                ),
                text = "Total Per Person"
            )
            Text(
                style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold
                ),
                text = "$${df.format(counter)}"
            )
        }
    }
}

@Composable
fun BodyCalculator(tpp: (Float) -> Unit){
    val context = LocalContext.current
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.UP
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        shape = RoundedCornerShape(corner = CornerSize(10.dp)),
        elevation = 20.dp
    ) {
        var totalAmount by remember {
            mutableStateOf(TextFieldValue("0"))
        }
        var nopeople by remember {
            mutableStateOf(1)
        }
        var tipAmount by remember {
            mutableStateOf(0.0f)
        }
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CreateTextView(value = totalAmount){
                    totalAmount = it
                if (totalAmount.text != "") {
                    val value = (Integer.parseInt(totalAmount.text) + tipAmount) / nopeople
                    tpp(value)
                }
            }
            NumberPeopleInput(nopeople){
                if (it < 0 && nopeople > 1){
                    nopeople += it
                }else if(it >= 1){
                    nopeople += it
                }else{
                    toastDemo(context= context,message = "Number of people cannot be less than 1!")
                }
                if (totalAmount.text != "") {
                    val value = (Integer.parseInt(totalAmount.text) + tipAmount) / nopeople
                    tpp(value)
                }
            }
            TipText(tipAmount = df.format(tipAmount).toFloat())
            ChangeTip(){
                tipAmount = (it.toInt() * Integer.parseInt(totalAmount.text) * 0.01).toFloat()
                if (totalAmount.text != "") {
                    val value = (Integer.parseInt(totalAmount.text) + tipAmount) / nopeople
                    tpp(df.format(value).toFloat())
                }
            }
        }
    }
}

@Composable
fun CreateTextView(value: TextFieldValue, valueChange: (TextFieldValue) -> Unit){
    OutlinedTextField(
        value = value,
        onValueChange = {
            valueChange(it)
        },
        label = { Text(text = "Enter Bill")},
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
    )
}

@Composable
fun NumberPeopleInput(nopeople:Int, changeCount: (Int) -> Unit){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .padding(
                    start = 10.dp,
                    end = 150.dp
                ),
            text = "Split"
        )
        Card(
            modifier = Modifier
                .size(45.dp)
                .clickable { changeCount(-1) },
            shape = CircleShape,
            elevation = 15.dp,
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(style = TextStyle(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 30.sp
                ),text = "-")
            }
        }
        Text(
            modifier = Modifier.padding(start = 10.dp, end = 10.dp),
            text = nopeople.toString()
        )
        Card(
            modifier = Modifier
                .size(45.dp)
                .clickable { changeCount(1) },
            shape = CircleShape,
            elevation = 15.dp
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp
                ),text = "+")
            }
        }
    }
}

@Composable
fun TipText(tipAmount: Float){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .padding(
                    start = 10.dp,
                    end = 200.dp
                ),
            text = "Tip"
        )
        Text(
            text = "$${tipAmount}",
            style = TextStyle(fontWeight = FontWeight.Bold)
        )
    }
}

fun toastDemo(context: Context,message: String) {

                Toast.makeText(
                    context,
                    message,
                    Toast.LENGTH_SHORT
                ).show()
}

@Composable
fun ChangeTip(setTipAmount: (Float) -> Unit){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var sliderValue by remember {
            mutableStateOf(0.0f)
        }
            Text(
                text = "${sliderValue.toInt()}%",
                style = TextStyle(fontWeight = FontWeight.Bold)
            )
        Slider(
            value = sliderValue,
            valueRange = 0f..100f,
            onValueChange = {
                sliderValue = it
                setTipAmount(sliderValue)
        })
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApp {
        Column() {
            TopHeader(0.0f)
            BodyCalculator(){
                Log.d("Preview", "DefaultPreview: Total Per Person: $it")
            }
        }
    }
}