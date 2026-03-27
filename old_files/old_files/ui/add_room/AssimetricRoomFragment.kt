package com.jomar.senhorpintor.ui.add_room


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.jomar.senhorpintor.R
import com.jomar.senhorpintor.base.*
import com.jomar.senhorpintor.data.GetJsonDataService.Companion.getClosetJsonList
import com.jomar.senhorpintor.data.GetJsonDataService.Companion.getDoorJsonList
import com.jomar.senhorpintor.data.GetJsonDataService.Companion.getMirrorJsonList
import com.jomar.senhorpintor.data.GetJsonDataService.Companion.getWindowJsonList
import com.jomar.senhorpintor.extensions.hideKeyboard


import com.jomar.senhorpintor.extensions.showToastMessage
import com.jomar.senhorpintor.model.entities.*
import com.jomar.senhorpintor.presentation.rooms.AssimetricRoomViewModel
import com.jomar.senhorpintor.util.getNumberList
import com.jomar.senhorpintor.util.toFloat
import kotlinx.android.synthetic.main.fragment_assimetric_room.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class AssimetricRoomFragment : BaseFragment() {

    private var roomId: Long = 0
    private lateinit var roomName: String
    private lateinit var orderNum: String
    private lateinit var closetList: List<ClosetKind>
    private lateinit var doorList: List<DoorKind>
    private lateinit var mirrorList: List<MirrorKind>
    private lateinit var windowList: List<WindowKind>
    private lateinit var qtyList: List<Int>
    private val viewModel: AssimetricRoomViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_assimetric_room, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getArgs()
        getJSONData()
        setupSpinners()
        setupListener()
        setupView()
    }

    private fun getArgs() {
        if (arguments?.containsKey(ROOM_ID)!!)
            roomId = arguments?.getLong(ROOM_ID) ?: 0
        if (arguments?.containsKey(ROOM_NAME)!!)
            roomName = arguments?.getString(ROOM_NAME) ?: ""
        if (arguments?.containsKey(ORDER_NUM)!!)
            orderNum = arguments?.getString(ORDER_NUM) ?: ""
    }

    private fun setupView() {
        setActionBarWithButtonHome(getString(R.string.assimetric_room))
        tv_note.text = roomName
    }

    private fun setupListener() {
        btn_add_assimtericRoom.setOnClickListener {
            addRoom()
            this.hideKeyboard()
        }
    }

    private fun addRoom() {
        if (!edt_height.text.isEmpty() &&
                !edt_w1.text.isEmpty() &&
                !edt_w2.text.isEmpty() &&
                !edt_w3.text.isEmpty() &&
                !edt_w4.text.isEmpty() &&
                !edt_w5.text.isEmpty() &&
                !edt_w6.text.isEmpty() &&
                !edt_w7.text.isEmpty() &&
                !edt_w8.text.isEmpty()
        ) {
            setupViewData()
        } else {
            showToastMessage(getString(R.string.new_orcament_please_fill_all_fields))
        }
    }

    private fun setupViewData() {
        val doorList: List<Float> = listOf(
                edt_w1.toFloat(),
                edt_w2.toFloat(),
                edt_w3.toFloat(),
                edt_w4.toFloat(),
                edt_w5.toFloat(),
                edt_w6.toFloat(),
                edt_w7.toFloat(),
                edt_w8.toFloat())
        val room = Room(null,
                roomId.toInt(), orderNum, 2, edt_note.text.toString(), mswitch.isChecked,
                spinner_door_qty.selectedItemPosition, spinner_door_kind.selectedItemPosition + 1,
                spinner_window_qty.selectedItemPosition, spinner_window_kind.selectedItemPosition + 1,
                spinner_closet_qty.selectedItemPosition, spinner_closet_kind.selectedItemPosition + 1,
                spinner_mirror_qty.selectedItemPosition, spinner_mirror_kind.selectedItemPosition + 1,
                spinner_demao_qty.selectedItemPosition, 0f, edt_height.toFloat(), 0f, 0f, 0f, 0f)
        createRoom(doorList, room)
    }

    fun createRoom(doorList: List<Float>, room: Room) = lifecycleScope.launch {
        try {
            viewModel.insertAssimetricRoom(doorList, room)
            findNavController().popBackStack()
        } catch (e: Exception) {
            showToastMessage(e.toString())
        }
    }

    private fun setupSpinners() {
        setupDoorSpinners()
        setupWindowSpinners()
        setupClosetSpinners()
        setupMirroSpinners()
        setupDemaoSpinner()
    }

    private fun getJSONData() {
        closetList = getClosetJsonList(App.instance, CLOSET)
        doorList = getDoorJsonList(App.instance, DOOR)
        mirrorList = getMirrorJsonList(App.instance, MIRROR)
        windowList = getWindowJsonList(App.instance, WINDOW)
        qtyList = getNumberList()
    }

    private fun setupDoorSpinners() {
        val doors = doorList.sortedBy { it.id }
        val adapterDoors = ArrayAdapter(App.instance, android.R.layout.simple_spinner_dropdown_item, doors)
        spinner_door_kind.adapter = adapterDoors

        val adapterDoorsQ = ArrayAdapter(App.instance, android.R.layout.simple_spinner_dropdown_item, qtyList)
        spinner_door_qty.adapter = adapterDoorsQ
    }

    private fun setupWindowSpinners() {
        val windows = windowList.sortedBy { it.id }
        val adapterWindows = ArrayAdapter(App.instance, android.R.layout.simple_spinner_dropdown_item, windows)
        spinner_window_kind.adapter = adapterWindows

        val adapterWindowQ = ArrayAdapter(App.instance, android.R.layout.simple_spinner_dropdown_item, qtyList)
        spinner_window_qty.adapter = adapterWindowQ
    }

    private fun setupClosetSpinners() {
        val closets = closetList.sortedBy { it.id }
        val adapterCloset = ArrayAdapter(App.instance, android.R.layout.simple_spinner_dropdown_item, closets)
        spinner_closet_kind.adapter = adapterCloset

        val closetQ = ArrayAdapter(App.instance, android.R.layout.simple_spinner_dropdown_item, qtyList)
        spinner_closet_qty.adapter = closetQ
    }

    private fun setupMirroSpinners() {
        val mirrors = mirrorList.sortedBy { it.id }
        val adapterMirrors = ArrayAdapter(App.instance, android.R.layout.simple_spinner_dropdown_item, mirrors)
        spinner_mirror_kind.adapter = adapterMirrors

        val mirrorsQ = ArrayAdapter(App.instance, android.R.layout.simple_spinner_dropdown_item, qtyList)
        spinner_mirror_qty.adapter = mirrorsQ
    }

    private fun setupDemaoSpinner() {
        val adapter = ArrayAdapter(App.instance, android.R.layout.simple_spinner_dropdown_item, qtyList)
        spinner_demao_qty.adapter = adapter
    }
    override fun onBackPressed(): Boolean {
        findNavController().popBackStack()
        return super.onBackPressed()
    }

}

//TODO VALIDATE INPUT FIELDS
//TODO DIALOGS VALIDATING ROOMS