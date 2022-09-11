package com.example.wheretogo.ui.mypage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.wheretogo.BaseFragment
import com.example.wheretogo.R
import com.example.wheretogo.data.local.AppDatabase
import com.example.wheretogo.data.remote.auth.AuthRetrofitInterface
import com.example.wheretogo.data.remote.auth.GetNameResponse
import com.example.wheretogo.data.remote.auth.getRetrofit
import com.example.wheretogo.databinding.FragmentMypageBinding
import com.example.wheretogo.ui.MainActivity
import com.example.wheretogo.ui.home.HomeBannerVPAdapter
import com.example.wheretogo.ui.login.LoginActivity
import com.example.wheretogo.ui.setting.SettingActivity
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MypageFragment : BaseFragment<FragmentMypageBinding>(FragmentMypageBinding::inflate) {

    private val service = getRetrofit().create(AuthRetrofitInterface::class.java)

    override fun initAfterBinding() {
        initLayout()
        initView()
        setIndicator()
        getName(getIdx())
        getEmail()
        initClickListener()
    }

    override fun onStart() {
        super.onStart()
        initView()
    }

    private fun initLayout(){
        val bannerAdapter = HomeBannerVPAdapter(this)
        //추가할 프래그먼트를 넣어줌
        bannerAdapter.addFragment(MypageSavedFragment())
        bannerAdapter.addFragment(MypageVisitedFragment())

        //속성값들
        binding.mypageVp.adapter = bannerAdapter
        binding.mypageVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
    }

    private fun setIndicator(){
        val viewPager2 = binding.mypageVp
        val tabLayout = binding.mypageTabLayout

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
        }.attach()

    }

    private fun initClickListener(){
        binding.mypageLoginTv.setOnClickListener {
            if (binding.mypageLoginTv.text == "로그인")
                startActivity(Intent(context, LoginActivity::class.java))
            else {
                logout()
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
            }
        }

        binding.mypageSettingIv.setOnClickListener {
            startActivity(Intent(context, SettingActivity::class.java))
        }
    }

    //유저 인덱스 가져옴
    private fun getIdx(): Int {
        val spf = activity?.getSharedPreferences("userInfo", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getInt("userIdx",-1)
    }

    private fun getName(userIdx: Int){
        service.getName(userIdx).enqueue(object: Callback<GetNameResponse> {
            override fun onResponse(call: Call<GetNameResponse>, response: Response<GetNameResponse>) {
                val resp = response.body()!!
                when(resp.code){
                    200->{
                        val spf = activity!!.getSharedPreferences("userInfo", AppCompatActivity.MODE_PRIVATE)
                        val editor = spf.edit()
                        Log.d("nickname",resp.results!!.nickName)
                        editor.putString("nickname",resp.results!!.nickName)
                        editor.apply()
                    }
                }
            }
            override fun onFailure(call: Call<GetNameResponse>, t: Throwable) {
            }
        })
    }

    //유저 닉네임 가져옴
    private fun getName(): String {
        val spf = activity?.getSharedPreferences("userInfo", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getString("nickname","USER")!!
    }

    private fun getEmail(): String {
        val spf = activity?.getSharedPreferences("userInfo", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getString("email","")!!
    }
    private fun initView(){
        val userIdx: Int = getIdx()
        if (userIdx==-1){
            binding.mypageLoginTv.text ="로그인"
            binding.mypageNicknameTv.text = "로그인하세요"
            binding.mypageEmailTv.text = "로그인 후 사용 가능한 서비스입니다."
            binding.mypageSettingIv.visibility = View.INVISIBLE
        }
        else{
            binding.mypageLoginTv.text ="로그아웃"
            binding.mypageNicknameTv.text = getName()
            binding.mypageEmailTv.text = getEmail()
            binding.mypageSettingIv.visibility = View.VISIBLE
        }
    }

    private fun logout(){
        val spf = activity?.getSharedPreferences("userInfo",AppCompatActivity.MODE_PRIVATE)
        val editor = spf!!.edit()
        editor.remove("userIdx") //키값에 저장된값 삭제-> idx=-1
        editor.apply()
        binding.mypageLoginTv.text = "로그인"
    }

}