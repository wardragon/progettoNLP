#!/usr/bin/python3
import json
import csv
import inquirer

annotatore = "Paolo Cerrito"

def sentiment():
    questions = [
      inquirer.List('size',
                    message="Sentiment della frase (1 Positivo, 2 Negativo, 3 Neutro)",
                    choices=['Positivo', 'Negativo', 'Neutro'],
                ),
    ]
    answers = inquirer.prompt(questions)
    return answers["size"]


with open("dataset.json",'r+', encoding='latin-1') as read_file:
    data = json.load(read_file)

with open("lista_pos_tagging.txt") as lista_txt:
    elenco = lista_txt.readline().split(',')
mappa = map_object = map(int, elenco)
elenco = list(mappa)
pos_tagged=list()

with open('50_pos_tagging_Cerrito.csv') as f:
    rf = csv.reader(f)
    for line in rf:
        pos = next(rf)
        merged = list()
        for i in range(len(line)):
            if line[i] == '': continue;
            merged.append(pos[i])
        pos_tagged.append(merged)

my_json = list()
count=0
for i in data:
    if i['id'] in elenco: 
        i['messaggioTags'] = pos_tagged[count]
        print(i['messaggio'])
        i['sentimentMessaggio'] = sentiment()
        i['messaggioWords'] = i['messaggio'].split()
        i['argomentoWords'] = i['argomento'].split()
        i['argomentoTags'] = pos_tagged[count + 1]
        print(i['argomento'])
        i['sentimentArgomento'] = sentiment()
        i['chiarimentiWords'] = i['chiarimenti'].split()
        print(i['chiarimenti'])
        i['sentimentChiarimenti'] = sentiment()
        i['chiarimentiTags'] = pos_tagged[count + 2]
        i['annotatore'] = annotatore
        count = count + 3
        my_json.append(i)

with open('Cerrito_50_pos_tagged.json', 'w') as fp:
    json.dump(my_json, fp, indent=1)
#
#with open('output.csv','w') as f:
#    wr = csv.writer(f)
#    for i in data:
#        if i['id'] not in (747, 766, 775, 789, 790, 840, 897, 902, 921, 927, 943, 959, 996, 1028, 1036, 1111, 1129): 
#            continue
#        outrw1 = i['messaggio'].split()
#        outrw2 = i['argomento'].split()
#        outrw3 = i['chiarimenti'].split()
#        wr.writerow(outrw1)
#        wr.writerow(len(outrw1)*[None])
#        wr.writerow(outrw2)
#        wr.writerow(len(outrw2)*[None])
#        wr.writerow(outrw3)
#        wr.writerow(len(outrw3)*[None])
#    f.close()
#    
