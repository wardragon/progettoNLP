#!/usr/bin/env python3
# -*- coding: utf-8 -*-

# Made with love by Leonardo Tamiano, 28/01/2020.

import json
import re
import os
from cmd import Cmd

VALID_POS_TAGS = ["ADJ", "ADP", "ADV", "AUX", "CCONJ", "DET",
                  "INTJ", "NOUN", "NUM", "PART", "PRON",
                  "PROPN", "PUNCT", "SCONJ", "SYM", "VERB"]

MAX_ID = 1433

# TODO: find a way to pass this as a local variable instead of having
# it global
ANNOTATOR_NAME = ""

# taken and slightly modified from stack overflow
# https://stackoverflow.com/questions/4697006/python-split-string-by-list-of-separators
def split(txt, remove_seps, keep_seps):
    default_sep = remove_seps[0]

    # we skip seps[0] because that's the default separator
    for sep in remove_seps[1:]:
        txt = txt.replace(sep, default_sep)

    for sep in keep_seps:
        txt = txt.replace(sep, default_sep + sep + default_sep)
        
    return [i.strip() for i in txt.split(default_sep) if i != ""]


# function used to update the json files
def update_json(data, filename):
    if not os.path.isfile(filename):
        # file does not exist
        with open(filename, "a+", encoding="latin_1") as output_file:
            output_file.write("[")
            json.dump(data, output_file, ensure_ascii=False, indent=1)
            output_file.write("]")
    else:
        # file already exists

        # modify previously created file by chaning the last ']'
        # with a ','.
        with open(filename, "rb+") as f:
            bytes_read = list(f.read())
            bytes_read.reverse()

            for i in range(0, len(bytes_read)):
                if bytes_read[i] == 93:
                    bytes_read[i] = 44
                    break

            bytes_read.reverse()
            bytes_to_write = bytes(bytes_read)

            f.seek(0)
            f.write(bytes_to_write)
            
        with open(filename, "a+", encoding="latin_1") as output_file:
            json.dump(data, output_file, ensure_ascii=False, indent=1)
            output_file.write("]")

