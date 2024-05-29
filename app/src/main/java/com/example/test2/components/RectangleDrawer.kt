package com.example.test2.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.test2.MainActivity
import com.example.test2.R
import kotlin.math.roundToInt


//Functia DrawOneDraggableRectangle este static => se apeleaza astfel:
//RectangleDrawer.DrawOneDraggableRectangle(xFloatPos, yFloatPos, swipeDistance, onRectangleSwiped: (swipeDirection: Int) -> Unit?
//xFloatPost si yFloatPos sunt pozitiile unde va aparea dreptunghiul
//swipeDistance este distanta fata de pozitia initiala pana unde trebuie sa mearga dreptunghiul pentru a fi consdierat un swipe
//onRectangleSwipe -> este un callback care intoarec -1 pentru swipe la stanga si 1 pentru swipe la dreapta, se apeleaza in momentul in care a fost dat un swipe


//poate mai tarziu ar trebui ca RectangleDrawer sa nu mai fie abstracta si sa pot seta dimensiuniule dreptunghiului
abstract class RectangleDrawer {

    companion object {
        @Composable
        fun DrawOneDraggableRectangle(
            xFloatPos: Float,
            yFloatPos: Float,
            swipeDistance: Float,
            onRectangleSwiped: (swipeDirection: Int) -> Unit?
        ) {

            var xOffset by remember { mutableStateOf(xFloatPos) }
            var yOffset by remember { mutableStateOf(yFloatPos) }
            var rotateAngle by remember { mutableStateOf(0f) }
            var backgroundColor by remember { mutableStateOf(Color.Blue) }
            var visible by remember { mutableStateOf(true) }
            var awaitState by remember { mutableStateOf(true) }


            AnimatedVisibility(visible = visible, exit = fadeOut())
            {
                Box(
                    Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectDragGestures(onDrag = { change, dragAmount ->
                                change.consume()
                                xOffset += dragAmount.x
                                yOffset += dragAmount.y
                                rotateAngle += dragAmount.x / 10

                                backgroundColor =
                                    if (dragAmount.x < 0) Color.Magenta else Color.Yellow

                                if ((xOffset > xFloatPos + swipeDistance || xOffset < xFloatPos - swipeDistance) && awaitState) {
                                    visible = false
                                    awaitState = false
                                    var swipeDirection: Int
                                    swipeDirection = 0
                                    if (xOffset > xFloatPos + swipeDistance) {
                                        swipeDirection = 1
                                    } else if (xOffset < xFloatPos - swipeDistance) {
                                        swipeDirection = -1
                                    }
                                    onRectangleSwiped(swipeDirection)
                                }
                            },
                                onDragEnd = {
                                    xOffset = xFloatPos
                                    yOffset = yFloatPos
                                    rotateAngle = 0f

                                }
                            )
                        }
                        .offset { IntOffset(xOffset.roundToInt(), yOffset.roundToInt()) }
                        .rotate(rotateAngle)
                ) {
                    Box(
                        Modifier
                            .size(200.dp, 400.dp)
                            .background(color = backgroundColor)
                    ){


                    }

                }
            }
        }
    }
}