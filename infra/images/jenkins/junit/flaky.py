#!/usr/bin/env python

import json
import glob
import argparse
import os

def parse_args():
    """
    Parse input arguments
    """
    global input_folder
    parser = argparse.ArgumentParser()

    parser.add_argument('-i','--input',
        help='input folder',
        required=True,
        type=str)

    args=parser.parse_args()
    input_folder=args.input

    if not os.path.exists(input_folder):
        print('{input_folder} not found'.format(input_folder=input_folder))
        sys.exit(1)

def main():
    parse_args()
    master={}
    success={}
    for file in glob.glob(input_folder+"/SERENITY*json"):
        with open(file) as json_file:
            data = json.load(json_file)
            master[file]=data
            testcase_list=data['testsuite']['testcase']
            if isinstance(data['testsuite']['testcase'],dict):
                testcase_list=[data['testsuite']['testcase']]
            for idx,testcase in enumerate(testcase_list):
               if len(testcase.keys()) == 1:
                   success[testcase['@name']]=True

    result={}
    for file,data in master.items():
        result[file]={'testsuite':{'testcase':[]}}
        result[file]['testsuite']['@errors']=0
        result[file]['testsuite']['@failures']=0
        result[file]['testsuite']['@name']=master[file]['testsuite']['@name']
        result[file]['testsuite']['@skipped']=master[file]['testsuite']['@skipped']
        result[file]['testsuite']['@tests']=0
        result[file]['testsuite']['@time']=master[file]['testsuite']['@time']
        result[file]['testsuite']['@timestamp']=master[file]['testsuite']['@timestamp']
        testcase_list=data['testsuite']['testcase']
        if isinstance(data['testsuite']['testcase'],dict):
            testcase_list=[data['testsuite']['testcase']]

        for idx,testcase in enumerate(testcase_list):
            if len(testcase.keys())>1 and testcase['@name'] not in success:
                result[file]['testsuite']['testcase'].append(testcase)
                result[file]['testsuite']['@failures']+=1
                result[file]['testsuite']['@tests']+=1
            elif len(testcase.keys())==1:
                result[file]['testsuite']['testcase'].append(testcase)
                result[file]['testsuite']['@tests']+=1

        result[file]['testsuite']['@errors']=str(result[file]['testsuite']['@errors'])
        result[file]['testsuite']['@failures']=str(result[file]['testsuite']['@failures'])
        result[file]['testsuite']['@tests']=str(result[file]['testsuite']['@tests'])

    for file,data in result.items():
        with open(input_folder+'/out/'+os.path.basename(file), 'w') as fp:
            json.dump(data, fp)

if __name__ == "__main__":
    input_folder=""
    main()
