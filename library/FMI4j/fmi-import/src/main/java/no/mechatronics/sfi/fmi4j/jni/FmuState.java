package no.mechatronics.sfi.fmi4j.jni;

public class FmuState {

    public long pointer;

    private FmuState() {

    }

    static FmuState allocate() {
        return new FmuState();
    }

}
