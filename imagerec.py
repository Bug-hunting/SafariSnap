# -*- coding: utf-8 -*-
"""
Created on Sat Jul 25 19:00:10 2020

@author: Josh Burkey & Ivan Ahumada
based on code by arun prakash
https://blog.francium.tech/build-your-own-image-classifier-with-tensorflow-and-keras-dc147a15e38e

"""

import cv2
import numpy as np
import os
from random import shuffle
from tqdm import tqdm
import tensorflow as tf
import matplotlib.pyplot as plt

model_weights_path = 'model'
train_data = 'train'
test_data = 'test'

def one_hot_label(img):
    label = img.split('.')[0]
    if label == 'antelope':
        ohl = np.array([1,0,0,0,0,0])
    elif label == 'bobcat':
        ohl = np.array([0,1,0,0,0,0])
    elif label == 'buffalo':
        ohl = np.array([0,0,1,0,0,0])
    elif label == 'chimpanzee':
        ohl = np.array([0,0,0,1,0,0])
    elif label == 'rhinoceros':
        ohl = np.array([0,0,0,0,1,0])
    elif label == 'wolf':
        ohl = np.array([0,0,0,0,0,1])
    return ohl

def train_data_with_label():
    train_images = []
    for i in tqdm(os.listdir(train_data)):
        path = os.path.join(train_data, i)
        img = cv2.imread(path, cv2.IMREAD_COLOR)
        img = cv2.resize(img, (200, 200))
        train_images.append([np.array(img), one_hot_label(i)])
    shuffle(train_images)
    return train_images

def test_data_with_label():
    test_images = []
    for i in tqdm(os.listdir(test_data)):
        path = os.path.join(test_data, i)
        img = cv2.imread(path, cv2.IMREAD_COLOR)
        img = cv2.resize(img, (200, 200))
        test_images.append([np.array(img), one_hot_label(i)])
        shuffle(test_images)
    return test_images

from keras.models import Sequential
from keras.models import Model
from keras.layers import *
from keras.optimizers import *
from keras.callbacks import *
from keras.applications.vgg16 import VGG16

checkpoint_callback = ModelCheckpoint(filepath=model_weights_path+"/"+'best_model.h5', monitor= 'accuracy', 
                                      verbose=1, save_best_only=False,
                                      include_optimizer=False, mode='max')

training_images = train_data_with_label()
testing_images = test_data_with_label()

tr_img_data = np.array([i[0] for i in training_images]).reshape(-1,200,200,3)
tr_lbl_data = np.array([i[1] for i in training_images])

tst_img_data = np.array([i[0] for i in testing_images]).reshape(-1,200,200,3)
tst_lbl_data = np.array([i[1] for i in testing_images])

IMG_SHAPE = (200,200,3)
base_model = VGG16(input_shape = IMG_SHAPE,include_top = False,weights = 'imagenet')
base_model.trainable = False

model = Sequential()
model.add(base_model)

model.add(InputLayer(input_shape=[200,200,3]))
model.add(Conv2D(filters=32,kernel_size=5,strides=1,padding='same', activation='relu'))
model.add(MaxPool2D(pool_size=5,padding='same'))

model.add(Conv2D(filters=50,kernel_size=5,strides=1,padding='same', activation='relu'))
model.add(MaxPool2D(pool_size=5,padding='same'))

model.add(Conv2D(filters=80,kernel_size=5,strides=1,padding='same', activation='relu'))
model.add(MaxPool2D(pool_size=5,padding='same'))

model.add(Dropout(0.25))
model.add(Flatten())
model.add(Dense(512,activation='relu'))
model.add(Dropout(rate=0.5))
model.add(Dense(6,activation='softmax'))

optimizer = Adam(lr=1e-3)

model.compile(optimizer = optimizer, loss='categorical_crossentropy', metrics=['accuracy'])
model.fit(x=tr_img_data,y=tr_lbl_data,epochs=30,batch_size=100, callbacks = [checkpoint_callback])
model = Model(model.input,model.output)
model.summary()



fig = plt.figure(figsize=(14, 14))

for cnt, data, in enumerate(testing_images[10:40]):
    
    y = fig.add_subplot(6, 5, cnt+1)
    img = data[0]
    data = img.reshape(1,200, 200,3)
    model_out = model.predict([data])
    
    if np.argmax(model_out) == 0:
        str_label='antelope'
    elif np.argmax(model_out) == 1:
        str_label='bobcat'
    elif np.argmax(model_out) == 2:
        str_label='buffalo'
    elif np.argmax(model_out) == 3:
        str_label='chimpanzee'
    elif np.argmax(model_out) == 4:
        str_label='rhinoceros'
    elif np.argmax(model_out) == 5:
        str_label='wolf'
        
    y.imshow(img, cmap='gray')
    plt.title(str_label)
    print(str_label)
    y.axes.get_xaxis().set_visible(False)
    y.axes.get_yaxis().set_visible(False)
    plt.show()

converter = tf.lite.TFLiteConverter.from_keras_model(model)
tflite_model = converter.convert()
open(model_weights_path + "/" + 'img_rec_model.tflite', 'wb').write(tflite_model)
    


