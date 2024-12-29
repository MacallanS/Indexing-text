import numpy as np
import matplotlib.pyplot as plt
from tensorflow.keras.datasets import mnist
import tensorflow.keras as keras
from sklearn.preprocessing import OneHotEncoder
from sklearn.model_selection import train_test_split

(train_images, train_labels), (test_images, test_labels) = mnist.load_data()

train_images = train_images.reshape(-1, 28*28) / 255.0
test_images = test_images.reshape(-1, 28*28) / 255.0
train_labels = np.array(train_labels)
test_labels = np.array(test_labels)

def display_image(image_data: np.array):
    plt.imshow(image_data.reshape((28, 28)), cmap='gray')
    plt.show()

display_image(train_images[25])

train_labels = train_labels.reshape(-1, 1)
label_encoder = OneHotEncoder(categories='auto')
encoded_labels = label_encoder.fit_transform(train_labels).toarray()

train_images, test_images, train_labels, test_labels = train_test_split(train_images, encoded_labels)

neural_net = keras.Sequential([
    keras.Input(shape=(28*28,)),
    keras.layers.Dense(units=128, activation='relu'),
    keras.layers.Dense(10, activation='softmax')
])

neural_net.compile(optimizer='sgd', loss='categorical_crossentropy', metrics=['accuracy'])
neural_net.fit(train_images, train_labels, epochs=20, batch_size=128)
neural_net.evaluate(test_images, test_labels)

display_image(test_images[80])

predicted_output = neural_net.predict(test_images[80].reshape((1, -1)))
print("Number on picture is", np.argmax(predicted_output))
