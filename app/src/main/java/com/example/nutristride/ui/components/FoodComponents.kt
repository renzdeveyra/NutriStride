package com.example.nutristride.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nutristride.data.model.FoodItem

@Composable
fun FoodItemRow(
    foodItem: FoodItem,
    onClick: () -> Unit,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = foodItem.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    if (foodItem.brand != null) {
                        Text(
                            text = foodItem.brand,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                
                if (trailingIcon != null) {
                    trailingIcon()
                } else {
                    Text(
                        text = "${foodItem.calories} cal",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                NutrientInfo(name = "Protein", value = foodItem.protein, unit = "g")
                NutrientInfo(name = "Carbs", value = foodItem.carbs, unit = "g")
                NutrientInfo(name = "Fat", value = foodItem.fat, unit = "g")
            }
        }
    }
}

@Composable
fun NutrientInfo(name: String, value: Double, unit: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "$value$unit",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = name,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun NutrientCircle(
    value: Float,
    maxValue: Float,
    color: Color,
    label: String
) {
    val progress = (value / maxValue).coerceIn(0f, 1f)
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(60.dp)
        ) {
            // Background track
            Canvas(modifier = Modifier.size(60.dp)) {
                drawArc(
                    color = Color.LightGray.copy(alpha = 0.3f),
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                )
            }
            
            // Progress arc
            Canvas(modifier = Modifier.size(60.dp)) {
                drawArc(
                    color = color,
                    startAngle = -90f,
                    sweepAngle = 360f * progress,
                    useCenter = false,
                    style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                )
            }
            
            // Value text
            Text(
                text = "${value.toInt()}",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )
        }
        
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall
        )
    }
}
