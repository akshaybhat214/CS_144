#!/bin/bash

# Run the drop.sql batch file to drop existing tables
mysql CS144 < drop.sql

# Run the create.sql batch file to create the database and tables
mysql CS144 < create.sql

# Compile and run the parser to generate the appropriate load files
ant run-all

#Sort all .csv files to remove duplicates
sort -u -o item-data.csv item-data.csv
sort -u -o category-data.csv category-data.csv
sort -u -o user-data.csv user-data.csv
sort -u -o location-data.csv location-data.csv
sort -u -o latlong-data.csv latlong-data.csv
sort -u -o bid-data.csv bid-data.csv

# Run the load.sql batch file to load the data into the databses
mysql CS144 < load.sql

#Remove .csv files (if they exist) so they do not get appended to.
rm item-data.csv category-data.csv user-data.csv location-data.csv latlong-data.csv bid-data.csv