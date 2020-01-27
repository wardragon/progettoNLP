# ------------------ Imports  ---------------------------------------

# import gspread
from pprint import pprint 
from itertools import combinations
import numpy as np
import math

ANNOTATION_CLASSES = ["ADJ", "ADP", "ADV", "AUX", "CCONJ", "DET", "INTJ", "NOUN",
                      "NUM", "PART", "PRON", "PROPN", "PUNCT", "SCONJ", "SYM", "VERB", "X"]

# Data taken from google sheet
ROWS = [
    [u'ADV', u'VERB', u'ADV', u'ADJ', u'SCONJ', u'DET', u'NOUN', u'DET', u'NOUN', u'ADP', u'NOUN', u'ADJ',
     u'ADP', u'PROPN', u'PROPN', u'PROPN', u'VERB', u'ADJ', u'SYM', u'CCONJ', u'SCONJ', u'VERB', u'DET', u'ADJ',
     u'NOUN', u'SCONJ', u'VERB', u'AUX', u'ADJ', u'ADP', u'NOUN', u'SYM', u'PUNCT', u'ADV', u'SCONJ', u'VERB',
     u'ADJ', u'VERB', u'SCONJ', u'ADV', u'ADJ', u'NOUN', u'ADP', u'ADJ', u'NOUN', u'CCONJ', u'DET', u'NOUN', 
     u'ADV', u'VERB', u'ADJ', u'DET', u'NOUN', u'CCONJ', u'DET', u'NOUN', u'PROPN', u'PART', u'PROPN', u'PUNCT'],
    
    [u'part', u'aux', u'adv', u'adj', u'sconj', u'det', u'noun', u'adp', u'noun', u'adp', u'noun', u'adj',
     u'adp', u'propn', u'propn', u'propn', u'aux', u'adj', u'sym', u'cconj', u'cconj', u'verb', u'det', u'adj',
     u'noun', u'sconj', u'verb', u'verb', u'verb', u'adp', u'propn', u'sym', u'sym', u'sconj', u'sconj', u'verb',
     u'adj', u'verb', u'adp', u'adp', u'adj', u'noun', u'', u'adj', u'noun', u'cconj', u'adp', u'noun', u'adv',
     u'aux', u'adj', u'adp', u'noun', u'cconj', u'adp', u'propn', u'propn', u'propn', u'propn', u'sym'],
    
    [u'CCONJ', u'VERB', u'ADV', u'ADJ', u'SCONJ', u'DET', u'NOUN', u'ADP', u'NOUN', u'ADP', u'NOUN', u'ADJ',
     u'ADP', u'PROPN', u'PROPN', u'PROPN', u'AUX', u'VERB', u'SYM', u'SCONJ', u'CCONJ', u'VERB', u'DET', u'ADJ',
     u'NOUN', u'SCONJ', u'AUX', u'VERB', u'ADJ', u'ADP', u'PROPN', u'SYM', u'PUNCT', u'ADJ', u'ADV', u'AUX', u'VERB',
     u'VERB', u'PRON', u'ADV', u'ADJ', u'NOUN', u'ADP', u'ADJ', u'NOUN', u'ADP', u'DET', u'NOUN', u'ADV', u'AUX',
     u'VERB', u'DET', u'NOUN', u'ADP', u'DET', u'NOUN', u'PROPN', u'PROPN', u'PROPN', u'PUNCT'],

    [u'ADV', u'VERB', u'ADV', u'ADJ', u'CCONJ', u'DET', u'NOUN', u'ADP', u'NOUN', u'CCONJ', u'NOUN', u'ADJ',
     u'ADP', u'PROPN', u'PROPN', u'PROPN', u'VERB', u'ADJ', u'PUNCT', u'ADV', u'CCONJ', u'VERB', u'DET', u'ADJ',
     u'NOUN', u'CCONJ', u'VERB', u'VERB', u'VERB', u'ADP', u'PROPN', u'PUNCT', u'PUNCT', u'PRON', u'CCONJ',
     u'VERB', u'ADJ', u'VERB', u'CCONJ', u'ADJ', u'ADJ', u'NOUN', u'ADP', u'ADJ', u'NOUN', u'ADP', u'DET', u'NOUN',
     u'ADV', u'VERB', u'ADJ', u'DET', u'NOUN', u'ADP', u'DET', u'PROPN', u'PROPN', u'PROPN', u'PROPN', u'PUNCT'],

    [u'ADV', u'VERB', u'ADV', u'ADJ', u'SCONJ', u'DET', u'NOUN', u'ADP', u'NOUN', u'ADP', u'NOUN', u'ADJ', u'ADP',
     u'PROPN', u'PROPN', u'PROPN', u'VERB', u'ADJ', u'SYM', u'SCONJ', u'SCONJ', u'VERB', u'DET' , u'ADJ', u'NOUN',
     u'SCONJ', u'AUX', u'AUX', u'VERB', u'ADP', u'NOUN', u'SYM', u'PUNCT', u'PRON', u'SCONJ', u'VERB', u'ADJ', u'VERB',
     u'SCONJ', u'ADV', u'PRON', u'NOUN', u'ADP', u'ADJ', u'NOUN', u'ADP', u'DET', u'NOUN', u'NOUN', u'VERB', u'ADJ',
     u'DET', u'NOUN', u'ADP', u'DET', u'NOUN', u'PROPN', u'PROPN', u'PROPN'],

    [u'AUX', u'VERB', u'ADJ', u'NOUN', u'SCONJ', u'DET', u'NOUN', u'ADP', u'NOUN', u'ADP', u'NOUN', u'ADJ', u'ADP',
     u'PROPN', u'PROPN', u'PROPN', u'VERB', u'NOUN', u'PUNCT', u'SCONJ', u'SCONJ', u'VERB', u'DET', u'ADJ', u'NOUN',
     u'ADV', u'AUX', u'VERB', u'VERB', u'ADP', u'PROPN', u'PUNCT', u'PUNCT', u'DET', u'SCONJ', u'VERB', u'ADV', u'VERB',
     u'SCONJ', u'ADV', u'PRON', u'NOUN', u'ADP', u'ADV', u'NOUN', u'ADP', u'DET', u'NOUN', u'ADV', u'VERB', u'NOUN',
     u'DET', u'NOUN', u'ADP', u'DET', u'NOUN', u'PROPN', u'PROPN', u'PROPN', u'PUNCT'],

    [u'ADV', u'AUX', u'ADJ', u'NOUN', u'SCONJ', u'DET', u'NOUN', u'ADP', u'NOUN', u'ADP', u'NOUN', u'ADJ', u'ADP',
     u'PROPN', u'PROPN', u'PROPN', u'AUX', u'NOUN', u'PUNCT', u'CCONJ', u'SCONJ', u'VERB', u'DET', u'ADJ', u'NOUN',
     u'SCONJ', u'AUX', u'VERB', u'ADV', u'ADP', u'PROPN', u'PUNCT', u'PUNCT', u'PRON', u'SCONJ', u'AUX', u'ADV',
     u'AUX', u'SCONJ', u'ADV', u'ADV', u'NOUN', u'ADP', u'ADJ', u'NOUN', u'ADP', u'DET', u'NOUN', u'ADV', u'AUX',
     u'NOUN', u'DET', u'NOUN', u'ADP', u'DET', u'PROPN', u'PROPN', u'PART', u'PROPN', u'PUNCT']
]

# -------------------------------------------------------------------------------------

data = {
    34: {"tamiano": ["AUX", "VERB"],
         "salman": ["NOUN", "VERB"]
    },

    1352: {"tamiano": ["AUX", "VERB"],
           "salman": ["NOUN", "VERB"]
    }
}

# obtained by linearizing, in order, 
flatten_data = {
    "tamiano": ["AUX", "VERB", "AUX", "VERB"],
    "salman": ["NOUN", "VERB", "NOUN", "VERB"]
}

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

# print coefficients for given spreadsheet
# my_list = [1, 2, 3, 4, 5, 6, 7]
my_list = [1, 2]
function = cohen_kappa_hp
for pair in combinations(my_list, 2):
    i, j = pair
    value = basic_agreement(ROWS[i-1], ROWS[j-1], ANNOTATION_CLASSES, function)
    print("Scott's pi coefficient for " + str(i) + ", " + str(j) + " is: " + str(value))
    
print(f"Fleiss kappa is: {fleiss_kappa(ROWS, ANNOTATION_CLASSES)}")

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


