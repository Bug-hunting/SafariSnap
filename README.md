# Developers
* Joshua Burkey
* Vaibhav Khamesra
* Phat Le
* Ivan Ahumada

# Description
**SafariSnap** is an android application that uses image classification to classify 6 different animals given an image:
* Antelopes
* Bobcats
* Buffalos
* Chimpanzees
* Rhinoceroses
* Wolves

The application features a front-end developed in Android Studio and back-end developed on Spyder IDE using Anaconda.

# Installation Instructions for ML Component
The machine learning component has been tested using Spyder IDE which is **heavily** recommended for use with this program.

```imagerec.py``` requires [OpenCV](https://anaconda.org/conda-forge/opencv) to operate properly as it's used for the image configuring before training and testing. For instructions on installing OpenCV, make sure you are running the latest version of Anaconda and use the instructions here: https://anaconda.org/conda-forge/opencv

[TensorFlow](https://www.tensorflow.org/install/pip) and [Keras](https://anaconda.org/conda-forge/keras) are also required; Refer to the links for instructions on installing them (pip is recommended for installation of TensorFlow). Simply unzip the machine learning code and it will automatically establish the testing, training and model directories. 

```/test``` should have 125 images which are used to confirm model fidelity after every compilation run

```/train``` contains 600 images used for training (this is all that is needed since we are using the VGG16 pretrained model as a base) 

```/model``` is where the tflite and h5 models are stored. Note the code will compile an ```.h5``` after each iteration, this is due to the low number of iterations and use of a pretrained model. 

# Installation and Setup Instructions for Android Studio
Install Android studio on your system
To set up the virtual device
Click on open AVD Manager> Create Virtual Device >select phone > select device name > press next >download api level 24 (System Image = Nougat) > finish

To run the application:
Download the github project.
Open android studio > open an existing android studio project > go to the location where you downloaded the project > in the directory you will see a android icon (green alien) > select the project name SafariSnap-master > press ok

Wait for the build gradle to finish. Once it completes loading the project >select you android emulator device and press the green arrow to run.

For first time users you have to create a new account in order to login and use the application functionality.


## Recommended specifications:
* A processor capable of AVX instructions (confirmed supported on the Ryzen 5 3000 series)
* 16gb ram
* Windows 10 operating system
* At least 200 megs free on disk.

***Note:** These are not neccessarily the minimum specifications, but what the code was tested on. Specifications lower than these will increase the time per training session, while an Nvidia GPU will decrease resource usage.*

To run ```imagerec.py``` in Spyder after all extensions have been installed correctly simply locate the large green play button icon in the tool bar on the upper left of the screen and click it.

The model will run for 30 iterations, compile an ```.h5``` file each iteration, and then print a random selection of 30 testing images with the titles that are assigned by the Neural Network model, which is used to check for functional accuracy, it will then generate a ```.tflite``` model for use in Android and other mobile applications.
