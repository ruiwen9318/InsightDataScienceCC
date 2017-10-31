# Project Title

Insight Data Engineering - Coding Challenge ＜/br＞
Find the running median of transaction amount associated with the same CMTE_ID and ZIP_CODE.＜/br＞
Find the median of transaction amount associated with the same CMTE_ID and TRANSACTION_DT.

# Getting Started

The code is written in Java8. And my code is under src folder.

To deal with the challenge I used HashMap and Heap data structure to store the data. When computing the running median, I used TreeMap as an implementation of Heap. The time complexity of the getRunningMedian function is O(log N), where N is the total number of rows in the input file.

I test the getRunningMedian function with JUnit in Eclipse, and I put my unit test file in the following path "src/unit_test/AmountHeapTest.java".

# Executing

Simply replace the itcont.txt file in the input folder with your test file and run  "run.sh".＜/br＞
Please do not change the input file name.

# Running the tests

I created my own test cases and put them in the insight_testsuite folder.＜/br＞
Simply run "run_tests.sh" under insight_testsuite folder.

# Authors
Name: Ruiwen Liang＜/br＞
Email: ruiwenli@usc.edu＜/br＞
Phone: 323-697-9683＜/br＞
School: University of Southern California