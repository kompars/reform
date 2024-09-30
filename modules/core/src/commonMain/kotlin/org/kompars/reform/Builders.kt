package org.kompars.reform

import org.kompars.reform.impl.*

public fun Form(
    //rawValues: Map<String, List<String?>>,
    name: String? = null,
    id: String? = null,
): Form {
    return FormImpl(name, id)
}

public fun buildForm(builder: FormBuilder.() -> Unit): Map<String, List<String?>> {
    return FormBuilder().apply(builder).build()
}

public class FormBuilder {
    private val rawValues = mutableMapOf<String, MutableList<String?>>()

    public fun append(name: String, value: String?): FormBuilder {
        rawValues.getOrPut(name) { mutableListOf() } += value
        return this
    }

    public fun append(name: String, values: List<String?>): FormBuilder {
        rawValues.getOrPut(name) { mutableListOf() }.addAll(values)
        return this
    }

    public fun build(): Map<String, List<String?>> {
        return rawValues
    }
}
