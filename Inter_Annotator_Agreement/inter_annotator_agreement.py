# ------------------ Imports  ---------------------------------------

from itertools import combinations
import numpy as np
import math
import json

ANNOTATION_CLASSES = ["ADJ", "ADP", "ADV", "AUX", "CCONJ", "DET", "INTJ", "NOUN",
                      "NUM", "PART", "PRON", "PROPN", "PUNCT", "SCONJ", "SYM", "VERB", "X"]


def parse_json(path):

    f = open(path, "r")
    parsed_json = (json.loads(f.read()))

    annotations = {}
    error = 0
    for ann in parsed_json:
        list_of_annotations = []
        try:
            if ann['messaggioTags'] == None:
                list_of_annotations.append(["" for x in range(0, len(ann['messaggioWords']))])
            else:
                list_of_annotations.append(ann['messaggioTags'])
            if ann['argomentoTags'] == None:
                list_of_annotations.append(["" for x in range(0, len(ann['argomentoWords']))])
            else:
                list_of_annotations.append(ann['argomentoTags'])

            if ann['chiarimentiTags'] == None:
                list_of_annotations.append(["" for x in range(0, len(ann['chiarimentiWords']))])
            else:
                list_of_annotations.append(ann['chiarimentiTags'])

            if ann['id'] in annotations:
                annotations[ann['id']][ann['annotatore']] = list_of_annotations
            else:
                annotations[ann['id']] = {ann['annotatore']: list_of_annotations}

        except Exception as e:
            print("None argoument of: ", ann['id'] , ann['messaggioWords'], ann['argomentoWords'], ann['chiarimentiWords'])

    return annotations

def preprocessinf():
    annotations_results = parse_json('posOutput.json')
    annotators_map = {}
    
    sorted_keys = annotations_results.keys()
    sorted_keys.sort()
    for x in sorted_keys:
        y = annotations_results[x].keys()
        for ann in y:
            annotators_map[ann] = []

    for x in sorted_keys:
        y = annotations_results[x].keys()
        for ann in y:
            for row in annotations_results[x][ann]:
                for tag in row:
                    annotators_map[ann].append(tag)

    return annotators_map

def parse_json_sentiment(path):

    f = open(path, "r")
    parsed_json = (json.loads(f.read()))

    annotations = {}
    error = 0
    for ann in parsed_json:
        list_of_annotations = []

        list_of_annotations.append(ann['sentimentArgomento'])

        list_of_annotations.append(ann['sentimentChiarimenti'])


        if ann['id'] in annotations:
            annotations[ann['id']][ann['annotatore']] = list_of_annotations
        else:
            annotations[ann['id']] = {ann['annotatore']: list_of_annotations}


    return annotations


def preprocessinf_sentiment():
    annotations_results = parse_json_sentiment('posOutput.json')
    print(annotations_results)
    annotators_map = {}
    
    sorted_keys = annotations_results.keys()
    sorted_keys.sort()
    for x in sorted_keys:
        y = annotations_results[x].keys()
        for ann in y:
            annotators_map[ann] = []

    for x in sorted_keys:
        y = annotations_results[x].keys()
        for ann in y:
            for row in annotations_results[x][ann]:
              annotators_map[ann].append(row)

    return annotators_map


# ------------------ Pos-tagging/Sentiment Agreement for 2 annotators  ---------------------------------------

def basic_agreement(row1, row2, classes, agreement_function):
    # annotator statistics: for each annotetor and for each class we
    # count the number of time that annotator has choosen that class.
    antr_1_stats = {}
    antr_2_stats = {}
    accuracy = 0
    terms = 0
    
    for c in classes:
        antr_1_stats[c] = 0
        antr_2_stats[c] = 0
        
    # check annotations
    for i in range(0, min(len(row1), len(row2))):
        annotation_1 = row1[i].upper()
        annotation_2 = row2[i].upper()

        # check if they both have annotated that word. If not, do not
        # consider it.
        if annotation_1 != "" and annotation_2 != "":
            if annotation_1 == annotation_2:
                accuracy += 1

            antr_1_stats[annotation_1] += 1
            antr_2_stats[annotation_2] += 1
            terms += 1

    # compute relative observed agreement
    accuracy = accuracy / float(terms)
    pe = agreement_function(antr_1_stats, antr_2_stats, classes, terms)
    
    agreement = (accuracy - pe)/(1 - pe)
    return agreement

# compute hypothetical probability of chance agreement described in
# cohen's kappa method
def cohen_kappa_hp(antr_1_stats, antr_2_stats, classes, terms):
    pe = 0
    for c in classes:
        pe += antr_1_stats[c] * antr_2_stats[c]
    pe = pe / float((terms**2))

    return pe

