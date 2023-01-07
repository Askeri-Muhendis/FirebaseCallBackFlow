package com.ibrahimethemsen.firebasecallbackflow.common

import android.content.Context
import android.widget.Toast

fun Context.userInfoMessage(msg : String,duration : Int){
    Toast.makeText(this,msg,duration).show()
}