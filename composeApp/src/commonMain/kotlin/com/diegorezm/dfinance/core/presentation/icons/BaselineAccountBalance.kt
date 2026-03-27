import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val BaselineAccountBalance: ImageVector
    get() {
        if (_BaselineAccountBalance != null) {
            return _BaselineAccountBalance!!
        }
        _BaselineAccountBalance = ImageVector.Builder(
            name = "BaselineAccountBalance",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.White)) {
                moveTo(4f, 10f)
                horizontalLineToRelative(3f)
                verticalLineToRelative(7f)
                horizontalLineToRelative(-3f)
                close()
            }
            path(fill = SolidColor(Color.White)) {
                moveTo(10.5f, 10f)
                horizontalLineToRelative(3f)
                verticalLineToRelative(7f)
                horizontalLineToRelative(-3f)
                close()
            }
            path(fill = SolidColor(Color.White)) {
                moveTo(2f, 19f)
                horizontalLineToRelative(20f)
                verticalLineToRelative(3f)
                horizontalLineToRelative(-20f)
                close()
            }
            path(fill = SolidColor(Color.White)) {
                moveTo(17f, 10f)
                horizontalLineToRelative(3f)
                verticalLineToRelative(7f)
                horizontalLineToRelative(-3f)
                close()
            }
            path(fill = SolidColor(Color.White)) {
                moveTo(12f, 1f)
                lineToRelative(-10f, 5f)
                lineToRelative(0f, 2f)
                lineToRelative(20f, 0f)
                lineToRelative(0f, -2f)
                close()
            }
        }.build()

        return _BaselineAccountBalance!!
    }

@Suppress("ObjectPropertyName")
private var _BaselineAccountBalance: ImageVector? = null
