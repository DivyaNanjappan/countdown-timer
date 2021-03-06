/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.timer.androiddevchallenge

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.timer.androiddevchallenge.ui.theme.MyTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp()
            }
        }
    }
}

@Composable
fun MyApp() {
    Surface(color = MaterialTheme.colors.background) {
        TimerContent()
    }
}

lateinit var countdown_timer: CountDownTimer

fun initTimer(hmsState: MutableState<String>) {

    countdown_timer = object : CountDownTimer(300000, 1000) {
        override fun onFinish() {
            countdown_timer.cancel()
        }

        @SuppressLint("DefaultLocale")
        override fun onTick(timeInMillis: Long) {
            val minutes = (timeInMillis / 1000) / 60
            val seconds = (timeInMillis / 1000) % 60
            hmsState.value = "$minutes:$seconds"
        }
    }
    countdown_timer.start()
}

@Composable
fun TimerContent() {
    val hmsState = remember { mutableStateOf("5:00") }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .fillMaxHeight()
    ) {

        Scaffold {
            ConstraintLayout(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight(),
                constraintSet = ConstraintSet {
                    val image = createRefFor("image")
                    val timeValue = createRefFor("timeValue")
                    val timerText = createRefFor("timerText")
                    val progressBar = createRefFor("progressBar")
                    val row = createRefFor("row")

                    constrain(image) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                    }

                    constrain(timeValue) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(image.bottom)
                    }


                    constrain(progressBar) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(timeValue.bottom)
                    }

                    constrain(timerText) {
                        start.linkTo(progressBar.start)
                        end.linkTo(progressBar.end)
                        top.linkTo(progressBar.top)
                        bottom.linkTo(progressBar.bottom)
                    }

                    constrain(row) {
                        top.linkTo(progressBar.bottom)
                    }

                }) {

                Image(
                    painter = painterResource(
                        id = R.drawable.ic_sand_clock
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .layoutId("image")
                        .width(150.dp)
                        .height(150.dp)
                        .padding(0.dp, 50.dp, 0.dp, 10.dp)
                )

                Text(
                    text = "Count Down - 5 Minutes",
                    modifier = Modifier
                        .layoutId("timeValue")
                        .padding(10.dp)
                        .wrapContentHeight(),
                    style = MaterialTheme.typography.h5,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.primary
                )

                Text(
                    text = hmsState.value,
                    modifier = Modifier
                        .layoutId("timerText")
                        .padding(10.dp)
                        .wrapContentHeight(),
                    style = MaterialTheme.typography.h3,
                    textAlign = TextAlign.Center
                )

                CircularProgressIndicator(
                    progress = 100f,
                    modifier = Modifier
                        .layoutId("progressBar")
                        .padding(8.dp)
                        .width(200.dp)
                        .height(200.dp),
                    color = colorResource(id = R.color.blue_700)
                )
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .layoutId("row")
                        .padding(8.dp)
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Button(
                        onClick = {
                            initTimer(hmsState)
                        },
                        modifier = Modifier
                            .padding(8.dp)
                            .width(150.dp)
                            .height(50.dp)
                    ) {
                        Text(text = "Start")
                    }

                    Button(
                        onClick = {
                            if (::countdown_timer.isInitialized) {
                                countdown_timer.cancel()
                                hmsState.value = "0"
                            }
                        },
                        modifier = Modifier
                            .padding(8.dp)
                            .width(150.dp)
                            .height(50.dp)
                    ) {
                        Text(text = "Stop")
                    }
                }
            }
        }
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp()
    }
}
