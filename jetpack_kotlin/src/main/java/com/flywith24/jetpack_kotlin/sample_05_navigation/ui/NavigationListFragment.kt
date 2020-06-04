package com.flywith24.jetpack_kotlin.sample_05_navigation.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.flywith24.jetpack_kotlin.R
import com.flywith24.jetpack_kotlin.base.observeEvent
import com.flywith24.jetpack_kotlin.common_data.Configs
import com.flywith24.jetpack_kotlin.databinding.KottlinFragmentListNavigationBinding
import com.flywith24.jetpack_kotlin.sample_04_databinding.ui.adapter.DataBindingMomentAdapter
import com.flywith24.jetpack_kotlin.sample_04_databinding.ui.state.ListViewModel
import com.flywith24.jetpack_kotlin.sample_05_navigation.ui.callback.SharedViewModel
import com.kunminx.architecture.ui.BaseFragment

/**
 * @author Flywith24
 * @date   2020/6/1
 * time   22:02
 * description
 */
class NavigationListFragment : BaseFragment() {

    private val mListViewModel by viewModels<ListViewModel>()
    private val mSharedViewModel by activityViewModels<SharedViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.kottlin_fragment_list_navigation, container, false)
        val binding = KottlinFragmentListNavigationBinding.bind(view)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = mListViewModel
        binding.click = ClickProxy()
        binding.adapter = DataBindingMomentAdapter(requireContext()).apply {
            setOnItemClickListener { item, _ ->
                val bundle = Bundle().apply { putParcelable(Configs.THIS_MOMENT, item) }
                nav().navigate(R.id.action_list_to_detail, bundle)
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.i("yyz11", "onViewCreated NavigationListFragment")

        mListViewModel.getListMutableLiveData().observe(viewLifecycleOwner) { moments ->
            Log.i("yyz11", "1. moments = $moments")

            mListViewModel.list.value = moments
        }

        mSharedViewModel.moment.observeEvent(viewLifecycleOwner) { moment ->
            Log.i("yyz11", "2. moment = $moment")
            mListViewModel.list.value?.let { list ->
                mListViewModel.list.value = ArrayList(list).apply { add(0, moment) }
            }
        }

        mListViewModel.requestList()
    }


    inner class ClickProxy {
        fun fabClick() {
            nav().navigate(R.id.action_list_to_editor)
        }
    }
}