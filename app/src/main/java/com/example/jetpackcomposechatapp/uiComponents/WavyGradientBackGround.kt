package com.example.jetpackcomposechatapp.uiComponents

import android.graphics.Color
import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import com.example.jetpackcomposechatapp.ui.theme.colorPink
import com.example.jetpackcomposechatapp.ui.theme.lightBlue
import com.example.jetpackcomposechatapp.ui.theme.veryLightBlue
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.Language

private data object BlueBackgroundElement : ModifierNodeElement<BlueBackgroundNode>() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun create() = BlueBackgroundNode()
    override fun update(node: BlueBackgroundNode) {
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private class BlueBackgroundNode : DrawModifierNode, Modifier.Node() {

    private val shader = RuntimeShader(SHADER)
    private val shaderBrush = ShaderBrush(shader)
    private val time = mutableFloatStateOf(0f)

    init {
        shader.setColorUniform(
            "color",
            Color.valueOf(Blue.red, Blue.green, Blue.blue, Blue.alpha)
        )
    }

    override fun ContentDrawScope.draw() {
        shader.setFloatUniform("resolution", size.width, size.height)
        shader.setFloatUniform("time", time.floatValue)
        drawRect(shaderBrush)
        drawContent()
    }

    override fun onAttach() {
        coroutineScope.launch {
            while (true) {
                withInfiniteAnimationFrameMillis {
                    time.floatValue = it / 1000f
                }
            }
        }
    }
}

fun Modifier.blueBackground(): Modifier =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this.then(BlueBackgroundElement)
    } else {
        drawWithCache {
            val gradientBrush = Brush.verticalGradient(listOf(lightBlue, veryLightBlue, White))
            onDrawBehind {
                drawRect(gradientBrush)
            }
        }
    }

@Language("AGSL")
val SHADER = """
    uniform float2 resolution;
    uniform float time;
    layout(color) uniform half4 color;
    
    float calculateColorMultiplier(float yCoord, float factor) {
        return step(yCoord, 1.0 + factor * 2.0) - step(yCoord, factor - 0.1);
    }

    float4 main(in float2 fragCoord) {
        // Config values
        const float speedMultiplier = 1.5;
        const float waveDensity = 1.0;
        const float loops = 8.0;
        const float energy = 0.6;
        
        // Calculated values
        float2 uv = fragCoord / resolution.xy;
        float3 rgbColor = color.rgb;
        float timeOffset = time * speedMultiplier;
        float hAdjustment = uv.x * 4.3;
        float3 loopColor = vec3(1.0 - rgbColor.r, 1.0 - rgbColor.g, 1.0 - rgbColor.b) / loops;
        
        for (float i = 1.0; i <= loops; i += 1.0) {
            float loopFactor = i * 0.1;
            float sinInput = (timeOffset + hAdjustment) * energy;
            float curve = sin(sinInput) * (1.0 - loopFactor) * 0.05;
            float colorMultiplier = calculateColorMultiplier(uv.y, loopFactor);
            rgbColor += loopColor * colorMultiplier;
            
            // Offset for next loop
            uv.y += curve;
        }
        
        return float4(rgbColor, 1.0);
    }
""".trimIndent()