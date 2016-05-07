from matplotlib.font_manager import FontProperties
import matplotlib.pyplot as plt
from drawnow import *
from pylab import *
import serial
import sys
import numpy as np
import os

def handle_close(evt):
	os._exit(0)

def plotter():
	plt.plot(temp, label='Signal')
	axes = plt.gca()
	axes.set_ylim([0, 1200])
	t = gcf().text(0.5, 0.95,
							figtitle,
							horizontalalignment = 'center',
							fontproperties = FontProperties(size=25))
	plt.legend()
	plt.grid()

if not len(sys.argv) == 2:
	print "Usage python %s PORT" %sys.argv[0];
	sys.exit(0);
port = sys.argv[1]
ser = serial.Serial(port, 115200)
temp = []
fig = plt.figure() 
fig.canvas.set_window_title('Oscilloscope')
fig.canvas.mpl_connect('close_event', handle_close)
figtitle = 'Oscilloscope'
plt.ion()

counter = 0
while True:
	try:
		while ser.inWaiting == 0:
			print "nothing"
			pass
		read = ser.readline().rstrip()
		print "Reading: %s" %read

		if not not read:
			var = int(read)
			temp.append(var)
			plt.pause(0.0001)
			drawnow(plotter)
			counter += 1
			if counter > 30:
				temp.pop(0)
	except ValueError:
		pass
	except KeyboardInterrupt:
		print '\nClosing...'
		sys.exit(0)
