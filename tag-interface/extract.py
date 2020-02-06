#!/usr/bin/python3
import json
import csv

with open("dataset.json",'r', encoding='latin-1') as read_file:
    data = json.load(read_file)


with open('output.csv','w') as f:
    wr = csv.writer(f)
    for i in data:
        if i['id'] not in ( 6, 21, 35, 44, 46, 55, 84, 99, 160, 167, 177, 213, 244, 282, 298, 301, 400, 430, 474, 510, 525, 563, 575, 578, 580, 625, 637, 666, 676, 685, 689, 1129): 
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
    
