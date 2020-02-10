#!/usr/bin/python3
import json
import csv

with open("dataset.json",'r', encoding='latin-1') as read_file:
    data = json.load(read_file)


with open('output.csv','w') as f:
    wr = csv.writer(f)
    for i in data:
        if i['id'] not in (747, 766, 775, 789, 790, 840, 897, 902, 921, 927, 943, 959, 996, 1028, 1036, 1111, 1129): 
            continue
        outrw1 = i['messaggio'].split()
        outrw2 = i['argomento'].split()
        outrw3 = i['chiarimenti'].split()
        wr.writerow(outrw1)
        wr.writerow(len(outrw1)*[None])
        wr.writerow(outrw2)
        wr.writerow(len(outrw2)*[None])
        wr.writerow(outrw3)
        wr.writerow(len(outrw3)*[None])
    f.close()
    
