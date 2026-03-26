package com.diegorezm.dfinance.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.diegorezm.dfinance.theme.DFinanceTheme


@Composable
fun NeobrutalistFAB(
    icon: ImageVector,
    onClick: () -> Unit,
    contentDescription: String? = null,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.size(60.dp)) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .offset(x = 4.dp, y = 4.dp)
                .background(MaterialTheme.colorScheme.outline)
        )
        FloatingActionButton(
            onClick = onClick,
            shape = RoundedCornerShape(0.dp),
            elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(56.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun BoxScope.NeobrutalistBottomFloatingFAB(
    icon: ImageVector,
    onClick: () -> Unit,
    contentDescription: String? = null,
    modifier: Modifier = Modifier
) {
    NeobrutalistFAB(
        icon = icon,
        onClick = onClick,
        contentDescription = contentDescription,
        modifier = modifier
            .align(Alignment.BottomEnd)
            .padding(
                end = 16.dp,
                bottom = 120.dp
            ),
    )
}


@Preview(showBackground = true)
@Composable
private fun NeobrutalistFABPreview() {
    DFinanceTheme {
        NeobrutalistFAB(
            icon = Icons.Default.Edit,
            onClick = {},
            contentDescription = "Add account",
            modifier = Modifier
        )
    }

}
