package com.github.billman64.firebasedemo

/* Firebase demo

    To use, download api setup .json from the Firebase console and place in /app directory.
 */

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.github.billman64.firebasedemo.ui.theme.FirebaseDemoTheme
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    val TAG = "firebaseDemo"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FirebaseDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }

        // write to db
        val db = Firebase.database
        val ref = db.getReference("message")

        ref.setValue("asdf1")
        Log.d(TAG, "value written to cloud database")

        // read from db
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                var value = snapshot.getValue()
                Log.d(TAG, " onDataChange() " + value.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Error - read failed", error.toException())
            }
        })


    }
}





@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FirebaseDemoTheme {
        Column {
                Greeting("Spiderman")
                Text("asdf")
            }


    }
}