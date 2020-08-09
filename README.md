# Developers
* Joshua Burkey
* Vaibhav Khamesra
* Phat Le
* Ivan Ahumada

# Description
SafariSnap is an android application that uses image classification to classify 6 different animals given an image:
* Antelopes
* Bobcats
* Buffalos
* Chimpanzees
* Rhinoceroses
* Wolves

The application features a front-end developed in Android Studio and back-end developed on Spyder IDE using Anaconda.


README 
VER 1.0

INSTALLATION INSTRUCTIONS FOR ML COMPONENT
The machine learning component has been tested using the Spyder IDE which is heavily recommended for use with this program. The machine learning code requires Opencv to operate properly, its used for the image configuring before training and testing. For instructions on installing Opencv if you dont have it, make sure you are running the latest version of anaconda and use the instructions here: https://anaconda.org/conda-forge/opencv . Tensorflow and keras are also required, refer to this page for instructions on installing them (pip is recommened for installation of tensorflow) see these links, for keras: https://anaconda.org/conda-forge/keras for tensorflow: https://www.tensorflow.org/install/pip . Simply unzip the machine learning code and it will automatically establish the testing, training and model directories. /test should have 125 images which are used to confirm model fidelity after every compilation run, /train contains 600 images used for training (this is all that is needed since we are using the VGG16 pretrained model as a base) /model is where the tflite and h5 models are stored. Note the code will compile an .h5 after each iteration, this is due to the low number of iterations and use of a pretrained model. 
Recommeneded specifications:
Ryzen 5 3400g or better
16gb ram
Windows 10 operating system
//put storage here//

Note: these are not neccessarily the minimum specifications, but what the code was tested on. Specifications lower than these will increase the time per training session, while an Nvidia GPU will decrease resource usage.

To run the code in spyder after all extensions have been installed correctly simply locate the large green play button icon in the tool bar on the upper left of the screen and click it. The model will run for 30 iterations, compile an .h5 each iteration, and then print a random selection of 30 testing images with the titles as assigned by the Neural Network model which is used to check for functional accuracy, it will then compile a .tflite model for use in android and other mobile applications.
