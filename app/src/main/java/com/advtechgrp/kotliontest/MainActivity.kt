package com.advtechgrp.kotliontest

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.advtechgrp.kotliontest.ui.theme.KotlionTestTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.random.Random


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KotlionTestTheme {
                AppMainScreen()
            }
        }
    }
}

@Composable
fun AppMainScreen() {
    val scaffoldState = rememberScaffoldState(rememberDrawerState(initialValue = DrawerValue.Closed))
    val scope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = Color.White,
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Compose Message") },
                icon = {
                    Icon(Icons.Outlined.Edit, contentDescription = "compose")
                },
                onClick = {
                    scope.launch {
                        val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                            message = "Work on progess",
                            actionLabel = "Clear"
                        )

                        when (snackbarResult) {
                            SnackbarResult.Dismissed -> Log.d("Snackbar", "Dismissed")
                            SnackbarResult.ActionPerformed -> Log.d("Snackbar", "Snackbar's button clicked")
                        }
                    }
                }
            )
        },
        drawerContent = { NavDrawer()},
        topBar = {
            TopAppBar(
                backgroundColor = Color.White,
                title = {
                    Text(text = "Corrlinks")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            scope.launch { scaffoldState.drawerState.open() }
                        }
                    ) {
                        Icon(Icons.Filled.Menu, contentDescription = "Localized description")
                    }

                },
                actions = {
                    EmailItemAvatarView(avatarLetter = "SK")
                }
            )
        }
    ) {
        EmailsListView()
    }
}

@Composable
fun NavDrawer(){
    Column {
        Divider(color = Color.Black)
        Spacer(modifier = Modifier.height(10.dp))
        NavDrawerContent(title = "Inbox")
        Spacer(modifier = Modifier.height(10.dp))
        NavDrawerContent(title = "Sent")
        Spacer(modifier = Modifier.height(10.dp))
        NavDrawerContent(title = "Drafts")
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun NavDrawerContent(title: String){
    Text(text = title, modifier =  Modifier
        .padding(18.dp))
}

@Composable
fun CustomNavigationBar(scaffoldState: ScaffoldState, scope: CoroutineScope){
    Row(modifier = Modifier.height(10.dp)) {
        Spacer(modifier = Modifier.height(16.dp))
    }
    Card(
        Modifier
            .height(75.dp)
            .fillMaxWidth()
            .padding(top = 20.dp, end = 16.dp, start = 16.dp), elevation = 4.dp, shape = RoundedCornerShape(10.dp)) {

        Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween){
            IconButton(
                onClick = {
                    scope.launch { scaffoldState.drawerState.open() }
                },
                modifier = Modifier.padding(start = 4.dp)
            ) {
                Icon(Icons.Filled.Menu, contentDescription = "menu", modifier = Modifier.fillMaxHeight())

            }

            IconButton(
                onClick = {
                    scope.launch { scaffoldState.drawerState.open() }
                },
                modifier = Modifier.padding(start = 4.dp)
            ) {
                Icon(Icons.Filled.Menu, contentDescription = "menu", modifier = Modifier.fillMaxHeight())

            }
        }
    }

}

sealed class DrawerScreens(val title: String, val route: String) {
    object Home : DrawerScreens("Home", "home")
    object Account : DrawerScreens("Account", "account")
    object Help : DrawerScreens( "Help", "help")
}

private val screens = listOf(
    DrawerScreens.Home,
    DrawerScreens.Account,
    DrawerScreens.Help
)

@Composable
fun ModalDrawerSample() {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 24.dp, top = 48.dp)
            ) {
                Image(
                    painter = painterResource(android.R.drawable.ic_menu_my_calendar),
                    contentDescription = "App icon"
                )
                screens.forEach { screen ->
                    Spacer(Modifier.height(24.dp))
                    Text(
                        text = screen.title,
                        style = MaterialTheme.typography.h4,
                        modifier = Modifier.clickable {
                            //onDestinationClicked(screen.route)
                        }
                    )
                }
            }
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 24.dp, top = 48.dp)
            ) {
                Image(
                    painter = painterResource(android.R.drawable.ic_menu_my_calendar),
                    contentDescription = "App icon"
                )
                screens.forEach { screen ->
                    Spacer(Modifier.height(24.dp))
                    Text(
                        text = screen.title,
                        style = MaterialTheme.typography.h4,
                        modifier = Modifier.clickable {
                            //onDestinationClicked(screen.route)
                        }
                    )
                }
            }
        }
    )
}

