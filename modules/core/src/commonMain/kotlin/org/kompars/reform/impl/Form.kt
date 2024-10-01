//package org.kompars.reform.impl
//
//import org.kompars.reform.*
//import org.kompars.reform.validation.*
//
//internal class FormImpl(
//    //override val rawValues: Map<String, List<String?>>,
//    override val name: String?,
//    override val id: String?,
//) : Form {
//    private val providers = mutableMapOf<String, suspend (List<String?>) -> FieldResult>()
//    private val validators = mutableMapOf<String?, MutableList<suspend () -> ValidationError?>>()
//
//    private val results = mutableMapOf<String, FieldResult>()
//
//    private val _errors = mutableMapOf<String?, MutableList<ValidationError>>()
//    private val _values = mutableMapOf<String, Any?>()
//
//    private var isProcessing = false
//    private var isProcessed = false
//
//    override val values: Map<String, *>
//        get() {
//            ensureProcessed()
//            return _values
//        }
//
//    override val errors: Map<String?, List<ValidationError>>
//        get() {
//            ensureProcessed()
//            return _errors
//        }
//
//    override val isValid: Boolean
//        get() = errors.isEmpty()
//
//    override fun <T : Any> createField(trim: Boolean, block: suspend (String) -> T?): RawField<T> {
//        ensureNotProcessed()
//
//        return RawFieldImpl(this) { value ->
//            when (value) {
//                null -> null
//                else -> value.let { if (trim) it.trim() else it }.ifEmpty { null }?.let { block(it) }
//            }
//        }
//    }
//
//    override fun addValidator(name: String?, block: suspend () -> ValidationError?) {
//        ensureNotProcessed()
//        validators.getOrPut(name) { mutableListOf() } += block
//    }
//
//    override fun addError(name: String?, error: ValidationError) {
//        ensureProcessed()
//        _errors.getOrPut(name) { mutableListOf() } += error
//    }
//
//    internal fun <T> register(name: String, provider: suspend (List<String?>) -> FieldResult): Lazy<T> {
//        ensureNotProcessed()
//
//        if (providers.containsKey(name)) {
//            throw DuplicateFormFieldException(this, name)
//        }
//
//        providers[name] = provider
//
//        return lazy {
//            getValue(name)
//        }
//    }
//
//    private fun <T> getValue(name: String): T {
//        ensureProcessed()
//
//        if (!isProcessing && !isValid) {
//            throw InvalidFormException(this)
//        }
//
//        @Suppress("UNCHECKED_CAST")
//        return when (val result = results[name]) {
//            is FieldResult.Value<*> -> result.value as T
//            is FieldResult.Errors -> throw InvalidFormFieldException(this, name)
//            null -> throw UnprocessedFormFieldException(this, name)
//        }
//    }
//
//    override suspend fun process(rawValues: Map<String, List<String?>>) {
//        if (isProcessed || isProcessing) {
//            return
//        }
//
//        isProcessing = true
//
//        for ((name, provider) in providers) {
//            results[name] = provider(rawValues[name].orEmpty())
//        }
//
//        _values += results
//            .filter { it.value is FieldResult.Value<*> }
//            .mapValues { (it.value as FieldResult.Value<*>).value }
//
//        _errors += results
//            .filter { it.value is FieldResult.Errors }
//            .mapValues { (it.value as FieldResult.Errors).errors.toMutableList() }
//
//        for ((name, validatorList) in validators) {
//            for (validator in validatorList) {
//                val error = validate(Unit) { validator() }
//                if (error != null) {
//                    _errors.getOrPut(name) { mutableListOf() } += error
//                }
//            }
//        }
//
//        isProcessing = false
//        isProcessed = true
//    }
//
//    private fun ensureProcessed() {
//        if (!isProcessed) {
//            throw IllegalStateException("Form not processed")
//        }
//    }
//
//    private fun ensureNotProcessed() {
//        if (isProcessed || isProcessing) {
//            throw ProcessedFormException(this)
//        }
//    }
//}
