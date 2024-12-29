import numpy as np
import matplotlib.pyplot as plt
from tensorflow.keras.datasets import mnist
import tensorflow.keras as keras
from sklearn.preprocessing import OneHotEncoder
from sklearn.model_selection import train_test_split

(X_train, y_train), (X_test, y_test) = mnist.load_data()

X_train = X_train.reshape(-1, 28*28) / 255.0
X_test = X_test.reshape(-1, 28*28) / 255.0
y_train = np.array(y_train)
y_test = np.array(y_test)

def plot_image(pixels: np.array):
    plt.imshow(pixels.reshape((28, 28)), cmap='gray')
    plt.show()

plot_image(X_train[25])

y_train = y_train.reshape(-1, 1)
encoder = OneHotEncoder(categories='auto')
labels_onehot = encoder.fit_transform(y_train).toarray()

X_train, X_test, y_train, y_test = train_test_split(X_train, labels_onehot)

model = keras.Sequential([
    keras.Input(shape=(28*28,)),
    keras.layers.Dense(units=128, activation='relu'),
    keras.layers.Dense(10, activation='softmax')
])

model.compile(optimizer='sgd', loss='categorical_crossentropy', metrics=['accuracy'])

model.fit(X_train, y_train, epochs=20, batch_size=128)

model.evaluate(X_test, y_test)

plot_image(X_test[80])

predicted_results = model.predict(X_test[80].reshape((1, -1)))
print("Number on picture is", np.argmax(predicted_results))
