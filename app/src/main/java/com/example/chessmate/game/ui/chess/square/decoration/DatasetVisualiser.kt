package com.example.chessmate.game.ui.chess.square.decoration

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.sp
import com.example.chessmate.game.model.data_chessmate.LocalActiveDatasetVisualisation
import com.example.chessmate.game.ui.chess.square.SquareDecoration
import com.example.chessmate.game.ui.chess.square.SquareRenderProperties


object DatasetVisualiser : SquareDecoration {

    @Composable
    override fun render(properties: SquareRenderProperties) {
        LocalActiveDatasetVisualisation .current.let { chessmate ->
            val datapoint = remember(chessmate, properties) { chessmate.dataPointAt(
                    properties.position,
                    properties.boardProperties.toState,
                    properties.boardProperties.cache
                )
            }

            val percentage = datapoint?.value?.let {
                (1.0f * datapoint.value - chessmate.minValue) / (chessmate.maxValue - chessmate.minValue)
            }?.coerceIn(0f, 1f)

            val interpolatedColor = percentage?.let {
                lerp(datapoint.colorScale.first, datapoint.colorScale.second, percentage)
            }

            val color by animateColorAsState(
                targetValue = interpolatedColor ?: Color.Transparent,
                animationSpec = tween(1500), label = ""
            )

            datapoint?.let {
                Box(
                    modifier = properties.sizeModifier
                        .background(color),
                    contentAlignment = Alignment.TopEnd
                ) {
                    datapoint.label?.let {
                        Text(
                            text = datapoint.label,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}

