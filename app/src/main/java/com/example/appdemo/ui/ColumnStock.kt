package com.example.appdemo.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appdemo.data.model.Stocks
import com.example.appdemo.ui.theme.blueColor
import com.example.appdemo.ui.theme.grayColor
import com.example.appdemo.ui.theme.redColor
import com.example.appdemo.ui.theme.subTextColor

@Composable
fun DrawerStockList(listStocks: List<Stocks>) {
    LazyColumn {
        items(items = listStocks, itemContent = { item: Stocks ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                        .align(Alignment.CenterStart)
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentHeight()
                    ) {
                        Text(
                            text = item.name, modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                                .wrapContentHeight(), color = Color.White, fontSize = 16.sp,
                            maxLines = 1, fontWeight = FontWeight.ExtraBold,
                            textAlign = TextAlign.Start,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = item.publisher,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            color = subTextColor,
                            fontSize = 14.sp,
                            maxLines = 1,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Normal,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    DrawChart(item)
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        modifier = Modifier
                            .wrapContentWidth()
                            .wrapContentHeight(),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = item.currentValue.toString(), modifier = Modifier
                                .wrapContentWidth()
                                .wrapContentHeight(), color = Color.White, fontSize = 16.sp,
                            maxLines = 1, fontWeight = FontWeight.Bold,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        val color = if (item.valueChange >= 0f) blueColor else redColor
                        Row(
                            modifier = Modifier
                                .width(70.dp)
                                .wrapContentHeight()
                                .align(Alignment.End)
                                .background(color = color, shape = RoundedCornerShape(5.dp))
                        ) {
                            val valueChange = if (item.valueChange >= 0) "+${item.valueChange}" else item.valueChange.toString()
                            Text(
                                text = valueChange,
                                modifier = Modifier
                                    .weight(1f)
                                    .wrapContentHeight(),
                                color = Color.White,
                                fontSize = 16.sp,
                                maxLines = 1,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.End,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                }
                Spacer(
                    modifier = Modifier
                        .padding(start = 14.dp)
                        .fillMaxWidth()
                        .height(1.dp)
                        .align(Alignment.BottomStart)
                        .background(grayColor)
                )
            }
        })
    }

}

@Composable
private fun DrawChart(item: Stocks) {
    Box(
        modifier = Modifier
            .width(60.dp)
            .height(15.dp),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
            val width = size.width
            val height = size.height
            val maxValue = item.oldValue.max()
            val minValue = item.oldValue.min()
            val xStep = width / (item.oldValue.size - 1)
            val yScale = height / (maxValue - minValue)

            val hasPositive = item.oldValue.any { it > 0 }
            val hasNegative = item.oldValue.any { it < 0 }

            val path = Path().apply {
                moveTo(0f, height - (item.oldValue[0] - minValue) * yScale)
                item.oldValue.forEachIndexed { index, point ->
                    if (index > 0) {
                        val prevPoint = item.oldValue[index - 1]
                        val prevX = (index - 1) * xStep
                        val prevY = height - (prevPoint - minValue) * yScale
                        val currentX = index * xStep
                        val currentY = height - (point - minValue) * yScale

                        val controlX1 = (prevX + currentX) / 2
                        val controlY1 = prevY
                        val controlX2 = (prevX + currentX) / 2
                        val controlY2 = currentY

                        cubicTo(
                            controlX1,
                            controlY1,
                            controlX2,
                            controlY2,
                            currentX,
                            currentY
                        )
                    }
                }
            }

            val color = if (item.valueChange >= 0) blueColor else redColor

            drawPath(
                path = path,
                color = color,
                style = Stroke(width = 2.dp.toPx())
            )
            if (hasPositive && hasNegative) {
                val zeroY = height - (0f - minValue) * yScale
                drawLine(
                    color = color,
                    start = Offset(0f, zeroY),
                    end = Offset(width, zeroY),
                    strokeWidth = 1.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(
                        floatArrayOf(10f, 10f),
                        0f
                    )
                )
            }
        })
    }
}

//private fun initPath(path: Path, step: Float, listValue: ArrayList<Float>) {
//    path.reset()
//    val maxValue = listValue.max()
//    val minvalue = listValue.min()
//    var x = 0f
//    for (i in 0 until listValue.size) {
//        if (i == 0) {
//            path.moveTo(x , listValue[i])
//        }
//    }
//}
//
//private fun convertValueToPoint(height: Float , maxValue: Float , minValue: Float , currentValue: Float) {
//    if ()
//}
