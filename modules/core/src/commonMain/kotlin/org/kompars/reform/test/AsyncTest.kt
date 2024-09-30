package org.kompars.reform.test

import kotlin.getValue
import org.kompars.reform.Form
import org.kompars.reform.InvalidFormException
import org.kompars.reform.buildForm
import org.kompars.reform.provideDelegate
import org.kompars.reform.text
import org.kompars.reform.validation.strongPassword

public suspend fun main() {
    val form = Form()

    val password by form.text()
        .required()
        .strongPassword()

    val values = buildForm {
        append("password", "ABC")
    }

    form.process(values)

    try {
        println(password)
    } catch (e: InvalidFormException) {
        e.form.errors["password"]?.forEach { println(it) }
    }

}
