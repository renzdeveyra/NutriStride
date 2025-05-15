package com.example.nutristride.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutristride.R
import com.example.nutristride.ui.theme.LightGrey
import com.example.nutristride.ui.theme.MediumGrey
import com.example.nutristride.ui.theme.NutriStrideOrange

@Composable
fun AppHeader(onProfileClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Logo
        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                ) {
                    append("nutri")
                }
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                ) {
                    append("stride")
                }
            }
        )

        // Profile Icon
        IconButton(onClick = onProfileClick) {
            Icon(
                imageVector = androidx.compose.material.icons.Icons.Default.Person,
                contentDescription = "Profile",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
fun CircularProgressIndicator(
    modifier: Modifier = Modifier,
    progress: Float,
    currentValue: Int,
    maxValue: Int,
    label: String
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(180.dp)
        ) {
            // Background track
            Canvas(modifier = Modifier.size(180.dp)) {
                val strokeWidth = 15.dp.toPx()
                drawArc(
                    color = LightGrey,
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }

            // Progress arc
            Canvas(modifier = Modifier.size(180.dp)) {
                val strokeWidth = 15.dp.toPx()
                drawArc(
                    color = NutriStrideOrange,
                    startAngle = -90f,
                    sweepAngle = 360f * progress,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }

            // Text in the center
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = currentValue.toString(),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "/ $maxValue",
                    fontSize = 16.sp,
                    color = MediumGrey
                )
            }
        }

        // Label below the circle
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun SmallCircularProgressIndicator(
    modifier: Modifier = Modifier,
    progress: Float,
    currentValue: Int,
    maxValue: Int,
    label: String
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(100.dp)
        ) {
            // Background track
            Canvas(modifier = Modifier.size(100.dp)) {
                val strokeWidth = 10.dp.toPx()
                drawArc(
                    color = LightGrey,
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }

            // Progress arc
            Canvas(modifier = Modifier.size(100.dp)) {
                val strokeWidth = 10.dp.toPx()
                drawArc(
                    color = NutriStrideOrange,
                    startAngle = -90f,
                    sweepAngle = 360f * progress,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }

            // Text in the center
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$currentValue / $maxValue",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Label below the circle
        Text(
            text = label,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun ActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.onBackground
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "+ $text",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
