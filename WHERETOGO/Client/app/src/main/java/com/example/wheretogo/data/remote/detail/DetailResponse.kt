package com.example.wheretogo.data.remote.detail

import com.google.gson.annotations.SerializedName

//상세정보 조회 응답값
data class DetailInfoResponse(
    @SerializedName(value = "code")val code:Int,
    @SerializedName(value = "isSuccess")val isSuccess:Boolean,
    @SerializedName(value = "results")val results:DetailInfoResult
)

data class DetailInfoResult(
    @SerializedName(value = "eventID")val eventID:Int,
    @SerializedName(value = "eventName")val eventName: String,
    @SerializedName(value = "startDate")val startDate: String,
    @SerializedName(value = "savedNum")val savedNum: Int?,
    @SerializedName(value = "visitedNum")val visitedNum: Int?,
    @SerializedName(value = "endDate")val endDate: String,

    @SerializedName(value = "kind")val kind: String?,
    @SerializedName(value = "pic")val pic: String?,

    @SerializedName(value = "place")val place: String?,
    @SerializedName(value = "detailedPlace")val detailedPlace: String?,

    @SerializedName(value = "tel")val tel: String?,
    @SerializedName(value = "telname")val telname: String?,

    @SerializedName(value = "agelimit")val agelimit: String?,
    @SerializedName(value = "eventtime")val eventtime: String?,

    @SerializedName(value = "homepage")val homepage: String?,//위치 상세정보
    @SerializedName(value = "overview")val overview: String?,
    @SerializedName(value = "eventplace")val eventplace: String?,
    @SerializedName(value = "bookingplace")val bookingplace: String?,
    @SerializedName(value = "subevent")val subevent: String?,
    @SerializedName(value = "price")val price: String?

)

//VisitedTBL 저장 여부 조회
data class DetailIsVisitedResponse(
    @SerializedName(value = "isVisited")val isVisited:Boolean,
    @SerializedName(value = "code")val code:Int,
    @SerializedName(value = "isSuccess")val isSuccess:Boolean
)

//SavedTBL 저장 여부 조회
data class DetailIsSavedResponse(
    @SerializedName(value = "isSaved")val isSaved:Boolean,
    @SerializedName(value = "code")val code:Int,
    @SerializedName(value = "isSuccess")val isSuccess:Boolean
)


//savedTBL에 추가
data class DetailSaveEventResponse(
    @SerializedName("msg") var msg : String,
    @SerializedName("code") var code : Int,
    @SerializedName("isSuccess") var isSuccess : Boolean
)

//savedTBL에 삭제
data class DetailDeleteSavedResponse(
    @SerializedName("msg") var msg : String,
    @SerializedName("code") var code : Int,
    @SerializedName("isSuccess") var isSuccess : Boolean
)


//visitedTBL에 추가
data class DetailVisitEventResponse(
    @SerializedName("msg") var msg : String,
    @SerializedName("code") var code : Int,
    @SerializedName("isSuccess") var isSuccess : Boolean,
)

//savedTBL에 삭제
data class DetailDeleteVisitedResponse(
    @SerializedName("msg") var msg : String,
    @SerializedName("code") var code : Int,
    @SerializedName("isSuccess") var isSuccess : Boolean,
)



