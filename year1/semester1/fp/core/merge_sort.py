def merge_sort(list, key = lambda item: item, reverse = False):
    if len(list) > 1:
        mid = len(list) // 2
        left_half = list[:mid]
        right_half = list[mid:]
        merge_sort(left_half, key = key, reverse = reverse)
        merge_sort(right_half, key = key, reverse = reverse)
        i = j = k = 0
        while i < len(left_half) and j < len(right_half):
            if not(reverse):
                if key(left_half[i]) < key(right_half[j]):
                    list[k] = left_half[i]
                    i = i+1
                else:
                    list[k] = right_half[j]
                    j = j+1
            else:
                if key(left_half[i]) > key(right_half[j]):
                    list[k] = left_half[i]
                    i = i+1
                else:
                    list[k] = right_half[j]
                    j = j+1
            k = k+1
        while i < len(left_half):
            list[k] = left_half[i]
            i = i+1
            k = k+1

        while j < len(right_half):
            list[k] = right_half[j]
            j = j+1
            k = k+1

merge_sort([3,2,1,4,5])