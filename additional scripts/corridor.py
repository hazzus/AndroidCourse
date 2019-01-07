import numpy as np
import cv2

def warn(*args, **kwargs):
    pass
import warnings
warnings.warn = warn


from sklearn.cluster import KMeans
import json


def centroid_histogram(clt):
    # grab the number of different clusters and create a histogram
    # based on the number of pixels assigned to each cluster
    num_labels = np.arange(0, len(np.unique(clt.labels_)) + 1)
    (hist, _) = np.histogram(clt.labels_, bins=num_labels)

    # normalize the histogram, such that it sums to one
    hist = hist.astype("float")
    hist /= hist.sum()

    # return the histogram
    return hist

floor_number = int(input('floor number: '))
picture = 'floor{}.png'.format(floor_number)

image = cv2.imread(picture)

lower_list = [195, 195, 195]
upper_list = [240, 240, 240]
lower = np.array(lower_list, dtype="uint8")
upper = np.array(upper_list, dtype="uint8")
mask = cv2.inRange(image, lower, upper)
image = cv2.bitwise_and(image, image, mask=mask)

height, width, channels = image.shape
cropped_width = width // 100
cropped_height = height // 100

result = []
try:
    for j in range(cropped_width * 4, width, cropped_width):
        for i in range(cropped_height * 6, height, cropped_height):
            counti = i // cropped_height
            countj = j // cropped_width
            if countj > 91:
                continue
            if countj > 22 and counti > 44:
                continue
            print(i // cropped_height, j // cropped_width)
            piece = image[i:i + cropped_height, j:j + cropped_width]
            piece = piece.reshape((piece.shape[0] * piece.shape[1], 3))
            clt = KMeans(n_clusters=2)
            clt.fit(piece)
            hist = centroid_histogram(clt)
            for (percent, color) in zip(hist, clt.cluster_centers_):
                color = color.astype('uint8').tolist()
                print(percent, color)
                if (lower_list <= color <= upper_list) and (percent > 0.5):
                    result.append({"x": j // cropped_width, "y": i // cropped_height,  "floor": 1})
                    print('oh my god it is corridor!')
except KeyboardInterrupt:
    pass

with open('floor{}coridors.json'.format(floor_number), 'w') as f:
    f.write(json.dumps(result))


