package com.compose.codelab.jetpackcomposebasic

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.compose.codelab.jetpackcomposebasic.ui.theme.JetpackComposeBasicTheme

class FirstWeekActivity : AppCompatActivity() { // ComponentActivity도 가능
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeBasicTheme {
                HoistMyApp()
            }
        }
    }

}

@Composable
private fun MyApp() {
    Surface(color = MaterialTheme.colors.background) {
        Greet("Android")
    }
}

@Composable
private fun MyApp2(names: List<String> = listOf("World", "Compose")) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        for (name in names) {
            Greet(name = name)
        }
    }
}

/* https://developer.android.com/codelabs/jetpack-compose-basics?authuser=4&continue=https%3A%2F%2Fdeveloper.android.com%2Fcourses%2Fpathways%2Fcompose%3Fhl%3Den%26authuser%3D4%23codelab-https%3A%2F%2Fdeveloper.android.com%2Fcodelabs%2Fjetpack-compose-basics&hl=en#7 */
// codeLab 8
@Composable
private fun HoistMyApp() {
    var shouldShowOnBoarding by rememberSaveable { mutableStateOf(true) }
    // remember : only as long as the composable is kept in the Composition
    // rememberSaveable : each state surviving configuration changed and process death

    if (shouldShowOnBoarding) {
        OnBoardingScreen(onContinueClicked = { shouldShowOnBoarding = false })
    } else {
        GreetingList()
    }
}

@Composable
private fun AnimationGreet(name: String) {
    var expanded by remember { mutableStateOf(false) }

    val extraPadding by animateDpAsState(
        targetValue = if (expanded) 48.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )   // animation에 추가적으로 스프링 효과를 주었다.
    )

    Surface(
        color = MaterialTheme.colors.primary,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Row(modifier = Modifier.padding(24.dp)) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = extraPadding.coerceAtLeast(0.dp)) // 최소 dp값을 주지않으면 앱이 크래시가 난다. java.lang.IllegalArgumentException: Padding must be non-negative

            ) {
                Text(text = "Hello")
                Text(text = name)
            }
            OutlinedButton(onClick = { expanded = !expanded }) {
                Text(if (expanded) stringResource(id = R.string.show_less) else stringResource(id = R.string.show_more))
            }
        }
    }
}


@Composable
private fun Greet(name: String = "Android") {
    val expanded = remember { mutableStateOf(false) }
    val extraPadding = if (expanded.value) 48.dp else 0.dp

    Surface(
        color = MaterialTheme.colors.primary,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Row(modifier = Modifier.padding(24.dp)) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = extraPadding)
            ) {
                Text(text = "Hello")
                Text(
                    text = name,
                    style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.ExtraBold)
                )
            }
            OutlinedButton(onClick = { expanded.value = !expanded.value }) {
                Text(text = if (expanded.value) "Show less" else "Show more")
            }
        }
    }
}

/* https://developer.android.com/codelabs/jetpack-compose-basics?authuser=4&continue=https%3A%2F%2Fdeveloper.android.com%2Fcourses%2Fpathways%2Fcompose%3Fhl%3Den%26authuser%3D4%23codelab-https%3A%2F%2Fdeveloper.android.com%2Fcodelabs%2Fjetpack-compose-basics&hl=en#8 */
// codeLab 9
@Composable
private fun GreetingList(names: List<String> = List(1000) { "$it" }) {
    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
        items(items = names) { name ->
            AnimationGreet(name = name)
        }
    }
}

@Composable
fun OnBoardingScreen(onContinueClicked: () -> Unit) {
    Surface() {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Welcome to the Basics CodeLab!")
            Button(
                onClick = onContinueClicked,
                modifier = Modifier.padding(24.dp)
            ) {
                Text(text = "Continue")
            }
        }
    }
}

@Preview(showBackground = true, name = "onBoardingScreen")
@Composable
fun OnBoardingPreview() {
    JetpackComposeBasicTheme {
        OnBoardingScreen(onContinueClicked = {})
    }
}

@Preview(showBackground = true, name = "Text preview", widthDp = 320)
@Composable
fun DefaultPreview() {
    JetpackComposeBasicTheme {
        MyApp2()
    }
}