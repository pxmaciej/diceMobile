package com.example.dicerollerv2.chat

import java.util.Date


data class ChatLog(val userId: String?, val text: String?, val rollDate: Date?, val rollValue: Int?)
{
}
