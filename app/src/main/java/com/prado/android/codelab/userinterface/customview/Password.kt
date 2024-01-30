package com.prado.android.codelab.userinterface.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.annotation.LayoutRes
import androidx.annotation.StyleableRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.prado.android.R
import com.prado.android.databinding.ItemPasswordBinding
import kotlin.properties.Delegates

abstract class Password @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = R.style.CustomComponents_TextInputLayout,
    @LayoutRes layoutRes: Int = R.layout.item_password,
    @StyleableRes styleableRes: IntArray = R.styleable.PasswordItem
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var binding: ItemPasswordBinding
    private val emptyString = ""
    private val defaultMinLength = 4
    private val defaultMaxLength = 25

    var passwordForgottenButtonEnabled: Boolean by Delegates.observable(false) { _, _, new ->
        binding.pwdButton.visibility = if (new) VISIBLE else GONE
        binding.pwdLayout.endIconMode = if (!new) TextInputLayout.END_ICON_PASSWORD_TOGGLE else TextInputLayout.END_ICON_NONE
    }

    var passwordForgottenButtonText: String by Delegates.observable(initialValue = emptyString) { _, old, new ->
        if (old != new) binding.pwdButton.text = new
    }

    var passwordCounterEnabled: Boolean by Delegates.observable(false) { _, _, new ->
        binding.pwdLayout.isCounterEnabled = new
    }

    var passwordCounterMaxLength: Int by Delegates.observable(defaultMaxLength) { _, _, new ->
        binding.pwdLayout.counterMaxLength = new
    }

    var passwordHintText: String by Delegates.observable(initialValue = resources.getString(R.string.input_password_hint_text)) { _, old, new ->
        if (old != new) binding.pwdLayout.hint = new
    }

    var passwordErrorText: String = resources.getString(R.string.input_password_error_text_default)
    var passwordCounterMinLength: Int = defaultMinLength

    var passwordSuccessTextEnabled: Boolean by Delegates.observable(false) { _, _, new ->
        binding.pwdLayout.isHelperTextEnabled = new
    }

    var passwordSuccessText: String by Delegates.observable(resources.getString(R.string.input_helper_text_blank_space)) { _, old, new ->
        if (binding.pwdEdit.text != null && binding.pwdEdit.text.toString().length > passwordCounterMinLength) {
            if (old != new) binding.pwdLayout.helperText = new
        }
    }

    val passwordForgottenButton: MaterialButton?
        get() = binding.pwdButton

    init {
        inflate(context, layoutRes, this)
        binding = ItemPasswordBinding.bind(rootView)
        context.theme.obtainStyledAttributes(
            attrs,
            styleableRes,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {

                passwordForgottenButtonText = getString(R.styleable.PasswordItem_passwordForgottenButtonText)
                    ?: emptyString
                passwordHintText = getString(R.styleable.PasswordItem_passwordHintText)
                    ?: resources.getString(R.string.input_password_hint_text)
                passwordForgottenButtonEnabled = getBoolean(R.styleable.PasswordItem_passwordForgottenButtonEnabled, false)
                passwordCounterEnabled = getBoolean(R.styleable.PasswordItem_passwordCounterEnabled, false)
                passwordCounterMaxLength = getInt(R.styleable.PasswordItem_passwordCounterMaxLength, defaultMaxLength)
                passwordCounterMinLength = getInt(R.styleable.PasswordItem_passwordCounterMinLength, defaultMinLength)
                passwordSuccessTextEnabled = getBoolean(R.styleable.PasswordItem_passwordSuccessTextEnabled, false)
                val blankSpace = resources.getString(R.string.input_helper_text_blank_space)
                val customHelperText = getString(R.styleable.PasswordItem_passwordSuccessText)
                passwordSuccessText = customHelperText ?: blankSpace
                val customErrorText = getString(R.styleable.PasswordItem_passwordErrorText)
                val defaultErrorText = resources.getString(R.string.input_password_error_text_default)
                passwordErrorText = customErrorText ?: defaultErrorText
            } finally {
                recycle()
            }
        }
        binding.pwdEdit.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                if (binding.pwdEdit.text.toString().length in passwordCounterMinLength..passwordCounterMaxLength) {
                    binding.pwdLayout.isErrorEnabled = false
                    if (passwordSuccessTextEnabled) binding.pwdLayout.helperText = passwordSuccessText
                } else {
                    if (binding.pwdEdit.text != null && binding.pwdEdit.text.toString().isEmpty()) {
                        showErrorHelperText()
                    }
                }
            }
        }
        binding.pwdEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(chars: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(chars: CharSequence?, start: Int, before: Int, count: Int) = Unit

            override fun afterTextChanged(editable: Editable?) {
                val passwordLayout = binding.pwdLayout
                val passwordForgottenButton = binding.pwdButton
                if (editable.isNullOrEmpty()) {
                    if (passwordForgottenButton.visibility != VISIBLE) passwordForgottenButton.visibility = VISIBLE
                    if (passwordLayout.endIconMode != TextInputLayout.END_ICON_NONE) passwordLayout.endIconMode = TextInputLayout.END_ICON_NONE
                } else {
                    if (passwordForgottenButton.visibility != GONE) passwordForgottenButton.visibility = GONE
                    if (passwordLayout.endIconMode != TextInputLayout.END_ICON_PASSWORD_TOGGLE) passwordLayout.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
                    if (binding.pwdEdit.text.toString().length in passwordCounterMinLength..passwordCounterMaxLength) {
                        passwordLayout.isErrorEnabled = false
                        if (passwordSuccessTextEnabled) passwordLayout.helperText = passwordSuccessText
                    } else {
                        showErrorHelperText()
                    }
                }
            }
        })
    }

    private fun showErrorHelperText() {
        val defaultErrorText = resources.getString(R.string.input_password_error_text_default)
        binding.pwdLayout.error = if (passwordErrorText.isNotEmpty()) passwordErrorText else defaultErrorText
        binding.pwdLayout.isErrorEnabled = true
        binding.pwdLayout.helperText = emptyString
    }
}