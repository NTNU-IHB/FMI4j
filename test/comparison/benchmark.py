import datetime
import shutil

from fmpy import read_model_description, extract
from fmpy.fmi2 import FMU2Slave


class TestOptions:
    def __init__(self, fmu_filename, step_size, stop_time, vr):
        self.fmu_filename = fmu_filename
        self.step_size = step_size
        self.stop_time = stop_time
        self.vr = vr


# options = TestOptions("C:\\Users\laht\\IdeaProjects\\FMI4j\\test\\fmi2\\cs\\win64\\FMUSDK\\2.0.4\\BouncingBall\\bouncingBall.fmu", 1E-3, 100, 0)
# options = TestOptions("C:\\Users\\laht\\Local Documents\\Vico\\Extra\\FMUs\\20161108\\HydraulicCylinderComplex.fmu", 1E-4, 20, 155)
options = TestOptions("C:\\Users\\laht\\IdeaProjects\\FMI4j\\test\\fmi2\\cs\\win64\\20sim\\4.6.4.8004\\TorsionBar\\TorsionBar.fmu", 1E-5, 12, 2)


def main():

    model_description = read_model_description(options.fmu_filename)

    unzipdir = extract(options.fmu_filename)

    fmu = FMU2Slave(guid=model_description.guid,
                    unzipDirectory=unzipdir,
                    modelIdentifier=model_description.coSimulation.modelIdentifier,
                    instanceName='instance1')

    # initialize
    fmu.instantiate()
    fmu.setupExperiment(tolerance=1E-4, startTime=0.0, stopTime=options.stop_time)
    fmu.enterInitializationMode()
    fmu.exitInitializationMode()

    start = datetime.datetime.now()

    t = 0.0
    sum = 0.0
    i = 0
    # simulation loop
    while t < options.stop_time-options.step_size:

        i += 1
        # perform one step
        fmu.doStep(currentCommunicationPoint=t, communicationStepSize=options.step_size)

        sum += fmu.getReal([options.vr])[0]
        #print("h={}".format(h))

        # advance the time
        t += options.step_size

    end = datetime.datetime.now()
    print("sum={}, iter={}".format(sum, i))

    delta = end - start
    print("{}ms".format(int(delta.total_seconds() * 1000)))

    try:
        fmu.terminate()
    except OSError:
        pass
    fmu.freeInstance()

    # clean up
    shutil.rmtree(unzipdir)


if __name__ == '__main__':
    main()