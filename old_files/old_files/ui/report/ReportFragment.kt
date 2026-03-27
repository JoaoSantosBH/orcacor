package com.jomar.senhorpintor.ui.report

import android.Manifest
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.jomar.senhorpintor.R
import com.jomar.senhorpintor.base.*
import com.jomar.senhorpintor.dto.ReportDTO
import com.jomar.senhorpintor.getRecyclerViewScreenshot
import com.jomar.senhorpintor.model.entities.Budget
import com.jomar.senhorpintor.presentation.report.ReportViewModel
import com.jomar.senhorpintor.ui.report.adapter.ReportAdapter
import com.jomar.senhorpintor.util.CreatePdfAndShare
import kotlinx.android.synthetic.main.fragment_report.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class ReportFragment : BaseFragment() {

    private val viewModel: ReportViewModel by viewModel()
    lateinit var  budge: Budget
    lateinit var adapter: ReportAdapter
    lateinit var dto: ReportDTO
     var myList: ArrayList<Any> = arrayListOf()
    var source:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments?.containsKey(BUDGET)!!)
            budge = arguments?.getParcelable(BUDGET)!!
        if (arguments?.containsKey(SRC)!!)
            source = arguments?.getString(SRC)!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        report()
        setupView()
        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
    }

    private fun setupView() {
        setActionBarWithButtonHome(getString(R.string.app_name))
    }

    fun report() = lifecycleScope.launch{
        dto = viewModel.showReport(budge)
        myList.add(dto.header!!)

         for (r in dto.rooms!!){
             myList.add(r)
         }
        myList.add(dto.acessories!!)
         for (m in dto.materials!!){
             myList.add(m)
         }
         myList.add(dto.estimating!!)

        setupAdapter()
    }
    private fun setupAdapter(){
        adapter = ReportAdapter(App.instance)
        val list: List<Any> = myList
        adapter.setData(list)
        report_recycler.adapter = adapter

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_report, menu)
        return super.onCreateOptionsMenu(menu, inflater)

    }
    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        ), REQUEST_SAVE_PDF)
    }

    private fun hasStoragePermission(): Boolean =
            ContextCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PermissionChecker.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PermissionChecker.PERMISSION_GRANTED

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_share -> {
                shareReport()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun shareReport() {
        val screen = report_recycler.getRecyclerViewScreenshot()
        if (hasStoragePermission()){
            CreatePdfAndShare.createPdf(screen!!)
        } else {
            requestStoragePermission()
        }
    }

    override fun onBackPressed(): Boolean {
        if (source == SRC){
            findNavController().navigate(R.id.action_reportFragment_to_homeFragment)
        } else {
            findNavController().navigateUp()
        }
        return super.onBackPressed()
    }
}
