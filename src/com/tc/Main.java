package com.tc;

public class Main {

    public static void main(String[] args) {
	// write your code here
        String fileName="/home/wu/Desktop/ip_host.cvs";
        CSVFileUtil csvFileUtil=new CSVFileUtil();
        csvFileUtil.handleCVSFile(fileName);
    }
}
