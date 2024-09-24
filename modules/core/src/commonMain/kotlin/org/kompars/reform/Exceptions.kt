package org.kompars.reform

public sealed class FormException(public val form: Form, message: String, cause: Throwable? = null) :
    Exception(message, cause)

public sealed class FieldException(form: Form, public val field: String, message: String, cause: Throwable? = null) :
    FormException(form, message, cause)

public class MissingValueException :
    Exception("Missing value")

public class ConversionFailedException(public val error: String, cause: Throwable? = null) :
    Exception("Conversion exception: $error", cause)

public class ProcessedFormException(form: Form) :
    FormException(form, "Form is already processed.")

public class InvalidFormException(form: Form, cause: Throwable? = null) :
    FormException(form, "Form is invalid.", cause)

public class DuplicateFormFieldException(form: Form, field: String) :
    FieldException(form, field, "Field '$field' is already registered.")

public class UnprocessedFormFieldException(form: Form, field: String) :
    FieldException(form, field, "Field '$field' is not processed yet.")

public class InvalidFormFieldException(form: Form, field: String) :
    FieldException(form, field, "Field '$field' is invalid.")
