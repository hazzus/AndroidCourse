from PIL import Image, ImageOps, ImageDraw, ImageFont
import sys
import os

def crop(path, percentage):
    im = Image.open(path)
    width, height = im.size
    cropped_width = width * percentage // 100
    cropped_height = height * percentage // 100
    column = 0
    cropped_matrix = []
    for i in range(0, width, cropped_width):
        col = []
        row = 0
        for j in range(0, height, cropped_height):
            piece = (i, j, i + cropped_width, j + cropped_height)
            col.append(im.crop(piece))
            row += 1
            if row == 100:
                break
        cropped_matrix.append(col)
    return cropped_matrix, cropped_width, cropped_height

def concat(percentage, matrix, w, h, name):
    amount = 100 // percentage
    concated = Image.new('RGB', ((w + 2) * amount, (h + 2) * amount))
    for i in range(amount):
        for j in range(amount):
            part = matrix[i][j]
            part_draw = ImageDraw.Draw(part)
            fnt = ImageFont.truetype("arial.ttf", 6)
            part_draw.text((0, 0), '{}, {}'.format(i, j), font=fnt, fill='black')
            part_with_border = ImageOps.expand(part, border=1, fill='red')
            concated.paste(part_with_border, (i * (w + 2), j * (h + 2)))
    concated.save('webbed_' + name)



if __name__ == '__main__':
    percentage = int(sys.argv[1])
    paths = sys.argv[2:]
    for pic in paths:
        matrix, w, h = crop(pic, percentage)
        concat(percentage, matrix, w, h, os.path.basename(pic))
