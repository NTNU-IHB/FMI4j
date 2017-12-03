/*
 * The MIT License
 *
 * Copyright 2017. Norwegian University of Technology
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING  FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package no.mechatronics.sfi.fmi4j.jna


import com.sun.jna.Library
import com.sun.jna.Pointer
import com.sun.jna.ptr.*
import no.mechatronics.sfi.fmi4j.jna.structs.Fmi2CallbackFunctions


/**
 *
 * @author Lars Ivar Hatledal laht@ntnu.no.
 */
interface Fmi2Library : Library {

    /**
     * Returns the string to uniquely identify the “fmi2TypesPlatform.h” header file used for
     * compilation of the functions of the FMU. The function returns a pointer to a static string specified
     * by “fmi2TypesPlatform” defined in this header file. The standard header file, as documented in
     * this specification, has fmi2TypesPlatform set to “default” (so this function usually returns
     * “default”).
     */
    fun fmi2GetTypesPlatform(): String

    /**
     * Returns the version of the “fmi2Functions.h” header file which was used to compile the
     * functions of the FMU. The function returns “fmiVersion” which is defined in this header file. The
     * standard header file as documented in this specification has version “2.0” (so this function
     * usually returns “2.0”).
     */
    fun fmi2GetVersion(): String

    /**
     * If loggingOn=fmi2True, debug logging is enabled, otherwise it is switched off.
     * If loggingOn=fmi2True and nCategories > 0, then only debug messages according to
     * the categories argument shall be printed via the logger function. Vector categories has
     * nCategories elements. The allowed values of “category” are defined by the modeling
     * environment that generated the FMU. Depending on the generating modeling environment,
     * none, some or all allowed values for “categories” for this FMU are defined in the
     * modelDescription.xml file via element “fmiModelDescription.LogCategories”, see
     * section 2.2.4.
     */
    fun fmi2SetDebugLogging(c: Pointer, loggingOn: Byte, nCategories: Int, categories: Array<String>): Int

    /**
     * Informs the FMU to setup the experiment. This function can be called after
     * fmi2Instantiate and before fmi2EnterInitializationMode is called. Arguments
     * toleranceDefined and tolerance depend on the FMU type:
     */
    fun fmi2SetupExperiment(c: Pointer, toleranceDefined: Byte, tolerance: Double, startTime: Double, stopTimeDefined: Byte, stopTime: Double): Int

    /**
     * Informs the FMU to enter Initialization Mode. Before calling this function, all variables with
     * attribute <ScalarVariable initial = "exact" or "approx"> can be set with the
     * “fmi2SetXXX” functions (the ScalarVariable attributes are defined in the Model
     * Description File, see section 2.2.7). Setting other variables is not allowed. Furthermore,
     * fmi2SetupExperiment must be called at least once before calling
     * fmi2EnterInitializationMode, in order that startTime is defined.
     */
    fun fmi2EnterInitializationMode(c: Pointer): Int

    /**
     * Informs the FMU to exit Initialization Mode.
     * For fmuType = fmi2ModelExchange, this function switches off all initialization equations
     * and the FMU enters implicitely Event Mode, that is all continuous-time and active discretetime
     * equations are available.
     */
    fun fmi2ExitInitializationMode(c: Pointer): Int

    fun fmi2Instantiate(instanceName: String, type: Int, guid: String, resourceLocation: String, functions: Fmi2CallbackFunctions, visible: Byte, loggingOn: Byte): Pointer

    /**
     * Informs the FMU that the simulation run is terminated. After calling this function, the final
     * values of all variables can be inquired with the fmi2GetXXX(..) functions. It is not allowed
     * to call this function after one of the functions returned with a status flag of fmi2Error or
     * fmi2Fatal
     */
    fun fmi2Terminate(c: Pointer): Int

    /**
     * Is called by the environment to reset the FMU after a simulation run. The FMU goes into the
     * same state as if fmi2Instantiate would have been called. All variables have their default
     * values. Before starting a new run, fmi2SetupExperiment and
     * fmi2EnterInitializationMode have to be called.
     */
    fun fmi2Reset(c: Pointer): Int

    /**
     * Disposes the given instance, unloads the loaded model, and frees all the allocated memory
     * and other resources that have been allocated by the functions of the FMU interface. If a null
     * pointer is provided for “c”, the function call is ignored (does not have an effect).
     */
    fun fmi2FreeInstance(c: Pointer)

    fun fmi2GetInteger(c: Pointer, vr: IntArray, nvr: Int, value: IntArray): Int

    fun fmi2GetReal(c: Pointer, vr: IntArray, nvr: Int, value: DoubleArray): Int

    fun fmi2GetString(c: Pointer, vr: IntArray, nvr: Int, value: Array<String>): Int

    fun fmi2GetBoolean(c: Pointer,  vr: IntArray, nvr: Int, value: ByteArray): Int

    /**
     * Set parameters, inputs, start values and re-initialize caching of variables that depend on these
     * variables (see section 2.2.7 for the exact rules on which type of variables fmi2SetXXX can be
     * called, as well as section 3.2.3 in case of ModelExchange and section 4.2.4 in case of
     * CoSimulation).
     * @param vr a vector of “nvr” value handles that define the variables that shall be set
     * @param value  a vector with the actual values of these variables
     */
    fun fmi2SetInteger(c: Pointer, vr: IntArray, nvr: Int, value: IntArray): Int

    /**
     * Set parameters, inputs, start values and re-initialize caching of variables that depend on these
     * variables (see section 2.2.7 for the exact rules on which type of variables fmi2SetXXX can be
     * called, as well as section 3.2.3 in case of ModelExchange and section 4.2.4 in case of
     * CoSimulation).
     * @param vr a vector of “nvr” value handles that define the variables that shall be set
     * @param value  a vector with the actual values of these variables
     */
    fun fmi2SetReal(c: Pointer, vr: IntArray, nvr: Int, value: DoubleArray): Int

