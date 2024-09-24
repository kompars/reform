package org.kompars.reform

import org.kompars.reform.impl.*

public fun Form(
    rawValues: Map<String, List<String?>>,
    name: String? = null,
    id: String? = null,
): Form {
    return FormImpl(rawValues, name, id)
}

public fun buildForm(name: String? = null, id: String? = null, builder: FormBuilder.() -> Unit): Form {
    return FormBuilder().apply(builder).build(name, id)
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

    public fun build(name: String? = null, id: String? = null): Form {
        return Form(rawValues, name, id)
    }
}
