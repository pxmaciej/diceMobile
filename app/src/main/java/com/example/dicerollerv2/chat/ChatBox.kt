package com.example.dicerollerv2.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dicerollerv2.R

class ChatBox(private val ChatBox: ArrayList<ChatLog>) : RecyclerView.Adapter<ChatBox.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatBox.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.chat_items, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: ChatBox.MyViewHolder, position: Int) {
        val chatLog = ChatBox[position]

        holder.textRoll.text = chatLog.text
    }
    override fun getItemCount(): Int {
        return ChatBox.size
    }

    public class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val textRoll : TextView = itemView.findViewById(R.id.textRoll)
    }
}