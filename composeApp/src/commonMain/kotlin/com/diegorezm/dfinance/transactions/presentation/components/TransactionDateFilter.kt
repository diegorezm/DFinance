package com.diegorezm.dfinance.transactions.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dfinance.composeapp.generated.resources.Res
import dfinance.composeapp.generated.resources.cancel
import dfinance.composeapp.generated.resources.current_month
import dfinance.composeapp.generated.resources.save
import dfinance.composeapp.generated.resources.select_date_range
import dfinance.composeapp.generated.resources.selected_range
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import androidx.compose.foundation.clickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDateFilter(
    startDate: LocalDate? = null,
    endDate: LocalDate? = null,
    isDateFiltered: Boolean = false,
    onDateRangeSelected: (LocalDate, LocalDate) -> Unit,
    onClearDateFilter: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(0.dp)
            )
            .background(
                color = if (isDateFiltered) MaterialTheme.colorScheme.secondaryContainer
                else MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(0.dp)
            )
            .clickable { showDatePicker = true }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.DateRange,
            contentDescription = null,
            tint = if (isDateFiltered) MaterialTheme.colorScheme.onSecondaryContainer
            else MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = when {
                startDate != null && endDate != null -> "${startDate} - ${endDate}"
                else -> stringResource(Res.string.current_month)
            },
            style = MaterialTheme.typography.labelLarge,
            fontWeight = if (isDateFiltered) FontWeight.Bold else FontWeight.Normal,
            color = if (isDateFiltered) MaterialTheme.colorScheme.onSecondaryContainer
            else MaterialTheme.colorScheme.onSurface
        )
        if (isDateFiltered) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Clear",
                modifier = Modifier
                    .clickable { onClearDateFilter() }
                    .padding(start = 4.dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }

    if (showDatePicker) {
        val dateRangePickerState = rememberDateRangePickerState()
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

        ModalBottomSheet(
            onDismissRequest = { showDatePicker = false },
            sheetState = sheetState,
            shape = RectangleShape,
            containerColor = MaterialTheme.colorScheme.background,
            dragHandle = null
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                DateRangePicker(
                    state = dateRangePickerState,
                    modifier = Modifier.weight(1f),
                    title = {
                        Text(
                            text = stringResource(Res.string.select_date_range),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(16.dp)
                        )
                    },
                    headline = {
                        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                            Text(
                                text = stringResource(Res.string.selected_range),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    showModeToggle = false,
                    colors = DatePickerDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.onBackground,
                        headlineContentColor = MaterialTheme.colorScheme.onBackground,
                        weekdayContentColor = MaterialTheme.colorScheme.onBackground,
                        subheadContentColor = MaterialTheme.colorScheme.onBackground,
                        navigationContentColor = MaterialTheme.colorScheme.onBackground,
                        yearContentColor = MaterialTheme.colorScheme.onBackground,
                        disabledYearContentColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.38f),
                        selectedYearContentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledSelectedYearContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.38f),
                        selectedYearContainerColor = MaterialTheme.colorScheme.primary,
                        disabledSelectedYearContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.38f),
                        dayContentColor = MaterialTheme.colorScheme.onBackground,
                        disabledDayContentColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.38f),
                        selectedDayContentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledSelectedDayContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.38f),
                        selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                        disabledSelectedDayContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.38f),
                        todayContentColor = MaterialTheme.colorScheme.primary,
                        todayDateBorderColor = MaterialTheme.colorScheme.primary,
                        dayInSelectionRangeContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        dayInSelectionRangeContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    )
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextButton(
                        onClick = { showDatePicker = false },
                        shape = RectangleShape,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            stringResource(Res.string.cancel),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    TextButton(
                        onClick = {
                            val start = dateRangePickerState.selectedStartDateMillis?.let {
                                Instant.fromEpochMilliseconds(it).toLocalDateTime(TimeZone.UTC).date
                            }
                            val end = dateRangePickerState.selectedEndDateMillis?.let {
                                Instant.fromEpochMilliseconds(it).toLocalDateTime(TimeZone.UTC).date
                            }
                            if (start != null && end != null) {
                                onDateRangeSelected(start, end)
                            }
                            showDatePicker = false
                        },
                        enabled = dateRangePickerState.selectedEndDateMillis != null,
                        shape = RectangleShape,
                        modifier = Modifier
                            .weight(1f)
                            .border(2.dp, MaterialTheme.colorScheme.outline),
                        colors = androidx.compose.material3.ButtonDefaults.textButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                        )
                    ) {
                        Text(
                            stringResource(Res.string.save),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
