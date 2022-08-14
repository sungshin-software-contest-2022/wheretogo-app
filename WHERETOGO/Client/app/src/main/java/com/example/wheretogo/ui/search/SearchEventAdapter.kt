package com.example.wheretogo.ui.search


import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.wheretogo.R
import com.example.wheretogo.data.remote.search.EventResult
import com.example.wheretogo.data.remote.search.SavedInfo
import com.example.wheretogo.data.remote.search.SearchService
import com.example.wheretogo.data.remote.search.VisitedInfo
import com.example.wheretogo.ui.detail.DetailActivity
import java.util.*


class SearchEventAdapter(var events: ArrayList<EventResult>, var con: Context) :
    RecyclerView.Adapter<SearchEventAdapter.ViewHolder>() {
    var TAG = "SearchEventListner"

    private val searchService = SearchService
    var filteredEvents = ArrayList<EventResult>()
    private var isSavedBtnSelected :Boolean = false
    private var isVisitedBtnSelected :Boolean = false
    val userIdx = getIdx()


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var eventName : TextView
        var startDate : TextView
        var endDate:TextView
        var hashtag1:TextView
        var hashtag2:TextView
        var hashtag3:TextView

        var visitedBtn : ImageButton
        var likedBtn : ImageButton


        init {
            eventName = itemView.findViewById(R.id.eventName)
            startDate = itemView.findViewById(R.id.startDate)
            endDate = itemView.findViewById(R.id.endDate)


            hashtag1 = itemView.findViewById(R.id.hashtag1)
            hashtag2 = itemView.findViewById(R.id.hashtag2)
            hashtag3 = itemView.findViewById(R.id.hashtag3)

            visitedBtn = itemView.findViewById(R.id.visitedBtn)
            likedBtn = itemView.findViewById(R.id.likedBtn)


            itemView.setOnClickListener {
              val intent = Intent(con, DetailActivity::class.java)
              con.startActivity(intent)
            }
        }

    }
    init {
        filteredEvents.addAll(events)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val con = parent.context
        val inflater = con.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_recycle_event, parent, false)


        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: SearchEventAdapter.ViewHolder, position: Int) {
        val event: EventResult = filteredEvents[position]

        fun getSavedInfo() : SavedInfo {
            val userID = userIdx
            val eventID = event.eventID

            return SavedInfo(userID, eventID)
        }

        fun getVisitedInfo() : VisitedInfo {
            val userID = userIdx
            val eventID = event.eventID
            val assess = "g"

            return VisitedInfo(userID, eventID, assess)
        }

        holder.eventName.text = event.eventName
        holder.startDate.text = event.startDate.slice(IntRange(0,9))
        if(event.endDate != null)
            holder.endDate.text = event.endDate.slice((IntRange(0,9)))
        else event.endDate

        holder.hashtag1.text = "#" + event.genre
        holder.hashtag2.text = "#" + event.theme
        holder.hashtag3.text = "#" + event.kind

        SearchService.getIsSavedEvent(this, userIdx, event.eventID)
        SearchService.getIsVisitedEvent(this, userIdx, event.eventID)

        if (isVisitedBtnSelected)
            holder.visitedBtn.setBackgroundResource(R.drawable.btn_check_click)
        else
            holder.visitedBtn.setBackgroundResource(R.drawable.btn_check_unclick)


        if(isSavedBtnSelected)
            holder.likedBtn.setBackgroundResource(R.drawable.btn_like_click)
        else
            holder.likedBtn.setBackgroundResource(R.drawable.btn_like_unclick)


        holder.visitedBtn.setOnClickListener {
//            if(userIdx==-1){
//                //toast. 로그인이 필요한 서비스입니다.
//                //자동로그인
//                println("로그인이 필요한 서비스입니다")
//            }
//            else{
            //visited 버튼이 비활성화 상태일 경우
            if (!isVisitedBtnSelected){
                holder.visitedBtn.setBackgroundResource(R.drawable.btn_check_click)
                isVisitedBtnSelected=true

                //VisitedTBL에 저장
                searchService.setVisitedEvent(this, getVisitedInfo())
                //로컬 savedDB에 저장

            }
            //vistied 버튼이 활성화 상태일 경우
            else{
                holder.visitedBtn.setBackgroundResource(R.drawable.btn_check_unclick)
                isVisitedBtnSelected=false
                //VistedTBL에서 삭제
                searchService.setDeleteVisitedEvent(this, userIdx, event.eventID)
            }
//            }
        }


        holder.likedBtn.setOnClickListener {
//            if(userIdx==-1){
//                //toast. 로그인이 필요한 서비스입니다.
//                //자동로그인
//                println("로그인이 필요한 서비스입니다")
//            }
//            else {
            //isLike 버튼이 비활성화 상태일 경우
            if (!isSavedBtnSelected) {
                holder.likedBtn.setBackgroundResource(R.drawable.btn_like_click)
                isSavedBtnSelected=true

                //savedTBL에 저장
                searchService.setSavedEvent(this, getSavedInfo())
                //로컬 savedDB에 저장

            }
            //isLike버튼이 활성화 상태일 경우
            else {
                holder.likedBtn.setBackgroundResource(R.drawable.btn_like_unclick)
                isSavedBtnSelected=false
                //savedTBL에 삭제
                searchService.setDeleteSavedEvent(this, userIdx, event.eventID)
            }
        }
//        }
    }


    override fun getItemCount(): Int {
        return filteredEvents.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    fun isSavedBtnSelected(result: Boolean){
        isSavedBtnSelected=result
    }

    fun isVisitedBtnSelected(result: Boolean) {
        isVisitedBtnSelected=result
    }

    fun setMyEvent(result: Boolean) : Boolean{
        return result
    }


    //유저 인덱스 가져오는 함수
    private fun getIdx(): Int {
        val spf = con?.getSharedPreferences("userInfo", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getInt("userIdx",-1)
    }




}
