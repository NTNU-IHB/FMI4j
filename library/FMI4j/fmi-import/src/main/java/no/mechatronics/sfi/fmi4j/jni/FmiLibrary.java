/*
 * The MIT License
 *
 * Copyright 2017-2018 Norwegian University of Technology
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

package no.mechatronics.sfi.fmi4j.jni;

import no.mechatronics.sfi.fmi4j.common.FmiStatus;
import no.mechatronics.sfi.fmi4j.importer.misc.OSUtil;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class FmiLibrary implements Closeable {

    static {

        String fileName = OSUtil.getLibPrefix() + "fmi." + OSUtil.getLibExtension();
        File copy = new File(fileName);
        try (InputStream is = FmiLibrary.class.getClassLoader()
                .getResourceAsStream("native/fmi/" + OSUtil.getCurrentOS() + "/" + fileName)) {
            try (FileOutputStream fos = new FileOutputStream(copy)) {
                byte[] buffer = new byte[1024];
                int bytes = is.read(buffer);
                while (bytes >= 0) {
                    fos.write(buffer, 0, bytes);
                    bytes = is.read(buffer);
                }
            }
            System.load(copy.getAbsolutePath());

        } catch (Exception ex) {
            copy.delete();
            throw new RuntimeException(ex);
        }
    }

    public FmiLibrary(String libName) {
        if (!load(libName)) {
            throw new RuntimeException("Unable to load native library!");
        }
    }

    private native boolean load(String libName);

    public native void close();

    public native String getVersion();

    public native String getTypesPlatform();

    public native FmiStatus setDebugLogging(
            long c, boolean loggingOn, int nCategories, String[] categories);

    public native FmiStatus setupExperiment(
            long c, boolean toleranceDefined,
            double tolerance, double startTime, double stopTime);

    public native FmiStatus enterInitializationMode(long c);

    public native FmiStatus exitInitializationMode(long c);

    public native long instantiate(
            String instanceName, int type, String guid,
            String resourceLocation, boolean visible, boolean loggingOn);

    public native FmiStatus terminate(long c);

    public native FmiStatus reset(long c);

    public native void freeInstance(long c);

    //read
    public native FmiStatus getInteger(long c, int vr[], int[] ref);

    public native FmiStatus getReal(long c, int[] vr, double[] ref);

    public native FmiStatus getString(long c, int[] vr, String[] ref);

    public native FmiStatus getBoolean(long c, int[] vr, boolean[] ref);

    //write
    public native FmiStatus setInteger(long c, int vr[], int[] values);

    public native FmiStatus setReal(long c, int[] vr, double[] values);

    public native FmiStatus setString(long c, int[] vr, String[] values);

    public native FmiStatus setBoolean(long c, int[] vr, boolean[] values);

    public native FmiStatus getDirectionalDerivative(
            long c, int[] vUnknown_ref,
            int[] vKnownRef, double[] dvKnown, double[] dvUnknown);


    public native long getFMUstate(long c, PointerByReference state);

    public native FmiStatus setFMUstate(long c, long state);


    /***************************************************
     Functions for FMI2 for Co-simulation
     ****************************************************/

    public native FmiStatus step(
            long c, double currentCommunicationPoint,
            double communicationStepSize, boolean noSetFMUStatePriorToCurrentPoint);

    public native FmiStatus cancelStep(long c);

    public native FmiStatus setRealInputDerivatives(long c, int[] vr, int[] order, double[] value);

    public native FmiStatus getRealOutputDerivatives(long c, int[] vr, int[] order, double[] value);

    public native FmiStatus getStatus(long c, int s, IntByReference value);

    public native FmiStatus getRealStatus(long c, int s, DoubleByReference value);

    public native FmiStatus getIntegerStatus(long c, int s, IntByReference value);

    public native FmiStatus getStringStatus(long c, int s, StringByReference value);

    public native FmiStatus getBooleanStatus(long c, int s, BooleanByReference value);

    public native FmiStatus getMaxStepSize(long c, DoubleByReference stepSize);

    /***************************************************
     Functions for FMI2 for Model Exchange
     ****************************************************/

    public native FmiStatus enterEventMode(long c);

    public native FmiStatus newDiscreteStates(long c, EventInfo ev);

    public native FmiStatus enterContinuousTimeMode(long c);

    public native FmiStatus setContinuousStates(long c, double[] x);

    public native FmiStatus completedIntegratorStep(long c, boolean noSetFMUStatePriorToCurrentPoint,
                                              BooleanByReference enterEventMode, BooleanByReference terminateSimulation);

    public native FmiStatus setTime(long c, double time);

    public native FmiStatus getDerivatives(long c, double[] derivatives);

    public native FmiStatus getEventIndicators(long c, double[] eventIndicators);

    public native FmiStatus getContinuousStates(long c, double[] x);

    public native FmiStatus getNominalsOfContinuousStates(long c, double[] x_nominals);

}
