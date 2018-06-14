#!/bin/bash
#
# Copyright © 2018 Ingo Thomsen (http://www.gerdi-project.de)
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#



#
# This is a one time help script, to extract the JSON description of each 
# time series dataset from the complete (original) JSON response from 
# OceanTEA in the file all.json
#

# exit in case of statements with non-zero return value or undefined variables
set -u 

outputdir=all_time_series_with_single_dataset_each

cat all.json  | tr -d '\n' | sed 's/^.*\[//' | sed 's/\].*$//' | sed 's/\},/\n/g' | split -l 1 

mkdir -p $outputdir

for f in x*; do 

	grep -ci adcp $f > /dev/null && rm -f $f && continue

	pos=$(     sed  -E 's|.*"station"\s*:\s*"([^"]*)".*|\1|I' $f)	
	datatype=$(sed  -E 's|.*"datatype"\s*:\s*"([^"]*)".*|\1|I' $f)	
	depth=$(   sed  -E 's|.*"depth"\s*:\s*([.0-9]*).*|\1|I' $f)	

	output=$outputdir/${pos}_${datatype}_${depth}.json

	{
		echo '''{
  "timeseries" : [
      {'''		
	  
  	# minimal pretty print
	sed  -E  -e '''
		s/\s*\{// ;
		s/}\s*$// ;
		s/\s*$//  ;
		s/.*/\0,/ ;
		s/,/,\n/g ;
 
	''' $f | sort | sed -E  -e '''
		$s/,// ;	
		  /^\s*$/d ;
	'''
		echo '''      }
   ]
}'''
	} > $output

	rm $f -f
done
