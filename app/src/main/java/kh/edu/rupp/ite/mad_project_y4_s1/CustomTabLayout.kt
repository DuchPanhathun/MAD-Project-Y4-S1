package kh.edu.rupp.ite.mad_project_y4_s1

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayout

class CustomTabLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TabLayout(context, attrs, defStyleAttr) {

    private val indicatorPaint = Paint()
    private val indicatorWidth: Float
    private val indicatorHeight: Float
    private val indicatorSpacing: Float

    init {
        val resources = context.resources
        indicatorWidth = resources.getDimension(R.dimen.indicator_width)
        indicatorHeight = resources.getDimension(R.dimen.indicator_height)
        indicatorSpacing = resources.getDimension(R.dimen.indicator_spacing)
        indicatorPaint.color = ContextCompat.getColor(context, R.color.indicator_color)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val tabCount = tabCount
        if (tabCount > 0) {
            val selectedPosition = selectedTabPosition
            val totalWidth = (indicatorWidth + indicatorSpacing) * tabCount - indicatorSpacing
            var startX = (width - totalWidth) / 2f

            for (i in 0 until tabCount) {
                val alpha = if (i == selectedPosition) 255 else 128
                indicatorPaint.alpha = alpha
                canvas.drawRoundRect(
                    startX,
                    (height - indicatorHeight).toFloat(),
                    startX + indicatorWidth,
                    height.toFloat(),
                    indicatorHeight / 2f,
                    indicatorHeight / 2f,
                    indicatorPaint
                )
                startX += indicatorWidth + indicatorSpacing
            }
        }
    }
}