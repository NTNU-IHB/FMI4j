#ifndef fmiFunctionTypes_h
#define fmiFunctionTypes_h

#include "fmiPlatformTypes.h"

#ifdef __cplusplus
extern "C" {
#endif

/* make sure all compiler use the same alignment policies for structures */
#if defined _MSC_VER || defined __GNUC__
#pragma pack(push,8)
#endif

/* Include stddef.h, in order that size_t etc. is defined */
#include <stddef.h>


/* Type definitions */
typedef enum {
    fmiOK,
    fmiWarning,
    fmiDiscard,
    fmiError,
    fmiFatal,
    fmiPending
} fmiStatus;

typedef enum {
    fmiDoStepStatus,
    fmiPendingStatus,
    fmiLastSuccessfulTime
} fmiStatusKind;

typedef void      (*fmiCallbackLogger)        (fmiComponent, fmiString, fmiStatus, fmiString, fmiString, ...);
typedef void*     (*fmiCallbackAllocateMemory)(size_t, size_t);
typedef void      (*fmiCallbackFreeMemory)    (void*);
typedef void      (*fmiStepFinished)          (fmiComponent, fmiStatus);

typedef struct {
   const fmiCallbackLogger         logger;
   const fmiCallbackAllocateMemory allocateMemory;
   const fmiCallbackFreeMemory     freeMemory;
} fmiMeCallbackFunctions;

typedef struct {
   const fmiCallbackLogger         logger;
   const fmiCallbackAllocateMemory allocateMemory;
   const fmiCallbackFreeMemory     freeMemory;
   const fmiStepFinished           stepFinished;
} fmiCsCallbackFunctions;

typedef struct {
    fmiBoolean iterationConverged;
    fmiBoolean stateValueReferencesChanged;
    fmiBoolean stateValuesChanged;
    fmiBoolean terminateSimulation;
    fmiBoolean upcomingTimeEvent;
    fmiReal    nextEventTime;
} fmiEventInfo;

/* reset alignment policy to the one set before reading this file */
#if defined _MSC_VER || defined __GNUC__
#pragma pack(pop)
#endif


/* Define fmi function pointer types to simplify dynamic loading */

/***************************************************
Types for Common Functions
****************************************************/

/* Inquire version numbers of header files and setting logging status */
   typedef const char* fmiGetTypesPlatformTYPE(void);
   typedef const char* fmiGetVersionTYPE(void);
   typedef fmiStatus  fmiSetDebugLoggingTYPE(fmiComponent, fmiBoolean);

/* Getting and setting variable values */
   typedef fmiStatus fmiGetRealTYPE   (fmiComponent, const fmiValueReference[], size_t, fmiReal   []);
   typedef fmiStatus fmiGetIntegerTYPE(fmiComponent, const fmiValueReference[], size_t, fmiInteger[]);
   typedef fmiStatus fmiGetBooleanTYPE(fmiComponent, const fmiValueReference[], size_t, fmiBoolean[]);
   typedef fmiStatus fmiGetStringTYPE (fmiComponent, const fmiValueReference[], size_t, fmiString []);

   typedef fmiStatus fmiSetRealTYPE   (fmiComponent, const fmiValueReference[], size_t, const fmiReal   []);
   typedef fmiStatus fmiSetIntegerTYPE(fmiComponent, const fmiValueReference[], size_t, const fmiInteger[]);
   typedef fmiStatus fmiSetBooleanTYPE(fmiComponent, const fmiValueReference[], size_t, const fmiBoolean[]);
   typedef fmiStatus fmiSetStringTYPE (fmiComponent, const fmiValueReference[], size_t, const fmiString []);


/***************************************************
Types for Functions for FMI for Co-Simulation
****************************************************/

    typedef fmiComponent fmiInstantiateSlaveTYPE (fmiString, fmiString, fmiString, fmiString, fmiReal, fmiBoolean, fmiBoolean, fmiCsCallbackFunctions, fmiBoolean);
    typedef fmiStatus fmiInitializeSlaveTYPE       (fmiComponent, fmiReal, fmiBoolean, fmiReal);

    typedef fmiStatus fmiSetRealInputDerivativesTYPE (fmiComponent, const fmiValueReference [], size_t, const fmiInteger [], const fmiReal []);
    typedef fmiStatus fmiGetRealOutputDerivativesTYPE(fmiComponent, const fmiValueReference [], size_t, const fmiInteger [], fmiReal []);

    typedef fmiStatus fmiDoStepTYPE     (fmiComponent, fmiReal, fmiReal, fmiBoolean);

    typedef fmiStatus fmiResetSlaveTYPE                  (fmiComponent);
    typedef fmiStatus fmiTerminateSlaveTYPE              (fmiComponent);

    typedef void fmiFreeSlaveInstanceTYPE(fmiComponent);


/***************************************************
Types for Functions for FMI for Model Exchange
****************************************************/

    typedef fmiComponent fmiInstantiateModelTYPE  (fmiString instanceName, fmiString GUID, fmiMeCallbackFunctions functions, fmiBoolean loggingOn);
    typedef fmiStatus fmiInitializeTYPE (fmiComponent c, fmiBoolean toleranceControlled, fmiReal relativeTolerance, fmiEventInfo* eventInfo);

    typedef fmiStatus fmiGetDerivativesTYPE     (fmiComponent c, fmiReal derivatives[]    , size_t nx);
    typedef fmiStatus fmiGetEventIndicatorsTYPE (fmiComponent c, fmiReal eventIndicators[], size_t ni);

    typedef fmiStatus fmiSetTimeTYPE                (fmiComponent c, fmiReal time);
    typedef fmiStatus fmiSetContinuousStatesTYPE    (fmiComponent c, const fmiReal x[], size_t nx);
    typedef fmiStatus fmiCompletedIntegratorStepTYPE(fmiComponent c, fmiBoolean* callEventUpdate);

    typedef fmiStatus fmiEventUpdateTYPE                (fmiComponent c, fmiBoolean intermediateResults, fmiEventInfo* eventInfo);
    typedef fmiStatus fmiGetContinuousStatesTYPE        (fmiComponent c, fmiReal states[], size_t nx);
    typedef fmiStatus fmiGetNominalContinuousStatesTYPE (fmiComponent c, fmiReal x_nominal[], size_t nx);
    typedef fmiStatus fmiGetStateValueReferencesTYPE   (fmiComponent c, fmiValueReference vrx[], size_t nx);

    typedef fmiStatus fmiTerminateTYPE                  (fmiComponent c);

    typedef void fmiFreeModelInstanceTYPE (fmiComponent c);


#ifdef __cplusplus
}  /* end of extern "C" { */
#endif

#endif /* fmiFunctionTypes_h */
