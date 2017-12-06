package no.mechatronics.sfi.fmi4j.misc

import com.sun.jna.Pointer
import com.sun.jna.ptr.PointerByReference

class FmuState {

    val pointer: Pointer = Pointer.NULL
    val pointerByReference: PointerByReference by lazy {
        PointerByReference(pointer)
    }

}