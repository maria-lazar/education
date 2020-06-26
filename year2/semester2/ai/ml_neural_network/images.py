import numpy as np
from PIL import Image


def to_sepia(img):
    width, height = img.size
    pixels = img.load()
    for py in range(height):
        for px in range(width):
            r, g, b = img.getpixel((px, py))

            tr = int(0.393 * r + 0.769 * g + 0.189 * b)
            tg = int(0.349 * r + 0.686 * g + 0.168 * b)
            tb = int(0.272 * r + 0.534 * g + 0.131 * b)

            if tr > 255:
                tr = 255

            if tg > 255:
                tg = 255

            if tb > 255:
                tb = 255

            pixels[px, py] = (tr, tg, tb)


def transform_to_sepia():
    for i in range(4101, 4201):
        img = Image.open("cats/cat." + str(i) + ".jpg")
        img_sepia = to_sepia(img)
        img.save("cats/cat." + str(i + 100) + ".jpg")

def resize():
    for i in range(4101, 4300):
        img = Image.open("cats/cat." + str(i) + ".jpg")
        imResize = img.resize((8, 8), Image.ANTIALIAS)
        imResize.save("cats/cat." + str(i) + ".jpg")
resize()

