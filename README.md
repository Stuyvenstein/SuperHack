SuperHack is a tool for rooted Andrdoid devices allowing the user the edit the 'hosts' file entries, as well as enable ADB over TCP (Network based debugging)

SuperHack is no longer supported but I keep the repository up as a proof of concept for anyone wanting to do something similar.
I stopped working on this repo a long time ago so the last vesion of Android it was intended to work on is Android SDK 18 (Jelly Bean)

Below are the instructions for configuring ADB over TCP:
To get ADB over TCP working, your phone needs to be connected to the same network as your PC running ADB via WiFi or some form of ethernet.
Windows command: "adb connect {Phone IP}:{Port}"
Example: "adb connect 192.168.10.24:5321"
To switch back to USB Debugging, use: "adb usb" while the device is connected to the PC via USB
Also ensure that USB Debugging is enabled on the Android device.
