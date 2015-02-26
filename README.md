# MotionListView
Interface component for CS 591: Holistic Mobile App Development at the University of New Mexico.

This is an extension of the Android ListView that uses the motion sensors in the 
device to allow for scrolling and shuffling of a generic list using motion controls.

Please see the [MotionListView.java](https://github.com/lnunno/AndroidInterfaceComponent/blob/master/app/src/main/java/com/lnunno/interfacecomponent/MotionListView.java) file for the implementation details, most of the other stuff in this repo is for 
the associated Android Studio project.

## Features
* *Shaking* the device **shuffles** the list.
* Moving the device *up* rapidly **sorts** the list.
* Tilting the device *forward* scrolls the list **down**.
* Tilting the device *backward* scrolls the list **up**.

## Video Demo
[Click here for a YouTube video demo.](http://youtu.be/16Shkz2pT6k)
![Video thumbnail](http://img.youtube.com/vi/16Shkz2pT6k/0.jpg)

## Customization
The following variables can be tweaked in the [MotionListView](https://github.com/lnunno/AndroidInterfaceComponent/blob/master/app/src/main/java/com/lnunno/interfacecomponent/MotionListView.java) class to change the sensitivity of the shake, tilt, and "lift" gestures. These numbers were determined from readings observed from a Samsung Galaxy S5 accelerometer and might need to be adjusted to be more general.

* `RESET_Y_THRESHOLD`: Threshold for "up" sort detection.
* `POS_Z_TILT_THRESHOLD`: Threshold to detect a forward tilt for scrolling.
* `NEG_Z_TILT_THRESHOLD`: Threshold to detect an away tilt for scrolling.
* `SHAKE_THRESHOLD`: Force needed for a shake.
* `UPDATE_INTERVAL_MILLIS`: How often movement detection is refreshed. Need a delay to detect bigger movements.
* `SCROLL_SENSITIVITY`: How much scrolling is done on tilt.

## Caveats
* This list must be used with `Comparable` elements, otherwise there will be a runtime error.
