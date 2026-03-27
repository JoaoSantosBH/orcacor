package com.jomar.senhorpintor.ui.add_menu

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.jomar.senhorpintor.R
import com.jomar.senhorpintor.base.*
import com.jomar.senhorpintor.data.GetJsonDataService.Companion.getRoomJsonList
import com.jomar.senhorpintor.extensions.hideKeyboard
import com.jomar.senhorpintor.extensions.showToastMessage
import com.jomar.senhorpintor.model.entities.Budget
import com.jomar.senhorpintor.model.entities.RoomKind
import com.jomar.senhorpintor.presentation.new_budget.NewBudgetViewModel
import com.jomar.senhorpintor.presentation.report.ReportViewModel
import kotlinx.android.synthetic.main.fragment_add_rooms.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class AddRoomsFragment : BaseFragment() {
    private val viewModel: NewBudgetViewModel by sharedViewModel()
    private val viewModelBudget: ReportViewModel by viewModel()
    private lateinit var budget: Budget
    private var idBudget: Long = 0
    private lateinit var simetric: List<RoomKind>
    private lateinit var assimetric: List<RoomKind>
    private lateinit var external: List<RoomKind>
    private lateinit var roomList: List<RoomKind>
    private lateinit var roomKind: RoomKind
    private var listIsEmpty: Boolean = true
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_rooms, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        roomList = getRoomJsonList(App.instance, ROOM)
        idBudget = arguments?.getLong(ORDER_ID)!!
        getBudget()

    }

    private fun setupView() {
        val title = getString(R.string.new_budget) + " " + budget.badgeNumber
        setActionBarWithButtonHome(title)
        setupSimetricSpinner()
        setupAssimetricSpinner()
        setupExternalSpinner()
        setupListeners()
    }

    private fun setupListeners() {
        btn_add_simteric_room.setOnClickListener {
            roomKind = spinnerSimetric.selectedItem as RoomKind
            addSimetricRoom(roomKind)
            this.hideKeyboard()
        }
        btn_add_asimteric_room.setOnClickListener {
            roomKind = spinnerasimetric.selectedItem as RoomKind
            addAssimetricRoom(roomKind)
            this.hideKeyboard()
        }
        btn_add_external_room.setOnClickListener {
            roomKind = spinnerXternal.selectedItem as RoomKind
            addExternalRoom(roomKind)
            this.hideKeyboard()
        }
        btn_finish.setOnClickListener {
            saveBudget()
            this.hideKeyboard()
        }
        btn_add_interruptors.setOnClickListener {
            showToastMessage(getString(R.string.soon_msg))
        }
    }

    private fun setupSimetricSpinner() {
        simetric = roomList.filter { roomKind ->
            roomKind.localization.equals(INTERNAL)
        }.sortedBy { r -> r.name }
        val adapterSimetric = ArrayAdapter(App.instance, android.R.layout.simple_spinner_dropdown_item, simetric)
        spinnerSimetric.adapter = adapterSimetric
    }

    private fun setupAssimetricSpinner() {
        assimetric = roomList.filter { roomKind ->
            roomKind.localization.equals(INTERNAL)
        }.sortedBy { r -> r.name }
        val adapterAssimetric = ArrayAdapter(App.instance, android.R.layout.simple_spinner_dropdown_item, assimetric)
        spinnerasimetric.adapter = adapterAssimetric
    }

    private fun setupExternalSpinner() {
        external = roomList.filter { roomKind ->
            roomKind.localization.equals(EXTERNAL)
        }.sortedBy { r -> r.name }
        val adapterExternal = ArrayAdapter(App.instance, android.R.layout.simple_spinner_dropdown_item, external)
        spinnerXternal.adapter = adapterExternal
    }

    private fun addAssimetricRoom(roomkind: RoomKind) {
        findNavController().navigate(AddRoomsFragmentDirections.actionAddRoomsFragmentToAssimetricRoomFragment(budget.badgeNumber!!, roomkind.name, roomkind?.id.toLong()))
    }

    private fun addSimetricRoom(roomkind: RoomKind) {
        findNavController().navigate(AddRoomsFragmentDirections.actionAddRoomsFragmentToSimetricRoomFragment(budget.badgeNumber!!, roomkind.name, roomkind?.id.toLong()))
    }

    private fun addExternalRoom(roomkind: RoomKind) {
        findNavController().navigate(AddRoomsFragmentDirections.actionAddRoomsFragmentToExternalRoomFragment(budget.badgeNumber!!, roomkind.name, roomkind?.id.toLong()))
    }

    fun getBudget() = lifecycleScope.launch {
        viewModel.getBudget(idBudget)
        budget = viewModel.budget
        setupView()
        checkList()

    }

    fun saveBudget() = lifecycleScope.launch {
        if (listIsEmpty) {
            showToastMessage(getString(R.string.null_budget))
        } else {
            viewModelBudget.saveBudgetReport(budget)
            findNavController().navigate(AddRoomsFragmentDirections.actionAddRoomsFragmentToReportFragment(budget, SRC))
        }
    }

    fun checkList() = lifecycleScope.launch {
        viewModelBudget.getRoomList(budget.badgeNumber!!)
        when (viewModelBudget.roomList.isNotEmpty()) {
            true -> listIsEmpty = false
            else -> listIsEmpty = true
        }
    }

    fun deleteBudget() = lifecycleScope.launch {
        viewModel.deleteBudget(budget)
    }

    fun deleteBudgetAndRooms() = lifecycleScope.launchWhenCreated {
        viewModelBudget.deleteRooms(budget.badgeNumber!!)
        deleteBudget()
    }

    override fun onBackPressed(): Boolean {

        if (this is OnBackPressedListener) {

            makeDIalog()
        } else  {
            findNavController().popBackStack()
        }

        return super.onBackPressed()
    }

    val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            onBackPressed()
        }
    }

    private fun makeDIalog() {
        checkList()
        AlertDialog.Builder(requireContext())
                .setTitle(R.string.back_button_alert_label)
                .setMessage(R.string.back_button_alert)
                .setNegativeButton(R.string.no, null)
                .setPositiveButton(R.string.yes, DialogInterface.OnClickListener { arg0, arg1 ->
                    if (listIsEmpty) {
                        deleteBudget()
                    } else {
                        deleteBudgetAndRooms()
                    }
                    findNavController().navigate(R.id.action_addRoomsFragment_to_homeFragment)
                }).create().show()
    }

    override fun onStart() {
        super.onStart()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    override fun onStop() {
        super.onStop()
        callback.isEnabled = false
    }

}



