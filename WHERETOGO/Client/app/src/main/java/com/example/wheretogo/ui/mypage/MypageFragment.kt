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
import com.example.wheretogo.R
import com.example.wheretogo.data.local.AppDatabase
import com.example.wheretogo.databinding.FragmentMypageBinding
import com.example.wheretogo.ui.MainActivity
import com.example.wheretogo.ui.home.HomeBannerVPAdapter
import com.example.wheretogo.ui.login.LoginActivity
import com.example.wheretogo.ui.setting.SettingActivity
import com.google.android.material.tabs.TabLayoutMediator

class MypageFragment : Fragment() {
    lateinit var binding: FragmentMypageBinding
    lateinit var appDB: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageBinding.inflate(inflater, container, false)
        appDB =AppDatabase.getInstance(requireContext())!!
        initLayout()
        initView()
        setIndicator()
        initClickListener()
//        val users = appDB.userDao().getUserList()
//        Log.d("userlist",users.toString())

        return binding.root
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


    private fun initView(){
        val userIdx: Int = getIdx()
        if (userIdx==-1){
            binding.mypageLoginTv.text ="로그인"
            binding.mypageNicknameTv.text = "로그인하세요"
            binding.mypageEmailTv.text = "로그인 후 사용 가능한 서비스입니다."
            binding.mypageProfileIv.setImageResource(R.drawable.img_change_info_profile)
        }
        else{
            binding.mypageLoginTv.text ="로그아웃"
            binding.mypageNicknameTv.text = appDB.userDao().getNickname(userIdx)
            binding.mypageEmailTv.text = appDB.userDao().getEmail(userIdx)

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