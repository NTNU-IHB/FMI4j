# ControlledTemperature

Simple temperature control model (thermostate). A thermistor gives out heat when the switch is closed. This heat is stored in a heat capacity (the room itself), and a constant temperature is added (293.15K, or room temperature of 20 degrees Celsius). The on-off controller turns off when it is on and the current state goes above the maximum value (reference + bandwidth / 2). It turns on when it is below the minimum value (reference - bandwidth / 2).

Contact: 	info@controllab.nl
