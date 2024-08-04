package com.example.appdemo.ui

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.appdemo.R
import com.example.appdemo.ui.theme.greenColor
import com.example.appdemo.ui.theme.hintColor
import com.example.appdemo.ui.theme.searchColor
import com.example.appdemo.ui.theme.titleColor
import com.example.appdemo.utils.getCurrentDate

@Composable
fun MainScreen() {
    val mainViewModel = hiltViewModel<MainViewModel>()
    val data by mainViewModel.filteredData.collectAsState()
    val isRetry = mainViewModel.isRetry
    val context = LocalContext.current
    LaunchedEffect(isRetry) {
        val message = if (isRetry) "Retrying connection..." else "Connected"
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(top = 20.dp)) {
        StatusBar()
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp) , verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.stocks),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = getCurrentDate(),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Black,
                    color = titleColor
                )
            }
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .clickable {
                        mainViewModel.retryTest()
                    }
                    .background(searchColor)
                    .padding(3.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_more_horiz_24),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        SearchBar { textValue ->
            mainViewModel.updateSearchQuery(textValue)
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(id = R.string.symbols),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = greenColor
        )
        Spacer(modifier = Modifier.height(20.dp))
        DrawerStockList(listStocks = data)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(onSearch: (String) -> Unit) {
    var textSearch by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        TextField(value = textSearch,
            onValueChange = {
                textSearch = it
                onSearch.invoke(textSearch)
            },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            singleLine = true,
            placeholder = {
                Text(
                    text = stringResource(id = R.string.search),
                    color = hintColor,
                    fontSize = 14.sp
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "Search Icon"
                )
            },
            textStyle = TextStyle(
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Start
            ),
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = Color.White,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                containerColor = searchColor
            ),
            shape = RoundedCornerShape(5.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    keyboardController?.hide()
                    onSearch.invoke(textSearch)
                }
            )
        )
    }
}

@Composable
private fun StatusBar() {
    val view = LocalView.current
    val context = view.context
    val window = (context as ComponentActivity).window
    WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
    window.statusBarColor = Color.Black.toArgb()
}