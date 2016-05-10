import matplotlib.pyplot as plt
import numpy as np
import pylab as pl
import sys

plot_label = "label"
temp = []
y = []
#plt.xlabel('Time', fontsize=20)
#plt.ylabel('Voltage', fontsize=20)
#axes = plt.gca()
#axes.set_ylim([0, max(temp) + 1])



if len(sys.argv) < 2:
	print "Usage python %s PORT" %sys.argv[0]
	sys.exit(0)

y.append(0)
for i in range(1, len(sys.argv)):
	y.append(i)
	temp.append(int(sys.argv[i]))
y.pop(len(sys.argv) - 2)
fig = plt.figure() 
fig.canvas.set_window_title("Disk Scheduling")
plt.plot(temp, label="SSTF", marker='o', markersize=11, linewidth=3)
plt.grid(b=True, which='major', color='black', linestyle='-')
plt.grid(b=True, which='minor', color='gray', linestyle='-')
plt.minorticks_on()
plt.legend()
plt.show()
