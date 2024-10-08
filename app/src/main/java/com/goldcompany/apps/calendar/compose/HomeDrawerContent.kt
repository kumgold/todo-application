package com.goldcompany.apps.calendar.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.goldcompany.apps.calendar.R

@Composable
fun HomeDrawerContent(

) {
    ModalDrawerSheet(
        drawerShape = RectangleShape
    ) {
        Surface(color = MaterialTheme.colorScheme.background) {
            LazyColumn(
                modifier = Modifier
                    .padding(
                        dimensionResource(id = R.dimen.default_margin)
                    )
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    DrawerHeader()
                }
            }
        }
    }
}

@Composable
private fun DrawerHeader() {
    Image(
        modifier = Modifier
            .size(300.dp)
            .padding(dimensionResource(id = R.dimen.default_margin)),
        imageVector = Icons.Filled.AccountCircle,
        contentDescription = stringResource(id = R.string.user),
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
    )
    HorizontalDivider(
        modifier = Modifier.padding(
            vertical = dimensionResource(id = R.dimen.default_margin)
        ),
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun DrawerItem() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
    ) {
        Icon(
            imageVector = Icons.Filled.Share,
            contentDescription = stringResource(id = R.string.menu)
        )
        Text(text = stringResource(id = R.string.graph))
    }
}