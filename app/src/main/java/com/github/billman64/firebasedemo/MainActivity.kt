package com.github.billman64.firebasedemo

/* Firebase demo

    To use, download api setup .json from the Firebase console and place in /app directory.
 */

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import com.github.billman64.firebasedemo.ui.theme.FirebaseDemoTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthProvider
import com.google.firebase.auth.ktx.auth
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
                    Column{
                        Greeting("Firebase demo")

                        var textInput by remember {mutableStateOf("")}

                        Row{
                            Text("Enter value to write to db: ")
                            BasicTextField(
                                value = textInput,
                                modifier = Modifier.background(Color.Green),
                                onValueChange = {textInput = it},

                            )
                        }
                        Button(

                            onClick = { writeToDb(textInput) }
                        ){Text("save to db")}

                        var dbOutput by remember { mutableStateOf("") }
//                        Text(
//                            text = dbOutput
//                        )



                        // read from db
                        val db = Firebase.database
                        val ref = db.getReference("message")
                        ref.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {

                                var value = snapshot.getValue().toString()
                                Log.d(TAG, " Reading db. onDataChange() " + value.toString())
                                dbOutput = value  // does now work
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.e(TAG, "Error - read failed", error.toException())
                            }
                        })

                        showData(dbOutput)

                    }

                }
            }


        }

    }


    fun writeToDb(value:String){

        // write to db
        val db = Firebase.database
        var ref = db.getReference("message")
        var f = Firebase.auth.signInWithCustomToken("3oBaTMOq7ccp4nwOYN1ouUYqRbg1") // temporary. Will be changed and hidden later.


        val result = ref.setValue(value).isSuccessful
        if(result){
            Log.d(TAG, "value $value written to cloud database")
        } else {
            Log.e(TAG, " value write not successful! (value: $value) ${f.exception?.message}")
        }


    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "$name",
        modifier = modifier
    )
}

@Composable
fun showData(data: String){
    Column{
        Text(
            text = "Data from cloud db:"
        )

        Text(
            text = data,
            modifier = Modifier.background(Color.Black),
            color = Color.Green,
            fontFamily = FontFamily.Monospace
        )

    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FirebaseDemoTheme {
        Column {
                Greeting("Spiderman")
                showData(data = "sample db data")
                Text("sample text")
            }
    }
}
