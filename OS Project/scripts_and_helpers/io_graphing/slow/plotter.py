import os
import sys
import psutil
import serial
from drawnow import *
import matplotlib.pyplot as plt
from matplotlib.font_manager import FontProperties

voltage = 0
plot_label = "label"
pid = os.getpid()

def handle_close(evt):
	for proc in psutil.process_iter():
	    # check whether the process pid matches
	    if proc.pid == pid:
	        proc.kill()

def map_to(value, leftMin, leftMax, rightMin, rightMax):
    # Figure out how 'wide' each range is
    leftSpan = leftMax - leftMin
    rightSpan = rightMax - rightMin

    # Convert the left range into a 0-1 range (float)
    valueScaled = float(value - leftMin) / float(leftSpan)

    # Convert the 0-1 range into a value in the right range.
    return rightMin + (valueScaled * rightSpan)

def plotter():
	plot_label = "Voltage: " + str(round(voltage, 3)) + " volts"
	plt.plot(temp, label=plot_label, linewidth=5)
	plt.xlabel('Time', fontsize=20)
	plt.ylabel('Voltage', fontsize=20)
	axes = plt.gca()
	axes.set_ylim([0, max(temp) + 1])
	
	# set figure title
	# t = gcf().text(0.5, 0.95,
	# 						figtitle,
	# 						horizontalalignment = 'center',
	# 						fontproperties = FontProperties(size=25))

	plt.legend()
	plt.grid()

if not len(sys.argv) == 2:
	print "Usage: python %s PORT" %sys.argv[0];
	sys.exit(0);
port = sys.argv[1]
ser = serial.Serial(port, 115200)
temp = []
fig = plt.figure() 
fig.canvas.set_window_title("Oscilloscope")
fig.canvas.mpl_connect("close_event", handle_close)
# figtitle = "Voltage Monitor"
# plt.ion()

counter = 0
while True:
	try:
		read = ser.readline().rstrip()

		if not not read:
			voltage = map_to(int(read), 0, 1023, 0.0, 5.0)
			temp.append(voltage)
			drawnow(plotter)
			
			counter += 1
			if counter > 30:
				temp.pop(0)
	except ValueError:
		pass
	except KeyboardInterrupt:
		print '\nClosing...'
		sys.exit(0)
