package com.softradix.jetpackcomposedemo.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.softradix.jetpackcomposedemo.database.entity.*
import com.softradix.jetpackcomposedemo.ui.theme.JetpackComposeDemoTheme
import com.softradix.jetpackcomposedemo.ui.theme.LightGray100
import com.softradix.jetpackcomposedemo.ui.theme.Purple500
import com.softradix.jetpackcomposedemo.viewModel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RoomDbActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    NestDb()
                }
            }
        }
    }
}

@Preview(name = "Nest DB")
@Composable
fun NestDbPreview() {
    NestDb()

}


@Composable
fun NestDb(viewModel: UserViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var userId by remember { mutableStateOf("") }

    // Insert Data
    viewModel.addUser(userData)
    viewModel.addPlaylist(PlaylistData)
    viewModel.addSong(SongData)
    viewModel.addPlaylistSongCrossRef(PlaylistSongCrossRefData)

    val getUserPlaylistWithSong = viewModel.readUserData.observeAsState(listOf()).value
    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Purple500)
                    .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Nested Relationship",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp, 15.dp, 15.dp, 0.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = userId,
                    onValueChange = { userId = it },
                    label = { Text(text = "Enter User Id") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.8f)
                )

                Spacer(modifier = Modifier.height(25.dp))

                Button(
                    onClick = {
                        scope.launch {
                            viewModel.getUser(userId.toInt())
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(60.dp)
                        .padding(10.dp),
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(Purple500)
                ) {
                    Text(text = "Submit", color = Color.White, fontSize = 13.sp)
                }

                Spacer(modifier = Modifier.height(30.dp))


                if (getUserPlaylistWithSong.isNotEmpty()) {
                    LazyColumn {
                        item {
                            UserHeader()
                        }

                        //UserData
                        items(getUserPlaylistWithSong.size) { index ->
                            UserDataList(getUserPlaylistWithSong[index])
                        }

                        //Playlist Data
                        item {
                            Spacer(modifier = Modifier.height(30.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Purple500)
                                    .padding(15.dp)
                            ) {
                                Text(
                                    text = "Playlist Id",
                                    modifier = Modifier.weight(0.3f),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold)

                                Text(
                                    text = "Playlist Name",
                                    modifier = Modifier.weight(0.3f),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold)
                            }
                        }

                        items(getUserPlaylistWithSong[0].playlists.size) { index ->
                            PlaylistDataList(getUserPlaylistWithSong[0].playlists[index])
                        }

                        //Song Data
                        item {
                            Spacer(modifier = Modifier.height(30.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Purple500)
                                    .padding(15.dp)
                            ) {
                                Text(
                                    text = "Song Id",
                                    modifier = Modifier.weight(0.3f),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Song Name",
                                    modifier = Modifier.weight(0.3f),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Artist Name",
                                    modifier = Modifier.weight(0.3f),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        items(getUserPlaylistWithSong[0].playlists[0].song.size) { index ->
                            SongDataList(getUserPlaylistWithSong[0].playlists[0].song[index])
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Purple500)
            .padding(15.dp)
    ) {
        Text(
            text = "User Id",
            modifier = Modifier.weight(0.3f),
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Name",
            modifier = Modifier.weight(0.3f),
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Age",
            modifier = Modifier.weight(0.3f),
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun UserDataList(userWithPlaylistAndSongs: UserWithPlaylistAndSongs) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(LightGray100)
            .padding(15.dp)
    ) {
        Text(
            text = userWithPlaylistAndSongs.user.userId.toString(),
            modifier = Modifier.weight(0.3f),
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = userWithPlaylistAndSongs.user.name,
            modifier = Modifier.weight(0.3f),
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = userWithPlaylistAndSongs.user.age.toString(),
            modifier = Modifier.weight(0.3f),
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun PlaylistDataList(playlistWithSongs: PlaylistWithSongs) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(LightGray100)
            .padding(15.dp)
    ) {
        Text(
            text = playlistWithSongs.playlist.playlistId.toString(),
            modifier = Modifier.weight(0.3f),
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = playlistWithSongs.playlist.playlistName,
            modifier = Modifier.weight(0.3f),
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun SongDataList(song: Song) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(LightGray100)
            .padding(15.dp)
    ) {
        Text(
            text = song.songId.toString(),
            modifier = Modifier.weight(0.3f),
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = song.songName,
            modifier = Modifier.weight(0.3f),
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = song.artist,
            modifier = Modifier.weight(0.3f),
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
fun Greeting3(name: String) {
    Text(text = "Hello $name!")
}


val userData = listOf(
    User(1, "User 1", 10),
    User(2, "User 2", 11),
    User(3, "User 3", 12),
    User(4, "User 4", 13),
    User(5, "User 5", 14)
)

val PlaylistData = listOf(
    Playlist(1, 1, "PlayList 1"),
    Playlist(2, 2, "PlayList 2"),
    Playlist(3, 1, "PlayList 3"),
    Playlist(3, 3, "PlayList 3"),
    Playlist(4, 2, "PlayList 4"),
    Playlist(5, 4, "PlayList 5"),
    Playlist(6, 5, "PlayList 6"),
    Playlist(7, 1, "PlayList 7")
)

val SongData = listOf(
    Song(1, "Song 1", "Artist 1"),
    Song(2, "Song 2", "Artist 2"),
    Song(3, "Song 3", "Artist 3"),
    Song(4, "Song 4", "Artist 4"),
    Song(5, "Song 5", "Artist 5"),
    Song(6, "Song 6", "Artist 6"),
    Song(7, "Song 7", "Artist 7")
)

val PlaylistSongCrossRefData = listOf(
    PlaylistSongCrossRef(1, 1),
    PlaylistSongCrossRef(1, 3),
    PlaylistSongCrossRef(2, 4),
    PlaylistSongCrossRef(1, 5),
    PlaylistSongCrossRef(3, 4),
    PlaylistSongCrossRef(2, 5),
    PlaylistSongCrossRef(5, 7)
)