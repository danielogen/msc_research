#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Fri Oct  4 09:24:24 2019

@author: danielogenrwot
"""

from __future__ import print_function
import os
import re
import csv

def read_original_data(basepath):
    files = []   
    for file in os.listdir(basepath):
        path = os.path.join(basepath, file)
        if os.path.isdir(path):
            # skip hidden directories
            continue
        else:
            files.append(path)
    
    print(files)
       
    # call write csv function and pass list of file names
    write_csv_data(basepath, files)
        
        
def write_csv_data(basepath, files):
    anti_patternlist = ['SpeculativeGenerality', 'BaseClassKnowsDerivedClass', 'MessageChains', 
                    'LongParameterList', 'SpaghettiCode', 'BaseClassShouldBeAbstract',
                    'LongMethod', 'ClassDataShouldBePrivate', 'TraditionBreaker', 
                    'ManyFieldAttributesButNotComplex', 'RefusedParentBequest', 'SwissArmyKnife',
                    'Blob', 'AntiSingleton', 'ComplexClass', 'LargeClass', 'FunctionalDecomposition',
                    'LazyClass']
    header = ['FullClassPath', 'AntiPattern']
    # data = files[20]
    datas = files
    for data in datas:
        for ap in anti_patternlist:
            search  = re.search(ap, data)
            if search is not None:
                anti_pattern = search.group()
                filename = basepath + anti_pattern + '.csv'
                # write header
                f = open(filename, 'w')
                writer = csv.DictWriter(f, fieldnames=header)
                writer.writeheader()
                f.close()
            else:
                continue
        
        f = open(data, 'r')
        content = f.read();
        # r = re.compile(r'k9mail[a-zA-Z0-9.-]+') --- for k9-mail
        # r = re.compile(r'com.eteks.sweethome3d[a-zA-Z0-9.-]+')
        # r = re.compile(r'wallet.src.[a-zA-Z0-9.-]+')
        # r = re.compile(r'[a-zA-Z0-9-]+.java.org.mars_sim.[a-zA-Z0-9.-]+')
        # r = re.compile(r'CH.ifa.draw[a-zA-Z0-9.-]+') # jhotdraw v5.3
        # r = re.compile(r'org.argouml[a-zA-Z0-9.-]+') # argouml-v0.35.1
        # r = re.compile(r'(net.sourceforge.ganttproject.[a-zA-Z0-9.-_]+|biz.ganttproject.[a-zA-Z0-9.-_]+|org.ganttproject.[a-zA-Z0-9.-_]+|org.w3c.[a-zA-Z0-9.-_]+|com.googlecode.[a-zA-Z0-9.-_]+)') # ganttproject-v2.8.11
        # r = re.compile(r'(com.ultramixer.[a-zA-Z0-9.-_]+|org.gjt.sp.[a-zA-Z0-9.-_]+|org.jedit.[a-zA-Z0-9.-_]+)') # jEdit-v5.5.0
        # r = re.compile(r'org.mars_sim.[a-zA-Z0-9.-]+') # mars-sim-v3.1.0
        # r = re.compile(r'edu.usf.cutr.opentripplanne.[a-zA-Z0-9.-_]+') # opentripplanner-for-andriod-v2.1.5
        # r = re.compile(r'(com.keepassdroid.[a-zA-Z0-9.-_]+|org.apache.commons.[a-zA-Z0-9.-_]+)') # keepassdroid-v2.5.9
        # r = re.compile(r'org.thoughtcrime.securesms.[a-zA-Z0-9.-]+') # Signal-Android-v4.60.5
        # r = re.compile(r'com.example.android[a-zA-Z0-9.-]+') # android-news-app
        # r = re.compile(r'org.telegram[a-zA-Z0-9.-_]+') # telegram-v6.1.1
        '''org.appdontnet4j.
        org.tweetalib.
        com.tweetlanes.
        com.twitter.
        com.inscription.
        org.asynctasktex.
        org.socialnetlib.'''
        # r = re.compile(r'(org.[a-zA-Z0-9.-_]+|com.[a-zA-Z0-9.-_]+)') # TweetLanes-v1.4.1
        # r = re.compile(r'android.net.[a-zA-Z0-9.-_]+') # android-10-platform-net-service
        # r = re.compile(r'freemind[a-zA-Z0-9.-_]+') 
        # r = re.compile(r'(com.google.checkstyle.[a-zA-Z0-9.-_]+|org.checkstyle.[a-zA-Z0-9.-_]+)')
        # r = re.compile(r'groupxii[a-zA-Z0-9.-_]+') # gogreen-v.0.1.3
        # r = re.compile(r'net.azib.ipscan[a-zA-Z0-9.-_]+') # ipscan-v3.7.3 
        # r = re.compile(r'(ca.mcgill.cs.jetuml.[a-zA-Z0-9.-_]+|org.json.[a-zA-Z0-9.-_]+)') # jetuml-v3.1 
        # r = re.compile(r'jpass[a-zA-Z0-9.-_]+') # jpass-v0.1.20
        # r = re.compile(r'org.kse[a-zA-Z0-9.-_]+') # keystore-explorer-v5.4.4
        # r = re.compile(r'com.chatroom[a-zA-Z0-9.-_]+') # LiveChatServer-v4.1
        # r = re.compile(r'(com.athaydes.logfx.[a-zA-Z0-9.-_]+|org.slf4j.[a-zA-Z0-9.-_]+)') # LogFX-v0.9.1
        # r = re.compile(r'(org.pgptool[a-zA-Z0-9.-_]+|org.jdesktop.[a-zA-Z0-9.-_]+)') # LogFX-v0.9.1
        # r = re.compile(r'com.amaze[a-zA-Z0-9.-_]+') # AmazeFileManager-v3.5.2
        # r = re.compile(r'de.danoeh.antennapod[a-zA-Z0-9.-_]+') #AntennaPod-v2.1.1
        # r = re.compile(r'io.github.hidroh.[a-zA-Z0-9.-_]+') #materialistic-v3.3
        # r = re.compile(r'it.feio.android.[a-zA-Z0-9.-_]+') #Omni-Notes-v6.0.5
        # r = re.compile(r'com.xiecc.seeWeather.[a-zA-Z0-9.-_]+') #seeWeather-v2.03
        # r = re.compile(r' com.jakewharton.[a-zA-Z0-9.-_]+') #Telecine-v1.6.2
        r = re.compile(r'com.naman14.timber.[a-zA-Z0-9.-_]+') #Timber-v1.8
        
        matched_str = r.findall(content)
        if len(matched_str) is not 0:
            # write to csv
            print(len(matched_str))
            to_write = []
            for cl in matched_str:
                # create a pair of classnames and anti_pattern 
                to_write = [cl, anti_pattern]
                with open(filename, 'a', newline ='') as file:
                    writer = csv.writer(file, delimiter=',')
                    writer.writerow(i for i in to_write)


if __name__ == '__main__':
    
    # data_path = ['DesignSmells']
    
    """design_smell = ['Bitcoin-wallet-6.31/Metrics-Bitcoint-wallet',
           'K9/Metrics-K9',
           'Mars-simulation/Metrics-Mars-simulation',
           'SweetHome3Dv5.6_new/Metrics-SweetHome3D_new',
           'SweetHome3Dv5.6_old/Metrics']"""
    # design_smell = data_path[0] + '/K9/Design Smell detection/'
    # design_smell = '../Design Smell/Mobile/android-10-platform-net-service/'
    design_smell = '../Design Smell/Mobile/Timber-v1.8/'

    
    read_original_data(design_smell)
    # write_csv_data(data_path)