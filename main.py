from rd03d import RD03D
import time

radar = RD03D()  # Defaults, multi-target mode and pins GP0/GP1

while True:
    if radar.update():
        target1 = radar.get_target(1)
        target2 = radar.get_target(2)
        target3 = radar.get_target(3)
        print(f"{target1.angle},{target1.distance},{target1.speed},{target2.angle},{target2.distance},{target2.speed},{target3.angle},{target3.distance},{target3.speed}")