    /**
     * Set parameters, inputs, start values and re-initialize caching of variables that depend on these
     * variables (see section 2.2.7 for the exact rules on which type of variables fmi2SetXXX can be
     * called, as well as section 3.2.3 in case of ModelExchange and section 4.2.4 in case of
     * CoSimulation).
     * @param vr a vector of “nvr” value handles that define the variables that shall be set
     * @param value  a vector with the actual values of these variables
     */
    fun fmi2SetString(c: Pointer, vr: IntArray, nvr: Int, value: Array<out String>): Int

    /**
     * Set parameters, inputs, start values and re-initialize caching of variables that depend on these
     * variables (see section 2.2.7 for the exact rules on which type of variables fmi2SetXXX can be
     * called, as well as section 3.2.3 in case of ModelExchange and section 4.2.4 in case of
     * CoSimulation).
     * @param vr a vector of “nvr” value handles that define the variables that shall be set
     * @param value  a vector with the actual values of these variables
     */
    fun fmi2SetBoolean(c: Pointer,  vr: IntArray, nvr: Int, value: ByteArray): Int


    fun fmi2GetDirectionalDerivative(c: Pointer, vUnknown_ref: IntArray, nUnknown: Int, vKnown_ref: IntArray, nKnown: Int, dvKnown: DoubleArray, dvUnknown: DoubleArray): Int

    /**
     * fmi2GetFMUstate makes a copy of the internal FMU state and returns a pointer to this copy
     * (FMUstate). If on entry *FMUstate == NULL, a new allocation is required. If *FMUstate !=
     * NULL, then *FMUstate points to a previously returned FMUstate that has not been modified
     * since. In particular, fmi2FreeFMUstate had not been called with this FMUstate as an argument.
     * [Function fmi2GetFMUstate typically reuses the memory of this FMUstate in this case and
     * returns the same pointer to it, but with the actual FMUstate.]
     * <br>
     * Note: This function is only supported by the FMU, if the optional capability flag
     * <fmiModelDescription> <ModelExchange / CoSimulation canGetAndSetFMUstate in =
     * "true"> in the XML file is explicitly set to true (see sections 3.3.1 and 4.3.1).
     */
    fun fmi2GetFMUstate(c: Pointer, state: PointerByReference): Int

    /**
     * fmi2SetFMUstate copies the content of the previously copied FMUstate back and uses it as
     * actual new FMU state. The FMUstate copy does still exist
     * <br>
     * Note: This function is only supported by the FMU, if the optional capability flag
     * <fmiModelDescription> <ModelExchange / CoSimulation canGetAndSetFMUstate in =
     * "true"> in the XML file is explicitly set to true (see sections 3.3.1 and 4.3.1).
     */
    fun fmi2SetFMUstate(c: Pointer, state: Pointer): Int

    /**
     * fmi2FreeFMUstate frees all memory and other resources allocated with the fmi2GetFMUstate
     * call for this FMUstate. The input argument to this function is the FMUstate to be freed. If a null
     * pointer is provided, the call is ignored. The function returns a null pointer in argument FMUstate
     * <br>
     * Note: This function is only supported by the FMU, if the optional capability flag
     * <fmiModelDescription> <ModelExchange / CoSimulation canGetAndSetFMUstate in =
     * "true"> in the XML file is explicitly set to true (see sections 3.3.1 and 4.3.1).
     */
    fun fmi2FreeFMUstate(c: Pointer, state: PointerByReference): Int

    /**
     * fmi2SerializedFMUstateSize returns the size of the byte vector, in order that FMUstate can
     * be stored in it. With this information, the environment has to allocate an fmi2Byte vector of the
     * required length size.
     * <br>
     * Note: This function is only supported by the FMU, if the optional capability flags
     * canGetAndSetFMUstate and canSerializeFMUstate in
     * <fmiModelDescription><ModelExchange / CoSimulation> in the XML file are explicitly set
     * to true (see sections 3.3.1 and 4.3.1).
     */
    fun fmi2SerializedFMUstateSize(c: Pointer, fmuState: Pointer, size: Pointer): Int

    /**
     * fmi2SerializeFMUstate serializes the data which is referenced by pointer FMUstate and
     * copies this data in to the byte vector serializedState of length size, that must be provided by
     * the environment.
     * <br>
     * Note: This function is only supported by the FMU, if the optional capability flags
     * canGetAndSetFMUstate and canSerializeFMUstate in
     * <fmiModelDescription><ModelExchange / CoSimulation> in the XML file are explicitly set
     * to true (see sections 3.3.1 and 4.3.1).
     */
    fun fmi2SerializeFMUstate(c: Pointer, fmuState: Pointer, serializedState: ByteArray, size: Int): Int

    /**
     * fmi2DeSerializeFMUstate deserializes the byte vector serializedState of length size,
     * constructs a copy of the FMU state and returns FMUstate, the pointer to this copy. [The
     * simulation is restarted at this state, when calling fmi2SetFMUState with FMUstate.]
     * <br>
     * Note: This function is only supported by the FMU, if the optional capability flags
     * canGetAndSetFMUstate and canSerializeFMUstate in
     * <fmiModelDescription><ModelExchange / CoSimulation> in the XML file are explicitly set
     * to true (see sections 3.3.1 and 4.3.1).
     */
    fun fmi2DeSerializeFMUstate(c: Pointer, serializedState: ByteArray, size: Int, state: PointerByReference): Int

}




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


