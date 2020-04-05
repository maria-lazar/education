def quick_sort(list, key = lambda item: item, reverse = False):
   quick_sort_helper(list, 0, len(list) - 1, key = key, reverse = reverse)

def quick_sort_helper(list, first, last, key = lambda item: item, reverse = False):
   if first < last:
       splitpoint = partition(list, first, last, key = key, reverse = reverse)
       quick_sort_helper(list, first, splitpoint - 1, key = key, reverse = reverse)
       quick_sort_helper(list, splitpoint + 1, last, key = key, reverse = reverse)

def partition(list, first, last, key = lambda item: item, reverse = False):
   pivot_value = list[first]
   left_mark = first + 1
   right_mark = last

   done = False
   while not done:
        if not(reverse):
            while left_mark <= right_mark and key(list[left_mark]) <= key(pivot_value):
                left_mark = left_mark + 1
            while key(list[right_mark]) >= key(pivot_value) and right_mark >= left_mark:
                right_mark = right_mark - 1
        else:
            while left_mark <= right_mark and key(list[left_mark]) >= key(pivot_value):
                left_mark = left_mark + 1
            while key(list[right_mark]) <= key(pivot_value) and right_mark >= left_mark:
                right_mark = right_mark -1
        if right_mark < left_mark:
            done = True
        else:
            aux = list[left_mark]
            list[left_mark] = list[right_mark]
            list[right_mark] = aux
   aux = list[first]
   list[first] = list[right_mark]
   list[right_mark] = aux
   return right_mark

a = [(54,26), (93,17), (77,31), (44,55), (20,0)]
quick_sort(a, key = lambda item: item[0], reverse = True)
print(a)

'''
Complexitate:
    - timp:
        -> worst case: cand pivotul este cel mai mic/mare element din lista, iar lista se partitioneaza in 2 liste de 0 si n-1 elemente
            T(n) = O(n) + T(0) + T(n-1) = O(n) + T(n-1) = O(n^2)
        -> best case: cand pivotul teoretic se plaseaza la mijlocul listei pentru fiecare apel
            T(n) = O(n) + 2T(n/2) = O(n*log2(n))
    - spatiu: O(1)            
'''