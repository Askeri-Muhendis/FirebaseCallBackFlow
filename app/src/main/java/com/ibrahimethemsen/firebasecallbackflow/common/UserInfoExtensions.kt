package com.ibrahimethemsen.firebasecallbackflow.common

import android.content.Context
import android.view.View
import android.widget.Toast

fun Context.userInfoMessage(msg : String,duration : Int){
    Toast.makeText(this,msg,duration).show()
}

fun View.setVisibility(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}