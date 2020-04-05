def bingo_sort(list, key = lambda item: item, reverse = False):
    max = len(list) - 1
    next_value = list[max]
    for i in range(max - 1, -1, -1):
        if not(reverse):
            if key(list[i]) > key(next_value):
                next_value = list[i]
        else:
            if key(list[i]) < key(next_value):
                next_value = list[i]

    while (max > 0) and (key(list[max]) == key(next_value)):
        max = max - 1

    while max > 0:
        value = next_value
        next_value = list[max]
        for i in range(max -1, -1, -1):
            if key(list[i]) == key(value):
                list[i], list[max] = list[max], list[i]
                max = max - 1
            elif not(reverse):
                if key(list[i]) > key(next_value):
                    next_value = list[i]
            else:
                if key(list[i]) < key(next_value):
                    next_value = list[i]
        while (max > 0) and (key(list[max]) == key(next_value)):
            max = max - 1



