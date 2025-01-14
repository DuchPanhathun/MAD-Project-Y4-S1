package kh.edu.rupp.ite.mad_project_y4_s1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kh.edu.rupp.ite.mad_project_y4_s1.model.Banner
import android.util.Log

class BannerViewModel : ViewModel() {
    private val _banners = MutableLiveData<List<Banner>>()
    val banners: LiveData<List<Banner>> = _banners

    fun fetchBanners() {
        FirebaseFirestore.getInstance().collection("banners")
            .whereEqualTo("active", true)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("BannerViewModel", "Listen failed.", e)
                    return@addSnapshotListener
                }

                val bannerList = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Banner::class.java)
                } ?: emptyList()
                
                _banners.value = bannerList
            }
    }
}