class SyntacticRecord:
    '''#
    
    A record object consist of three columns regarding the feedback that
    each student left for a given lecture:
    
    1) column_1 related to the message of the lecture.

    2) column_2 related to the subjects discussed during lecture
    
    3) column_3 related to any clarification regarding the lecture,
       i.e. the things that the student did not understand properly.

    '''

    def __init__(self, object_id):
        with open("dataset.json", 'r', encoding="latin_1") as dataset:
            json_dataset = json.load(dataset)

            for json_object in json_dataset:
                if json_object['id'] == object_id:
                    # parse the object to get the data required for
                    # later pos tagging
                    self.object_id = object_id
                    self.date = json_object['data']
                    self.strings = [json_object['messaggio'], json_object['argomento'], json_object['chiarimenti']]
                    self.splitted_strings = [[], [], []]
                    self.pos = [[], [], []]
                    self.sentiment = [0, 0, 0]

    def do_pos_tag_on_column(self, column_id):
        # pos tag first column
        print(f"/-----------------------------------------------------/")        
        print(f"Do pos tagging on the following (id = {self.object_id}; column = {column_id}):\n\n"
              f"\t\"{self.strings[column_id]}\"\n")
            
        print("The available tags are:\n"
              "\n"
              "\tADJ (adjective), ADP (adjposition), ADV (adverb), AUX (auxiliary), \n"
              "\tCCONJ (coordinating conjunction), DET (determiner), INTJ (interjection), \n"
              "\tNOUN (noun), NUM (numeral), PART (particle), PRON (pronoun), PROPN (proper noun), \n"
              "\tPUNCT (punctuation), SCONJ (subordinating conjunction), SYM (symbol), VERB (verb). \n")
            
        '''
        Remove Split on: 'a 'e 'o 'u 'i <whitespace>
        Keep split on: ( ) . ,
        '''
        # TODO: check if it splits on newline (\n)
        atoms = split(self.strings[column_id], [" ", "'"], ["(", ")", ".", ",", "!", "?", ";", "\n"])
        pos_tags = [0] * len(atoms)
        i = 0
        while i < len(atoms):
            pos_tag = input(f"Pos tag for \"{atoms[i]}\": ")
            pos_tag = re.sub(' +', ' ', pos_tag).strip()                
            if pos_tag == "b":
                if i > 0:
                    i -= 1
            elif pos_tag not in VALID_POS_TAGS:
                print("ERROR: invalid POS tagging detected!")
            else:
                pos_tags[i] = pos_tag
                i += 1

        self.splitted_strings[column_id] = atoms
        self.pos[column_id] = pos_tags
        return

    def do_sentiment_analysis_on_column(self, column_id):
        print(f"/-----------------------------------------------------/")                
        done = 0
        while not done:
            print(f"Do sentiment analysis on the following (id = {self.object_id}; column = {column_id}):\n\n"
                  f"\t\"{self.strings[column_id]}\"\n")
            
            print("The available values are:\n"
                  "\n"
                  "\t0 = neutral\n"
                  "\t1 = positive\n"
                  "\t2 = negative\n")

            sentiment_input = input()
            done = 1
            if not sentiment_input.isdigit() or int(sentiment_input) > 2:
                print("ERROR: invalid sentiment value!")
                done = 0
                
        self.sentiment[column_id] = int(sentiment_input)
        return
                    
    def annotate(self):
        self.do_pos_tag_on_column(0)
        self.do_sentiment_analysis_on_column(0)

        self.do_pos_tag_on_column(1)
        self.do_sentiment_analysis_on_column(1)

        self.do_pos_tag_on_column(2)
        self.do_sentiment_analysis_on_column(2)

        return

    def write_json_output(self):
        global ANNOTATOR_NAME
        
        filename = "PosOutput.json"

        sentimento_messaggio = ""
        sentimento_argomento = ""
        sentimento_chiarimenti = ""

        SENTIMENT_CODE = {
            0: "Neutro",
            1: "Positivo",
            2: "Negativo"
        }
        
        # construct dict object
        data = {
            "id": self.object_id,
            "data": self.date,
            "messaggio": self.strings[0],
            "messaggioWords": self.splitted_strings[0],
            "messaggioTags": self.pos[0],
            "sentimentMessaggio": SENTIMENT_CODE[self.sentiment[0]],
            "argomento": self.strings[1],
            "argomentoWords": self.splitted_strings[1],
            "argomentoTags": self.pos[1],
            "sentimentArgomento": SENTIMENT_CODE[self.sentiment[1]],
            "chiarimenti": self.strings[2],
            "chiarimentiWords": self.splitted_strings[2],
            "chiarimentiTags": self.pos[2],
            "sentimentChiarimenti":SENTIMENT_CODE[self.sentiment[2]],
            "annotatore": ANNOTATOR_NAME
            }

        update_json(data, filename)
        
