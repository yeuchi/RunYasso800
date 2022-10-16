package com.ctyeung.runyasso800

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ctyeung.runyasso800.ui.theme.RunYasso800Theme
import com.ctyeung.runyasso800.views.GoalScreen
import com.ctyeung.runyasso800.views.RecapScreen
import com.ctyeung.runyasso800.views.RunScreen
import com.ctyeung.runyasso800.views.ShareScreen
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch

/*
 * Reference
 * Tab Layout in Android using Jetpack Compose
 * https://www.geeksforgeeks.org/tab-layout-in-android-using-jetpack-compose/
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RunYasso800Theme {
                // A surface container using the 'background' color from the theme
                TabLayout()
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabLayout() {

    // on below line we are creating variable for pager state.
    val pagerState = rememberPagerState(pageCount = 3)

    // on below line we are creating a column for our widgets.
    Scaffold(
        // for column we are specifying modifier on below line.
        modifier = Modifier.background(Color.White)
    ) {
        BottomAppBar(backgroundColor = Color.Blue) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {}
        }
        TabsContent(pagerState = pagerState)
        Tabs(pagerState = pagerState)
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Tabs(pagerState: PagerState) {
    val list = listOf(
        "Goal" to Icons.Default.Star,
        "Run" to Icons.Default.Face,
        "Recap" to Icons.Default.List,
        "Share" to Icons.Default.Share
    )
    val scope = rememberCoroutineScope()
    // on below line we are creating a
    // individual row for our tab layout.
    TabRow(
        // on below line we are specifying
        // the selected index.
        selectedTabIndex = pagerState.currentPage,

        // on below line we are
        // specifying background color.
        backgroundColor = Color.Blue,

        // on below line we are specifying content color.
        contentColor = Color.White,

        // on below line we are specifying
        // the indicator for the tab
        indicator = { tabPositions ->
            // on below line we are specifying the styling
            // for tab indicator by specifying height
            // and color for the tab indicator.
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                height = 2.dp,
                color = Color.White
            )
        }
    ) {
        // on below line we are specifying icon
        // and text for the individual tab item
        list.forEachIndexed { index, _ ->
            // on below line we are creating a tab.
            Tab(
                // on below line we are specifying icon
                // for each tab item and we are calling
                // image from the list which we have created.
                icon = {
                    Icon(imageVector = list[index].second, contentDescription = null)
                },
                // on below line we are specifying the text for
                // the each tab item and we are calling data
                // from the list which we have created.
                text = {
                    Text(
                        list[index].first,
                        // on below line we are specifying the text color
                        // for the text in that tab
                        color = if (pagerState.currentPage == index) Color.White else Color.LightGray
                    )
                },
                // on below line we are specifying
                // the tab which is selected.
                selected = pagerState.currentPage == index,
                // on below line we are specifying the
                // on click for the tab which is selected.
                onClick = {
                    // on below line we are specifying the scope.
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
        }
    }
}

// on below line we are creating a tab content method
// in which we will be displaying the individual page of our tab .
@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabsContent(pagerState: PagerState) {
    // on below line we are creating
    // horizontal pager for our tab layout.
    HorizontalPager(state = pagerState) {
        // on below line we are specifying
        // the different pages.
            page ->
        when (page) {
            // on below line we are calling tab content screen
            // and specifying data as Home Screen.
            0 -> GoalScreen().Render()
            // on below line we are calling tab content screen
            // and specifying data as Shopping Screen.
            1 -> RunScreen().Render()
            // on below line we are calling tab content screen
            // and specifying data as Settings Screen.
            2 -> RecapScreen().Render()

            3 -> ShareScreen().Render()
        }
    }
}