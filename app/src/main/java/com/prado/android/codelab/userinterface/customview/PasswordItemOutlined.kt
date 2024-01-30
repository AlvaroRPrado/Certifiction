package com.prado.android.codelab.userinterface.customview

import android.content.Context
import android.util.AttributeSet
import com.prado.android.R

class PasswordItemOutlined @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = R.style.CustomComponents_TextInputLayout
) : Password(context, attrs, defStyleAttr, defStyleRes, R.layout.item_password_outlined)