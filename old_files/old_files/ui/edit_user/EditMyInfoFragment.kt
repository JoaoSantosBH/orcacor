package com.jomar.senhorpintor.ui.edit_user

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
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
import kotlinx.android.synthetic.main.fragment_edit_my_info.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class EditMyInfoFragment : BaseFragment() {
    private val viewModel: RegisterViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit_my_info, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = getString(R.string.my_info)
        showToolbarAndTitle(title, false)
        getUser()

    }

    private fun setupView() {
        setActionBarWithButtonHome(getString(R.string.app_name))
        val name = viewModel.myUser.nome
        val email = viewModel.myUser.email
        val zap = viewModel.myUser.zapUsuario

        edt_register_name.text = Editable.Factory.getInstance().newEditable(name)
        edt_register_email.text = Editable.Factory.getInstance().newEditable(email)
        edt_register_phone.text = Editable.Factory.getInstance().newEditable(zap)

        edt_register_email.addTextChangedListener(PhoneNumberFormattingTextWatcher())
        tv_terms_of_service.setOnClickListener {
            findNavController().navigate(EditMyInfoFragmentDirections.actionEditMyInfoFragmentToTermsOfUseFragmentHome2(TERMS_URL))
        }

        buttonSaveRegister.setOnClickListener {
            val result = validateFields()
            when (result) {
                true -> {
                    when (checkBox.isChecked) {
                        true -> {
                            updateUser()
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

    fun getUser() = lifecycleScope.launch {
        viewModel.getUser()
        setupView()
    }

    private fun openHome() {
        findNavController().navigate(R.id.action_editMyInfoFragment_to_homeFragment)
    }

    private fun updateUser() = lifecycleScope.launch {
        val name = edt_register_name.text.toString()
        val email = edt_register_email.text.toString()
        val zap = edt_register_phone.text.toString()
        val user = User(viewModel.myUser.id, name, email, zap)
        viewModel.updateUser(user)
    }

    private fun validateFields(): Boolean {
        if (edt_register_name.text.toString() != "" &&
                edt_register_email.text.toString() != "" &&
                edt_register_email.text.toString() != "") {
            return true
        }
        return false
    }

    override fun onBackPressed(): Boolean {
        findNavController().popBackStack()
        return super.onBackPressed()
    }
}
