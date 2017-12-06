package no.mechatronics.sfi.fmi4j.misc

import com.sun.jna.ptr.ByReference


/**
 * https://stackoverflow.com/questions/29162569/jna-passing-string-by-reference-to-dll-but-non-return
 */
class StringByReference : ByReference {

    var value: String
        get() = pointer.getString(0)
        set(str) = pointer.setString(0, str)

    @JvmOverloads constructor(size: Int = 0) : super(if (size < 4) 4 else size) {
        pointer.clear((if (size < 4) 4 else size).toLong())
    }

    constructor(str: String) : super(if (str.length < 4) 4 else str.length + 1) {
        value = str
    }

}
