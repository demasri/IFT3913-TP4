# Auteur : Daniel El-Masri (20096261)
# Date : 18 Devembre 2020
# IFT3913 - Travail Pratique 4

# Step 0 : Initialisation
import os
from os import path as pt
import sys
import subprocess as sp 
import glob
import csv

# Step 1 : Clone git repository
def clone_git_repository(url):
	repo = url.split('/')[-1][:-4]

	if(pt.exists(repo)):
		print('Repository already exists... Pulling lastest changes')
	else:
		print('Cloning repository into working folder...')
		os.system('git clone ' + url)

	working_directory = sp.getoutput('cd')

	os.chdir(str(working_directory) + '/' + repo)
	os.system('git reset --hard')
	os.system('git pull')


# Step 2 : Get list of commit hashes from master
def get_hashes_list():
	print('Retrieving commit hashes...')
	hash_list = sp.getoutput('git rev-list master')
	return hash_list.split('\n')

# Step 3-a : Go through each committed version
def go_through_master_history(hash_list, path):
	result = [['id_version', 'n_classes']]
	print('Going through repository versions...')
	for hash in hash_list:
		os.system('git reset --hard ' + hash)
		col = [hash]
		col.append(count_java_files(path))

		result.append(col)
	print('Finished going through repository versions...')

	return result

# Step 3-b : Count the number of .java files
def count_java_files(path):
	result = 0
	print('Counting java files in repository...')
	for root, dirs, files in os.walk(path):
	    for file in files:
	        if file.endswith('.java'):
	            result += 1
	        else:
	        	result += 0
	return result

# Step 4 : Calculate BC metric for each version
def calculate_BC_metrique():



# Step 5 : Write output CSV file
def write_output_csv_file(data):
	print('Writing data to CSV file...')
	with open('../CSV/result.csv', 'w', newline='') as filewriter:
		writer = csv.writer(filewriter)
		writer.writerows(data)





# Main method
def main():
	url = sys.argv[1]

	clone_git_repository(url)

	path = sp.getoutput('cd')

	hashes = get_hashes_list()

	data = go_through_master_history(hashes, path)

	write_output_csv_file(data)


if __name__ == '__main__':
	main()