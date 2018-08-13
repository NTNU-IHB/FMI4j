package no.mechatronics.sfi.fmi4j.jni;

public class FmiCallbackFunctions {


    LoggerCallback logger = new LoggerCallbackImpl();


    interface AllocateMemoryCallback {
        void invoke(int nobj, int size);
    }

    interface FreeMemoryCallback {
        void invoke(long c);
    }

    interface LoggerCallback {
        void invoke(long c, String instanceName, int status, String category, String message, long... args);
    }

    interface StepFinishedCallback {
        void invoke(long c, int status);
    }

    class LoggerCallbackImpl implements LoggerCallback {
        @Override
        public void invoke(long c, String instanceName, int status, String category, String message, long... args) {

        }
    }


}
