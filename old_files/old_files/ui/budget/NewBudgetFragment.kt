package com.jomar.senhorpintor.ui.budget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.jomar.senhorpintor.R
import com.jomar.senhorpintor.base.BaseFragment
import com.jomar.senhorpintor.extensions.showToastMessage
import com.jomar.senhorpintor.model.entities.Budget
import com.jomar.senhorpintor.presentation.new_budget.NewBudgetViewModel
import kotlinx.android.synthetic.main.fragment_new_budget.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewBudgetFragment : BaseFragment() {

    private val viewModel: NewBudgetViewModel by viewModel()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_budget, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setActionBarWithButtonHome(getString(R.string.app_name))
    }

    private fun setupListeners() {
        fab_new_orcamento.setOnClickListener {
            validate()
        }
    }

    fun validate() {
        if (edt_register_name.text.isEmpty() || edt_register_email_n.text.isEmpty()) {
            showToastMessage(getString(R.string.validate_destinatarium))
        } else {
            val budget = Budget(
                    null, null, null, edt_register_name.text.toString(), edt_register_email_n.text.toString()
            )
            insertBudget(budget)
        }

    }

    fun insertBudget(budget: Budget) = lifecycleScope.launch {
        viewModel.insert(budget)
        openAddRoomsFragment(viewModel.orderId)
    }

    fun openAddRoomsFragment(order: Long) {
        findNavController().navigate(NewBudgetFragmentDirections.actionBadgeDestinationFragmentToAddRoomsFragment(order))
    }

    override fun onBackPressed(): Boolean {
        findNavController().popBackStack()
        return super.onBackPressed()
    }

}
