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
   const fmiStepFinished           stepFinished;
} fmiCallbackFunctions;


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

/* Creation and destruction of FMU instances and setting debug status */
   typedef fmiComponent fmiInstantiateSlaveTYPE (fmiString, fmiString, fmiString, fmiString, fmiReal, fmiBoolean, fmiBoolean, fmiCallbackFunctions, fmiBoolean);
   typedef void          fmiFreeSlaveInstanceTYPE(fmiComponent);

/* Enter and exit initialization mode, terminate and reset */
   typedef fmiStatus fmiInitializeSlaveTYPE       (fmiComponent, fmiReal, fmiBoolean, fmiReal);
   typedef fmiStatus fmiTerminateSlaveTYPE              (fmiComponent);
   typedef fmiStatus fmiResetSlaveTYPE                  (fmiComponent);

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
Types for Functions for FMI2 for Co-Simulation
****************************************************/

/* Simulating the slave */
   typedef fmiStatus fmiSetRealInputDerivativesTYPE (fmiComponent, const fmiValueReference [], size_t, const fmiInteger [], const fmiReal []);
   typedef fmiStatus fmiGetRealOutputDerivativesTYPE(fmiComponent, const fmiValueReference [], size_t, const fmiInteger [], fmiReal []);

   typedef fmiStatus fmiDoStepTYPE     (fmiComponent, fmiReal, fmiReal, fmiBoolean);
   typedef fmiStatus fmiCancelStepTYPE (fmiComponent);

#ifdef __cplusplus
}  /* end of extern "C" { */
#endif

#endif /* fmiFunctionTypes_h */
