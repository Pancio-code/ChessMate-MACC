package com.example.chessmate.ui.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog


@Composable
fun MenuCountryPicker(isConfirmMode: () -> Unit, onValueChange: (String) -> Unit, currentCountry: String) {
    val countryCode = CountryPicker()
    countryCode.CountryCodeDialog(
        pickedCountry = {
            Log.v("TAG", "country name is : ${it.countryName}")
            onValueChange(it.countryCode)
        },
        defaultSelectedCountry = getListOfCountries().single { it.countryCode == currentCountry },
        dialogSearch = true,
        dialogRounded = 22,
        isConfirmMode = isConfirmMode
    )
}

@Suppress("SameParameterValue")
class CountryPicker {
    @Composable
    fun CountryCodeDialog(
        isOnlyFlagShow: Boolean = false,
        defaultSelectedCountry: CountryCode = getListOfCountries().first(),
        pickedCountry: (CountryCode) -> Unit,
        dialogSearch: Boolean = true,
        dialogRounded: Int = 12,
        isConfirmMode: () -> Unit
    ) {
        val countryList: List<CountryCode> = getListOfCountries()
        var isPickCountry by remember { mutableStateOf(defaultSelectedCountry) }
        var isOpenDialog by remember { mutableStateOf(false) }
        var searchValue by remember { mutableStateOf("") }

        Card(
            modifier = Modifier
                .padding(3.dp)
                .clickable { isOpenDialog = true },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.inverseOnSurface,
                )
            ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(
                            id = getFlags(
                                isPickCountry.countryCode
                            )
                        ), contentDescription = null,
                    )
                    if (!isOnlyFlagShow) {
                        Text(
                            isPickCountry.countryName,
                            Modifier.padding(horizontal = 18.dp),
                            color = Color.White
                        )
                    }
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.White)
                }
            }

            //Dialog
            if (isOpenDialog) {
                Dialog(
                    onDismissRequest = { isOpenDialog = false },
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.85f),
                        shape = RoundedCornerShape(dialogRounded.dp)
                    ) {
                        Column {
                            if (dialogSearch) {
                                searchValue = dialogSearchView()
                            }
                            LazyColumn {
                                val myList = countryList.takeIf { searchValue.isEmpty() } ?:
                                                countryList.searchCountryList(searchValue)
                                items(myList) { countryItem ->
                                    Row(
                                        Modifier
                                            .padding(
                                                horizontal = 18.dp,
                                                vertical = 18.dp
                                            ).fillMaxWidth()
                                            .clickable {
                                                pickedCountry(countryItem)
                                                isPickCountry = countryItem
                                                isOpenDialog = false
                                                isConfirmMode()
                                            },
                                        verticalAlignment = Alignment.CenterVertically) {
                                        Image(
                                            painter = painterResource(
                                                id = getFlags(
                                                    countryItem.countryCode
                                                )
                                            ), contentDescription = null
                                        )
                                        Text(
                                            countryItem.countryName,
                                            Modifier.padding(horizontal = 18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun dialogSearchView(): String {
        var searchValue by remember { mutableStateOf("") }
        Row {
            MyCustomTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                value = searchValue,
                onValueChange = {
                    searchValue = it
                },
                fontSize = 14.sp,
                hint = "Search ...",
                textAlign = TextAlign.Start,
            )
        }
        return searchValue
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun MyCustomTextField(
        modifier: Modifier = Modifier,
        value: String,
        onValueChange: (String) -> Unit,
        hint: String = "",
        fontSize: TextUnit = 16.sp,
        textAlign: TextAlign = TextAlign.Center
    ) {
        Box(
            modifier = modifier
                .background(
                    color = Color.White.copy(alpha = 0.1f)
                )
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = value,
                onValueChange = onValueChange,
                textStyle = LocalTextStyle.current.copy(
                    textAlign = textAlign,
                    fontSize = fontSize
                ),
                singleLine = true,
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        tint = Color.Black.copy(0.2f)
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.inverseOnSurface,
                    focusedIndicatorColor = MaterialTheme.colorScheme.inverseOnSurface,
                    disabledIndicatorColor = MaterialTheme.colorScheme.inverseOnSurface,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.inverseOnSurface
                )
            )
            if (value.isEmpty()) {
                Text(
                    text = hint,
                    color = Color.White,
                    modifier = Modifier.then(
                        Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 52.dp)
                    )
                )
            }
        }
    }
}

fun List<CountryCode>.searchCountryList(key: String): MutableList<CountryCode> {
    val tempList = mutableListOf<CountryCode>()
    this.forEach {
        if (it.countryName.lowercase().contains(key.lowercase())) {
            tempList.add(it)
        }
    }
    return tempList
}