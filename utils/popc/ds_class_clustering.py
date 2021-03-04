#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Mon Apr  6 14:42:06 2020

@author: danielogenrwot
"""

from sklearn.cluster import KMeans
#import numpy as np
import pandas as pd
from disp import display
from popc import popc

# locate the binary file
data_k9_binary = pd.read_csv('../Analytics/Results/csv/all_labeled_data_desktop_18_01_2021.csv')
X = data_k9_binary.iloc[:, [4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21]].values

kmeans = KMeans(n_clusters=6, random_state=0).fit(X)
result = []
for i in range(len(X)):
        result.append([kmeans.labels_[i], X[i]])
display(result, 'kmeans')

labels = popc(X)
print(labels)
# add new column to the dataframe
data_k9_binary['cluster'] = labels 
# write new dataframe to csv
data_k9_binary.to_csv('../Analytics/Results/csv/all_labeled_data_desktop_18_01_2021_clustered.csv')

result = []
for i in range(len(X)):
        result.append([labels[i], X[i]])
display(result, 'popc')