package com.jomar.senhorpintor.ui.register

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.jomar.senhorpintor.R
import com.jomar.senhorpintor.base.BaseFragment
import com.jomar.senhorpintor.base.TERMS_URL
import com.jomar.senhorpintor.model.entities.User
import com.jomar.senhorpintor.presentation.register.RegisterViewModel
import com.jomar.senhorpintor.ui.register.RegisterFragmentDirections.Companion.actionFragmentRegisterToTermsOfUseFragment
import kotlinx.android.synthetic.main.register_fragment.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class RegisterFragment : BaseFragment() {

    private val viewModel: RegisterViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.register_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = getString(R.string.my_info)
        showToolbarAndTitle(title, false)
        setupView()
    }

    private fun setupView() {
        edt_register_email.addTextChangedListener(PhoneNumberFormattingTextWatcher())
        tv_terms_of_service.setOnClickListener {
            findNavController().navigate(actionFragmentRegisterToTermsOfUseFragment(TERMS_URL))
        }

        buttonSaveRegister.setOnClickListener {
            val result = validateFields()
            when (result) {
                true -> {
                    when(checkBox.isChecked){
                        true -> {
                            saveUser()
                            makeFirstAcces()
                            openHome()
                        }
                        else -> Toast.makeText(requireActivity(), R.string.validate_register_chk_box, Toast.LENGTH_SHORT).show()
                    }
                }
                else -> {
                    Toast.makeText(requireActivity(), R.string.validate_register, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun openHome(){
        findNavController().navigate(R.id.action_fragmentRegister_to_homeActivity)

    }
    private fun makeFirstAcces() = lifecycleScope.launch {
        viewModel.makeFirstAccess()
    }

    private fun saveUser() = lifecycleScope.launch{
        val name = edt_register_name.text.toString()
        val email = edt_register_email.text.toString()
        val zap = edt_register_phone.text.toString()
        val user = User(null, name, email, zap)
        viewModel.saveUser(user)
    }

    private fun validateFields(): Boolean {
        if (edt_register_name.text.toString() != "" &&
                edt_register_email.text.toString() != "" &&
                edt_register_email.text.toString() != "") {
            return true
        }
        return false
    }
}