@Composable
fun EmailsListView()
{
    var emailsList = generateEmailList()

    LazyColumn {
        items(emailsList) { email ->
            EmailItemView(email)
        }
    }
}

@Composable
fun EmailItemView(email: EmailModel)
{
    Surface(
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 8.dp, end = 8.dp)
    ) {
        Row(
        ) {
            //EmailItemAvatarView(email.sender[0])

            Spacer(modifier = Modifier.size(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                EmailItemSenderView(email.sender, email.date)
                EmailItemTitleView(email.subject)
                EmailItemSummaryView(email.message)
                Spacer(modifier = Modifier.size(10.dp))
            }
        }
    }
}

@Composable
fun EmailItemSenderView(sender: String, date: String)
{
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(
            text = sender,
            style = TextStyle(
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.weight(1f)
        )

        Text(
            text = date,
            style = TextStyle(
                color = Color.DarkGray,
                fontSize = 14.sp
            )
        )
    }
}

@Composable
fun EmailItemTitleView(subject: String)
{
    Text(
        text = subject,
        style = TextStyle(
            color = Color.DarkGray,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    )
}

@Composable
fun EmailItemSummaryView(summary: String)
{
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = summary,
            style = TextStyle(
                color = Color.Gray,
                fontSize = 14.sp
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun EmailItemAvatarView(avatarLetter: String)
{
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(generateRandomColor()),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = avatarLetter.toString(),
            style = TextStyle(
                color = Color.Black,
                fontSize = 15.sp
            )
        )
    }
}

fun generateEmailList() : List<EmailModel>
{
    var list = arrayOf(
        EmailModel(
            sender = "Shrabya Kayastha",
            message = "Demo is developed by Shrabya Kayastha. Build using compose kotlin to test it capabilities",
            subject = "Test",
            date = "Apr 26"
        ),
        EmailModel(
            sender = "John Doe",
            message = "Testing email Testing emailTesting emailTesting emailTesting emailTesting emailTesting emailTesting emailTesting emailTesting email \n",
            subject = "Hello",
            date = "Apr 22"
        ),
        EmailModel(
            sender = "Shrabya Kayastha",
            message = "Demo is developed by Shrabya Kayastha. Build using compose kotlin \n",
            subject = "Test 2",
            date = "Apr 21"
        ),
        EmailModel(
            sender = "Android",
            message = "Build using compose kotlin \n",
            subject = "hello",
            date = "Apr 18"
        ),
        EmailModel(
            sender = "Tom Test",
            message = "Hello how are you?",
            subject = "howdy?",
            date = "Apr 11"
        ),
        EmailModel(
            sender = "Window Update",
            message = "Reminder to update your windows",
            subject = "Update",
            date = "Feb 26"
        ),

        EmailModel(
            sender = "Shrabya Kayastha",
            message = "Demo is developed by Shrabya Kayastha. \n\n Build using compose kotlin \n",
            subject = "Test",
            date = "Jan 26"
        ),
        EmailModel(
            sender = "John Doe",
            message = "Testing email \n",
            subject = "Hello",
            date = "Jan 22"
        ),
        EmailModel(
            sender = "Shrabya Kayastha",
            message = "Demo is developed by Shrabya Kayastha. \n\n Build using compose kotlin \n",
            subject = "Test 2",
            date = "Jan 21"
        ),
        EmailModel(
            sender = "Android",
            message = "Build using compose kotlin \n",
            subject = "hello",
            date = "Jan 18"
        ),
        EmailModel(
            sender = "Tom Test",
            message = "Hello how are you?",
            subject = "howdy?",
            date = "Jan 11"
        ),
        EmailModel(
            sender = "Window Update",
            message = "Reminder to update your windows",
            subject = "Update",
            date = "Jan 26"
        )
    )

    return list.toList()
}

fun generateRandomColor() : Color
{
    var colors = arrayOf(
        Color(0xFF9E9E9E),
        Color(0xFF8B8B8B),
        Color(0xFFF17575),
        Color(0xFF1AC01A),
        Color(0xFF7F7FCA),
        Color(0xFFC5C525),
        Color(0xFF4DC0C0),
        Color(0xFFCE52CE)
    )

    var index = Random(System.currentTimeMillis()).nextInt(from = 0, until = colors.size-1)
    return colors[index]

}
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    KotlionTestTheme {
        AppMainScreen()
    }
}

@Preview
@Composable
fun DefaultPreview1() {
    KotlionTestTheme {
        //CustomNavigationBar()
    }
}