class PosTaggingPrompt(Cmd):
    intro = "Welcome to the Pos Tagging shell! \n Type ? or TAB to list commands"
    prompt = "POS> "

    def do_exit(self, inp):
        print("Exiting POS tagging shell...")
        return True

    # Used to tag objects identified with given id
    def do_id(self, inp):
        # check if inp is a number
        if inp.isdigit():
            object_id = int(inp)

            # check if inp is a valid number
            if object_id < 0 or object_id > MAX_ID:
                print(f"ERROR: the id passed has to be in the range [0, {MAX_ID}].")
                return
            else:
                pass
                # Get from the dataset.json file the three columns
                # (TagObject) we have to tag.
                record_object = SyntacticRecord(object_id)

                # Tag the three columns and do sentiment analysis.
                record_object.annotate()

                # Append the output to a file named PosOutput.json or
                # create it if it does not exists.
                record_object.write_json_output()
                
        else:
            print("Error: wrong argument, a positive number must be supplied after id <number>")
            return

    # TODO: Used to annotate POS tagging and sentiment analysis for a
    # given number of uniformly randomly choosen phrase.
    '''
    def do_rand_id(self, inp):
        inp = re.sub(' +', ' ', inp).strip()

        if not inp.isdigit():
            print("ERROR: wrong argument, a positive number must be supplied after rand_id <number>")
        else:
            number_of_annotations = int(inp)
            if number_of_annotations < 0 or number_of_annotations > MAX_ID:
                print(f"ERROR: The number of annotations must be in the range [0, MAX_ID]")
            else:
                # arg validation finished
                # TODO: generate rand numbers and do the annoations.
                pass
    '''

    def do_file(self, inp):
        filename = inp
        
        if not os.path.isfile(filename):
            print("ERROR: the file does not exists.")
        else:
            with open(filename, "r") as f:
                data = f.read()
                data = re.sub(' +', ' ', data).replace("\n", "").replace("\t", "").split(",")
                
                for object_id in data:
                    # TODO: check this error?
                    # print("ERROR: file must contains a list of couples of the form <id1-id2>")
                    if int(object_id) < 0 or int(object_id) > MAX_ID:
                       print(f"ERROR: ERROR: the ids in the file have to be in the range [0, {MAX_ID}]")
                    else:
                        # correct
                        object_id = int(object_id)
                        record_object = SyntacticRecord(object_id)
                        record_object.annotate()
                        record_object.write_json_output()

class SemanticRecord:
    def __init__(self, object_id1, object_id2, column_id):
        global ANNOTATOR_NAME
        
        self.object_ids = [object_id1, object_id2]
        self.column_id  = column_id
        self.similarity = -1
        self.annotator  = ANNOTATOR_NAME
        
        with open("dataset.json", 'r', encoding="latin_1") as dataset:
            json_dataset = json.load(dataset)

            column_num2str = {
                0: "messaggio",
                1: "argomento",
                2: "chiarimenti"
            }
            
            for json_object in json_dataset:
                if json_object['id'] == object_id1:
                    # get data regarding first object
                    self.string_1 = json_object[column_num2str[column_id]]
                elif json_object['id'] == object_id2:
                    # get data regarding second object                    
                    self.string_2 = json_object[column_num2str[column_id]]

    def annotate(self):
        print(f"/-----------------------------------------------------/")                
        done = 0
        while not done:
            print(f"Do similarity analysis on the following (id_1 = {self.object_ids[0]}, id_2 = {self.object_ids[1]}; column = {self.column_id}):\n\n"
                  f"\t\"{self.string_1}\"\n"
                  f"\t\"{self.string_2}\"\n")
            
            print("The available values are:\n"
                  "\n"
                  "\t1 = molto diverse\n"
                  "\t2 = diverse\n"
                  "\t3 = poco diverse\n"
                  "\t4 = abbastanza simili\n"
                  "\t5 = simili\n"
                  "\t6 = molto simili\n")

            similarity_input = input()
            done = 1
            if not similarity_input.isdigit() or int(similarity_input) > 6:
                print("ERROR: invalid similarity value!")
                done = 0
                
        self.similarity =  int(similarity_input)
        return

    def write_json_output(self):
        global ANNOTATOR_NAME
        
        filename = "semantics.json"

        column_num2str = {
            0: u"Messaggio docente",
            1: u"Argomento più interessante",
            2: u"Argomento meno chiaro"
        }
        
        # construct dict object
        data = {
            "firstId": self.object_ids[0],
            "secondId": self.object_ids[1],
            "column": column_num2str[self.column_id],
            "firstPhrase": self.string_1,
            "secondPhrase": self.string_2,
            "voto": self.similarity,
            "annotatore": ANNOTATOR_NAME,            
            "id": str(self.object_ids[0]) + "-" + str(self.object_ids[1]) + "-" + str(self.column_id)
        }

        '''# 
        TODO: find a way to remove last ']' character and replace it
        with ',' if the file was already opened.
        '''
        update_json(data, filename)

