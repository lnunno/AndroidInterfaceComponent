# MotionListView
Interface component for CS 591: Holistic Mobile App Development at the University of New Mexico.

This is an extension of the Android ListView that uses the motion sensors in the 
device to allow for scrolling and shuffling of a generic list using motion controls.

Please see the [MotionListView.java](https://github.com/lnunno/AndroidInterfaceComponent/blob/master/app/src/main/java/com/lnunno/interfacecomponent/MotionListView.java) file for the implementation details, most of the other stuff in this repo is for 
the associated Android Studio project.

## Features
* Shaking the device shuffles the list.
* Moving the device up rapidly sorts the list.

## Caveats
* This list must be used with `Comparable` elements, otherwise there will be a runtime error.
