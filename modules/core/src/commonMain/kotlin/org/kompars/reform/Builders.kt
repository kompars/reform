package org.kompars.reform

//public fun Form(
//    //rawValues: Map<String, List<String?>>,
//    name: String? = null,
//    id: String? = null,
//): Form {
//    return FormImpl(name, id)
//}

public fun buildFormValues(builder: FormValuesBuilder.() -> Unit): Map<String, List<String?>> {
    return FormValuesBuilder().apply(builder).build()
}

public class FormValuesBuilder {
    private val rawValues = mutableMapOf<String, MutableList<String?>>()

    public fun append(name: String, value: String?): FormValuesBuilder {
        rawValues.getOrPut(name) { mutableListOf() } += value
        return this
    }

    public fun append(name: String, values: List<String?>): FormValuesBuilder {
        rawValues.getOrPut(name) { mutableListOf() }.addAll(values)
        return this
    }

    public fun build(): Map<String, List<String?>> {
        return rawValues
    }
}
