import random
import string

def generate_number():
    number = random.randint(1,500)
    return number

def generate_name():
    first_letter = ''.join(random.choice(string.ascii_uppercase) for _ in range(0,1))
    i = random.randint(1,7)
    letters = ''.join(random.choice(string.ascii_lowercase) for _ in range(1, i))
    return first_letter + letters