class SemanticTaggingPrompt(Cmd):
    intro = "Welcome to the Semantic Tagging Shell! \n Type ? or TAB to list commands"
    prompt = "SEM> "

    def do_exit(self, inp):
        print("Exiting semantic tagging shell...")
        return True

    # Used to annotate a given couple of 
    def do_id(self, inp):

        object_ids = inp.split("-")
        if len(object_ids) != 2 or not object_ids[0].isdigit() or not object_ids[1].isdigit():
            print("Error: wrong argument formatting, the correct format is id <id1-id2>")
        else:
            id_1, id_2 = int(object_ids[0]), int(object_ids[1])
            if id_1 < 0 or id_1 > MAX_ID or id_2 < 0 or id_2 > MAX_ID:
                print(f"ERROR: ERROR: the id passed has to be in the range [0, {MAX_ID}]") 
            else:
                # find info about the two phrases
                column_0 = SemanticRecord(id_1, id_2, 0)
                column_1 = SemanticRecord(id_1, id_2, 1)
                column_2 = SemanticRecord(id_1, id_2, 2)
                
                # annotate them
                column_0.annotate()
                column_1.annotate()
                column_2.annotate()
                
                # write output in json file
                column_0.write_json_output()
                column_1.write_json_output()
                column_2.write_json_output()

    # TODO: Used to annotate similarity for a given number of
    # uniformly randomly choosen phrase.
    # def do_rand_id(self, inp):
    # pass

    def do_file(self, inp):
        filename = inp
        
        if not os.path.isfile(filename):
            print("ERROR: the file does not exists.")
        else:
            with open(filename, "r") as f:
                couples = f.read()
                couples = re.sub(' +', ' ', couples).replace("\n", "").replace("\t", "").split(",")
                
                for couple in couples:
                    id1, id2 = couple.split("-")
                    # TODO: check this error?
                    # print("ERROR: file must contains a list of couples of the form <id1-id2>")
                    if int(id1) > MAX_ID or int(id2) > MAX_ID:
                        print(f"ERROR: ERROR: the ids in the file have to be in the range [0, {MAX_ID}]")
                    else:
                        # correct
                        id_1, id_2 = int(id1), int(id2)
                        
                        # find info about the two phrases
                        column_0 = SemanticRecord(id_1, id_2, 0)
                        column_1 = SemanticRecord(id_1, id_2, 1)
                        column_2 = SemanticRecord(id_1, id_2, 2)
                
                        # annotate them
                        column_0.annotate()
                        column_1.annotate()
                        column_2.annotate()
                        
                        # write output in json file
                        column_0.write_json_output()
                        column_1.write_json_output()
                        column_2.write_json_output()
        
class BasicPrompt(Cmd):
    intro = "Type ? or TAB to list commands"
    
    def do_exit(self, inp):
        print("Exiting main shell...")
        return True

    def do_auth(self, inp):
        global ANNOTATOR_NAME
        
        #ANNOTATOR_NAME = inp.split(" ")[0]
        ANNOTATOR_NAME = inp
        if len(ANNOTATOR_NAME) > 0:
            print(f"Welcome: {ANNOTATOR_NAME}!")
        else:
            print(f"ERROR: name must have at least 1 character!")

    def do_pos_tagging(self, inp):
        global ANNOTATOR_NAME
        
        if ANNOTATOR_NAME == "":
            print("ERROR: login first by doing auth <name>")
        else:
            PosTaggingPrompt().cmdloop()

    def do_semantic_tagging(self, inp):
        global ANNOTATOR_NAME

        if ANNOTATOR_NAME == "":
            print("ERROR: login first by doing auth <name>")
        else:
            SemanticTaggingPrompt().cmdloop()
        
if __name__ == "__main__":
    BasicPrompt().cmdloop()