# compute hypothetical probability of chance agreement described in
# scott's pi method
def scott_pi_hp(antr_1_stats, antr_2_stats, classes, terms):
    pe = 0
    for c in classes:
        pe += ((antr_1_stats[c] + antr_2_stats[c])/float(terms))**2

    return pe

# ------------------ Pos-tagging/Sentiment Agreement for multiple annotators  ---------------------------------------

# Implementation of ideas found in
# https://www.wikiwand.com/en/Fleiss%27_kappa
# 
def fleiss_kappa(rows, classes):
    # we need to calculate, for each word and for each category, how
    # many annotators assigned that category for that word.

    # how many annotators do we have?
    n = len(rows)

    # how many subjects do we have at most?
    N_max = len(rows[0])

    # how many classes do we have?
    k = len(classes)

    classes_to_num = {}
    for i in range(0, len(classes)):
        classes_to_num[classes[i]] = i
    
    # matrix[i][j] := number of raters who assigned the i-th subject
    # to the j-th category
    m = np.zeros((N_max, k))
    for i in range(0, N_max):
        for a in range(0, n):
            if i < len(rows[a]) and rows[a][i] != '':
                m[i][classes_to_num[rows[a][i].upper()]] += 1


    # P[i] = extent to which raters agree for the i-th subject
    P = [0] * N_max
    for i in range(0, N_max):
        for j in range(0, k):
            P[i] += m[i][j]**2
            
        P[i] = P[i] - n
        P[i] = (1/float((n * (n-1)))) * P[i]

    # P_mean = mean of the Pi's
    P_mean = 0
    for i in range(0, N_max):
        P_mean += P[i]
    P_mean = P_mean / float(N_max)

                
    # p[j] = proportion of all assignment which were to the j-th category
    p = [0] * k
    for j in range(0, k):
        for i in range(0, N_max):
            p[j] += m[i][j]
        p[j] = p[j] / (float)(N_max*n)
    
    # PE as defined in the wikipedia page
    PE = 0
    for j in range(0, k):
        PE += p[j]**2

    # final agreement
    return (P_mean - PE)/float(1 - PE)


# ------------------ Semantic Agreement for 2 annotators ---------------------------------------

# computes the kendel tau correlation between row1 and row2, where
# row1 and row2 are lists of values between 1 and 5.
def kendell_tau(col1, col2):
    # 1) make table of rankings and order it using the first row.
    data_matrix = np.column_stack([col1, col2])
    ind = np.argsort(data_matrix[:,0])
    data_matrix = data_matrix[ind]
    
    # 2) count number of concordant pairs: for every element of the
    # second row count how many larger elements are below it.
    concordant_pairs_list = []
    column = data_matrix[:,1]

    for i in range(0, len(column)):
        concordant_pairs = 0
        for x in column[i:]:
            if x > column[i]:
                concordant_pairs += 1
        concordant_pairs_list.append(concordant_pairs)
    
    # 3) count number of discordant pairs:
    discordant_pairs_list = []
    column = data_matrix[:,1]

    for i in range(0, len(column)):
        discordant_pairs = 0
        for x in column[i:]:
            if x < column[i]:
                discordant_pairs += 1
        discordant_pairs_list.append(discordant_pairs)

    # 4) sum concordant pair values and discordant pair values
    concordant_value = sum(concordant_pairs_list)
    discordant_value = sum(discordant_pairs_list)

    return (float(concordant_value - discordant_value)) / (float(concordant_value + discordant_value))

# ------------------ Semantic Agreement for multiple annotators ---------------------------------------

# TODO: understand how to extend kendell's tau to multiple annotators.

# ------------------ Testing area ---------------------------------------
print("|--------------- Syntactical annotation agreement testing --------------------|")

preprocessinf_sentiment()

sorted_list = list(preprocessinf().values())
sorted_list.sort()

iaa_pos = fleiss_kappa(list(preprocessinf().values()), ANNOTATION_CLASSES)
iaa_sentiment = fleiss_kappa(list(preprocessinf_sentiment().values()), ['POSITIVO', 'NEGATIVO', 'NEUTRO'])

print("POS: ",iaa_pos)
print("SENTIMENT: ",iaa_sentiment)


# ---------
print("|--------------- Semantical annotation agreement testing --------------------|")

# compute statistical significance for kendells tau

col1 = [1, 2, 5, 6, 4, 3, 7, 8, 9, 10, 11, 12]
col2 = [1, 2, 6, 5, 3, 4, 8, 7, 10, 9, 12, 11]
n = len(col1)
tau = kendell_tau(col1, col2)
z = 3 * tau * math.sqrt(n * (n-1)) / math.sqrt(2 * (2*n + 5))

print(f"Tau     is: {tau}")
print(f"Z-value is: {z}")


