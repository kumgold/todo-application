package com.goldcompany.apps.todoapplication.home.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.goldcompany.apps.data.data.Task
import com.goldcompany.apps.todoapplication.R
import com.goldcompany.apps.todoapplication.util.dateToMilli
import kotlinx.coroutines.flow.collectLatest
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CalendarView(
    selectedDateMilli: Long,
    monthlyTasks: Map<Long, List<Task>>,
    selectDateMilli: (Long) -> Unit,
    getMonthlyTasks: (LocalDate, LocalDate) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = dimensionResource(id = R.dimen.default_margin))
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        var currentLocalDate by remember { mutableStateOf(LocalDate.now()) }
        val yearRange = IntRange(currentLocalDate.year - 40, currentLocalDate.year + 40)
        val initialPage = (currentLocalDate.year - yearRange.first) * 12 + currentLocalDate.monthValue - 1
        val pagerState = rememberPagerState(initialPage = initialPage) {
            (yearRange.last - yearRange.first) * 12
        }

        LaunchedEffect(key1 = currentLocalDate.dayOfWeek) {
            currentLocalDate = LocalDate.now()
        }

        LaunchedEffect(key1 = pagerState) {
            snapshotFlow { pagerState.currentPage }.collectLatest { page ->
                val displayDate = if (page == initialPage) {
                    LocalDate.of(
                        yearRange.first + page/12,
                        page % 12 + 1,
                        currentLocalDate.dayOfMonth
                    )
                } else {
                    LocalDate.of(
                        yearRange.first + page/12,
                        page % 12 + 1,
                        1
                    )
                }

                selectDateMilli(displayDate.dateToMilli())
                getMonthlyTasks(
                    LocalDate.of(
                        yearRange.first + page/12,
                        page % 12 + 1,
                        1
                    ),
                    LocalDate.of(
                        yearRange.first + page/12,
                        page % 12 + 2,
                        1
                    )
                )
            }
        }

        DayOfWeekView()
        Spacer(modifier = Modifier.height(20.dp))
        HorizontalPager(state = pagerState) { page ->
            val month = LocalDate.of(
                yearRange.first + page/12,
                page % 12 + 1,
                1
            )
            val lastDay = month.lengthOfMonth()
            val firstDayOfWeek = month.dayOfWeek.value
            val days = IntRange(1, lastDay).toList()

            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (firstDayOfWeek != 7) {
                    repeat(firstDayOfWeek) {
                        item {
                            EmptyDay()
                        }
                    }
                }
                items(days) { day ->
                    val date = month.withDayOfMonth(day)

                    CalendarItem(
                        date = date,
                        isToday = (date == LocalDate.now()),
                        isContainTasks = monthlyTasks.keys.contains(date.dateToMilli()),
                        currentDateMilli = selectedDateMilli,
                        getDailyTasks = selectDateMilli
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalStdlibApi::class)
@Composable
private fun DayOfWeekView() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        (listOf(DayOfWeek.entries.last()) + DayOfWeek.entries.subList(0, 6)).forEach { dayOfWeek ->
            Text(
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.US)
                    .uppercase(Locale.ROOT),
            )
        }
    }
}

@Composable
private fun CalendarItem(
    date: LocalDate,
    isToday: Boolean,
    isContainTasks: Boolean,
    currentDateMilli: Long,
    getDailyTasks: (Long) -> Unit,
) {
    val milli = date.dateToMilli()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .aspectRatio(1.0f)
            .border(
                width = 1.dp,
                color = if (isToday) {
                    Color.Red
                } else if (currentDateMilli == milli) {
                    Color.Gray
                } else {
                    MaterialTheme.colorScheme.background
                },
                shape = RoundedCornerShape(10.dp)
            )
            .clip(shape = RoundedCornerShape(10.dp))
            .clickable {
                getDailyTasks(milli)
            }
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(
                text = (date.dayOfMonth).toString(),
            )
            if (isContainTasks) {
                Box(
                    modifier = Modifier.size(5.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        )
                        .align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Composable
private fun EmptyDay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .aspectRatio(1.0f)
    )
}