# Auteur : Daniel El-Masri (20096261) & Bach, Rym (20078272)
# Date : 18 Devembre 2020
# IFT3913 - Travail Pratique 4


# References:
# 1- https://docs.python.org/3/library/csv.html
# 2- https://docs.scipy.org/doc/scipy-0.14.0/reference/generated/scipy.stats.pearsonr.html
# 3- https://docs.scipy.org/doc/scipy/reference/generated/scipy.stats.normaltest.html
# 4- https://stackoverflow.com/questions/11604653/add-command-line-arguments-with-flags-in-python3


# Step 0 : Initialisation
import os # for system commands
from os import path as pt
import sys
import subprocess as sp 
import glob # for file looping and searching
import csv # to write and read csv files
import time # allow pauses in code
import statistics as stat # calculate median
from scipy import stats as stt # for normality and correlation tests
import argparse # allow for script flags
import timeit # to calculate the total runtime

parser = argparse.ArgumentParser()
parser.add_argument("-max", "--maximum", help="Maximum number of commits")
parser.add_argument('-url', '--repository', help="Repository link")

BC_METRIC = 6
N_CLASSES_INDEX = 1
M_C_BC_INDEX = 2
CA_PATH = sp.getoutput('cd') + '\\CodeAnalyzer'
WORKING_DIRECTORY = sp.getoutput('cd')


# Step 1 : Clone git repository
def clone_git_repository(url, repo_name):

	# check if repository already exists
	if(pt.exists(repo_name)):
		print('Repository already exists... Pulling lastest changes')
	else:
		print('Cloning repository into working folder...')
		os.system('git clone ' + url)

	working_directory = sp.getoutput('cd')

	os.chdir(str(working_directory) + '/' + repo_name)
	os.system('git reset --hard')
	os.system('git pull')


# Step 2 : Get list of commit hashes from master
def get_hashes_list():
	print('Retrieving commit hashes...')
	hash_list = sp.getoutput('git rev-list master')
	return hash_list.split('\n')

# Step 3-a : Go through each committed version
def go_through_master_history(hash_list, path, maximum_tries=None):
	result = [['id_version', 'n_classes', 'm_c_BC']]
	i = 0
	print('Going through repository versions...')	
	for hash in hash_list:
		i += 1

		# sets repo to hash version
		os.system('git reset --hard ' + hash)

		# computes information
		col = [hash]
		col.append(count_java_files(path))

		# goes to CodeAnalyzer folder
		os.chdir(CA_PATH)

		execute_CodeAnalyzer(path)

		col.append(calculate_median_BC(path))

		# returns to repository
		os.chdir(path)

		result.append(col)

		if(maximum_tries and i >= maximum_tries):
			break
	print('Finished going through repository versions...')

	return result

# Step 3-b : Count the number of .java files
def count_java_files(path):
	result = 0
	print('Counting java files in repository...')
	# got through file to get the .java ones
	for root, dirs, files in os.walk(path):
	    for file in files:
	        if file.endswith('.java'):
	            result += 1
	        else:
	        	result += 0
	return result

# Step 4-a : Compile and Execute CodeAnalyzer on repository
def compile_CodeAnalyzer(project_path):
	# goes to CodeAnalyzer folder
	os.chdir(CA_PATH)

	print('Compiling CodeAnalyzer...')
	os.system('javac CodeAnalyzer.java')

	time.sleep(5)

	# returns to repository
	os.chdir(project_path)

def execute_CodeAnalyzer(project_path):

	print('Executing CodeAnalyzer on ' + project_path)
	os.system('java CodeAnalyzer "' + project_path + '"')

	time.sleep(5)

# Step 4-b : Read output CSV and calculate BC median
def calculate_median_BC(project_path):
	results = []

	# reads csv file to get all classe_BC
	results = __get_specific_data_from_column('./CSV/classes.csv', BC_METRIC)

	if(len(results) > 0):
		return stat.median(results)
	else:
		return -1


# Step 5 : Write output CSV file
def write_output_csv_file(data, repo_name):
	print('Writing data to CSV file...')
	filename = 'result_' + repo_name
	with open('../CSV/' + filename + '.csv', 'w', newline='') as filewriter:
		writer = csv.writer(filewriter)
		writer.writerows(data)

# Step 6-a : Check for data normality
def check_for_normality(repo_name):
	filename = WORKING_DIRECTORY + '\\CSV\\result_' + repo_name + '.csv'

	print("Calculating P-Value for classe_BC metric...")
	data = __get_specific_data_from_column(filename, M_C_BC_INDEX)

	result = stt.normaltest(data)
	alpha = 1e-3

	print("The p-value calculated is : " + str(result.pvalue))

	# null hypothesis: x comes from a normal distribution
	if(result.pvalue < alpha):
		print("The null hypothesis can be rejected, the data is not normally distributed...")
	else:
		print("The null hypothesis cannot be rejected, the data is normally distributed...")

# Step 6-b : Check for data correlation
def check_for_correlation(repo_name):
	filename = WORKING_DIRECTORY + '\\CSV\\result_' + repo_name + '.csv'

	print("Calculating Pearson's correlation coefficent between data...")
	n_classes = __get_specific_data_from_column(filename, N_CLASSES_INDEX)
	m_c_bc = __get_specific_data_from_column(filename, M_C_BC_INDEX)

	result = stt.pearsonr(n_classes, m_c_bc)

	if(1 - abs(result[0]) < 0.9):
		print('Correlation coefficent is ' + str(result[0]) + '... The data is not correlated...')
	else:
		print('Correlation coefficent is ' + str(result[0]) + '... The data is correlated...')



# Main method
def main():
	# beautify the console
	os.system('cls')
	os.system('echo ====================================================')
	os.system('echo 	              	   PROTO		      			')
	os.system('echo ====================================================')

	args = parser.parse_args()

	url = args.repository
	repository = url.split('/')[-1][:-4]
	if args.maximum:
		maximum = int(args.maximum)
	else:
		maximum = None

	start = timeit.default_timer()

	clone_git_repository(url, repository)

	path = sp.getoutput('cd')

	hashes = get_hashes_list()

	compile_CodeAnalyzer(path)

	data = go_through_master_history(hashes, path, maximum)

	write_output_csv_file(data, repository)

	check_for_normality(repository)

	check_for_correlation(repository)

	stop = timeit.default_timer()

	print('Total execution time is ' + str(stop - start) + 's')

# =============================================================================
# Private helper functions
# =============================================================================

def __get_specific_data_from_column(file, index):
	results = []

	# reads csv file to get all data from specific column
	with open(file, newline='') as f:
		reader = csv.reader(f)

		for row in reader:
			if(len(results) == 0):
				results.append(0)
			else:
				results.append(float(row[index])) 

	# exclude title from data
	results.pop(0)

	return results


if __name__ == '__main__':
	main()