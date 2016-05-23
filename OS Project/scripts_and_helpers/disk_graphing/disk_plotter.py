import sys
import matplotlib.pyplot as plt

plot_label = "label"
x1 = []
x2 = []
y1 = []
y2 = []

if len(sys.argv) < 2:
	print "Usage: python %s elem1 elem2 elem3 elem4 ..." %sys.argv[0]
	print "Usage: python %s elem1 elem2 elem3 elem4 ... s elem1 elem2 elem3 elem4 ..." %sys.argv[0]
	sys.exit(0)

counter = 0
z = 0
for i in range(1, len(sys.argv)):
	z = i
	if not sys.argv[i] == "s":
		x1.append(int(sys.argv[i]))
		y1.append(counter)
		counter += 1
	else:
		counter = 0
		break

for i in range(z+1, len(sys.argv)):
	x2.append(int(sys.argv[i]))
	y2.append(counter)
	counter += 1

fig = plt.figure() 
fig.canvas.set_window_title("Graphical Representation")
plt.gca().invert_yaxis()
plt.gca().xaxis.tick_top()
plt.plot(x1, y1, label="SSTF", marker='o', markeredgewidth=2, markersize=11, linewidth=3)

if len(x2) > 0:
	plt.plot(x2, y2, label="C LOOK", marker='H', markeredgewidth=2, markersize=11, linewidth=3)
	plt.legend(loc='best')
plt.grid(b=True, which='major', color='black', linestyle='-')
plt.grid(b=True, which='minor', color='gray', linestyle='-')
plt.minorticks_on()
plt.show()