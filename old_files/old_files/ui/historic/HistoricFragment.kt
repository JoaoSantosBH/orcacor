package com.jomar.senhorpintor.ui.historic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.jomar.senhorpintor.R
import com.jomar.senhorpintor.base.App
import com.jomar.senhorpintor.base.BaseFragment
import com.jomar.senhorpintor.extensions.hide
import com.jomar.senhorpintor.extensions.show
import com.jomar.senhorpintor.extensions.showToastMessage
import com.jomar.senhorpintor.model.entities.Budget
import com.jomar.senhorpintor.presentation.historic.HistoricViewModel
import com.jomar.senhorpintor.ui.historic.adapter.HistoricAdapter
import com.jomar.senhorpintor.ui.historic.adapter.SwipeToDeleteCallback
import kotlinx.android.synthetic.main.fragment_historic.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class HistoricFragment : BaseFragment() {

    private val viewModel: HistoricViewModel by viewModel()
    lateinit var myList: MutableList<Budget>
    lateinit var adapter: HistoricAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_historic, container, false)
    }

    private fun makeAdapter() = HistoricAdapter(myList,
            listener = { openBudget(it) }
    )

    private fun openBudget(it: Budget) {
        findNavController().navigate(HistoricFragmentDirections.actionHistoricFragmentToReportFragment(it, ""))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showPlaceHolder(false)
        getMyHistoric()
        setActionBarWithButtonHome(getString(R.string.app_name))
        setSwipe()
    }

    private fun setSwipe() {
        val swipeHandler = object : SwipeToDeleteCallback(App.instance) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = myHistoryRecycler.adapter as HistoricAdapter

                val budgetToRemove = myList[viewHolder.adapterPosition]
                adapter.removeAt(viewHolder.adapterPosition)
                removeBudget(budgetToRemove)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(myHistoryRecycler)
    }

    private fun removeBudget(budgetToRemove: Budget) = lifecycleScope.launch {
        viewModel.deleteBudgetAndRooms(budgetToRemove)
        showToastMessage(getString(R.string.deleted_msg))
    }


    private fun setupAdapter() {
        adapter = makeAdapter()
        myHistoryRecycler.adapter = adapter
    }

    private fun getMyHistoric() = lifecycleScope.launch {
        viewModel.getBudgets()
        myList = viewModel.list
        setupAdapter()
        if (myList.isEmpty()) showPlaceHolder(true)
    }


    private fun showPlaceHolder(show: Boolean) {
        when (show) {
            true -> {
                placeholder.show()
                tv_placeholder.show()
            }
            else -> {
                placeholder.hide()
                tv_placeholder.hide()
            }
        }
    }

    override fun onBackPressed(): Boolean {
        findNavController().popBackStack()
        return super.onBackPressed()
    }

}
