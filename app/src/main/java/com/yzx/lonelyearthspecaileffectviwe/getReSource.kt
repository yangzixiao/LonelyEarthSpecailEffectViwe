package com.yzx.lonelyearthspecaileffectviwe

import android.content.res.Resources


fun getReSource(): Resources {
    return Resources.getSystem()
}


fun getScreenWidth():Int{
   return getReSource().displayMetrics.widthPixels
}