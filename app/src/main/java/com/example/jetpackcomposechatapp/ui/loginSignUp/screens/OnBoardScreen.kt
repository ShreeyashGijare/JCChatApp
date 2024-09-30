package com.example.jetpackcomposechatapp.ui.loginSignUp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.jetpackcomposechatapp.R
import com.example.jetpackcomposechatapp.ui.theme.interFontFamilyBold
import com.example.jetpackcomposechatapp.uiComponents.BlueOutlinedButtonComponent
import com.example.jetpackcomposechatapp.uiComponents.PinkBackgroundButtonComponent
import com.example.jetpackcomposechatapp.utils.AuthRouteScreen
import kotlin.math.absoluteValue

@Composable
fun OnBoardScreen(
    rootNavController: NavController
) {

    val pageCount: Int = 3
    val pagerState = rememberPagerState(pageCount = {
        pageCount
    })
    val coroutineScope = rememberCoroutineScope()

    val onboardDataList: List<OnBoardItem> = listOf(
        OnBoardItem(R.drawable.onboarding_one, "Connect with friends"),
        OnBoardItem(R.drawable.onboarding_three, "Chat without limits"),
        OnBoardItem(R.drawable.onboarding_two, "Share your moments")
    )

    /*LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            val nextPage = (pagerState.currentPage + 1) % onboardDataList.size
            coroutineScope.launch {
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }*/

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        HorizontalPager(state = pagerState) { page ->
            Box(
                modifier = Modifier
                    .fillMaxHeight(.7f)
                    .fillMaxWidth()
                    .graphicsLayer {
                        val pageOffset = pagerState.calculateCurrentOffsetForPage(page)
                        translationX = pageOffset * size.width
                        alpha = 1 - pageOffset.absoluteValue

                    },
                contentAlignment = Alignment.BottomStart
            ) {
                Image(
                    painter = painterResource(id = onboardDataList[page].image),
                    contentDescription = "",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxSize()
                )
                Text(
                    text = onboardDataList[page].text,
                    fontSize = 30.sp,
                    modifier = Modifier
                        .fillMaxWidth(.5f)
                        .padding(start = 20.dp, bottom = 30.dp),
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 42.sp,
                    fontFamily = interFontFamilyBold,
                    color = if (page == 1) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary
                )
            }
        }
        Row(
            Modifier
                .height(100.dp)
                .fillMaxWidth(0.3f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Top
        ) {
            repeat(pageCount) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration) if (iteration == 1) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .background(color, CircleShape)
                        .size(10.dp)
                )
            }
        }

        PinkBackgroundButtonComponent(
            buttonText = R.string.sign_in,
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            rootNavController.navigate(AuthRouteScreen.LoginScreen.route)
        }
        Spacer(modifier = Modifier.height(20.dp))
        BlueOutlinedButtonComponent(
            buttonText = R.string.create_account,
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            rootNavController.navigate(AuthRouteScreen.SignUpScreen.route)
        }
    }
}

fun PagerState.calculateCurrentOffsetForPage(page: Int): Float {
    return (currentPage - page) + currentPageOffsetFraction
}

data class OnBoardItem(
    val image: Int,
    val text: String
)